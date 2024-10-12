package otus.homework.customview.presentation.customView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.os.Parcelable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import otus.homework.customview.R
import otus.homework.customview.domain.model.Cost
import otus.homework.customview.domain.model.CostCategoryType
import otus.homework.customview.presentation.customView.states.CostPieChartState
import otus.homework.customview.px
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.sqrt

class CostPieChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {

    private var costsByCategory: Map<String, Int> = emptyMap()

    private val rect = RectF()
    private val chartSize = CHART_SIZE.px

    private val eraserPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.category_transparent)
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    private val sectors: MutableList<PieChartCategoryInfo> = mutableListOf()

    private val gestureDetector =
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

            override fun onDown(e: MotionEvent): Boolean {
                val centerX = (width / 2).toFloat()
                val centerY = (height / 2).toFloat()
                val circleRadius = (width / 2).toFloat()
                val holeRadius = circleRadius / 4

                val xToCenter = abs(centerX - e.x)
                val yToCenter = abs(centerY - e.y)

                // Проверка что точка нажатия внутри отверстия круговой диаграммы
                if (xToCenter <= holeRadius && yToCenter <= holeRadius) return false

                // Проверка что точка нажатия вне круговой диаграммы
                if (isOutsideCircle(centerX, centerY, circleRadius, e)) {
                    return false
                }

                // считаем угол
                val angle = calculateAngle(centerX, centerY, circleRadius, e)

                sectors.forEach {
                    if (it.startAngle <= angle && it.endAngle > angle) {
                        Toast.makeText(context, it.name, Toast.LENGTH_SHORT).show()
                    }
                }

                return true
            }

            private fun isOutsideCircle(
                centerX: Float,
                centerY: Float,
                radius: Float,
                e: MotionEvent
            ): Boolean {
                val distanceSquared =
                    (centerX - e.x) * (centerX - e.x) + (centerY - e.y) * (centerY - e.y)
                return distanceSquared > radius * radius
            }

            private fun calculateAngle(
                centerX: Float,
                centerY: Float,
                radius: Float,
                e: MotionEvent
            ): Float {
                val xToCenter = e.x - centerX
                val yToCenter = e.y - centerY
                val b = sqrt(xToCenter * xToCenter + yToCenter * yToCenter)
                val a = radius
                val c =
                    sqrt((width - e.x) * (width - e.x) + abs(centerY - e.y) * abs(centerY - e.y))

                var angle = acos((a * a + b * b - c * c) / (2 * a * b)) * (180 / Math.PI)
                if (e.y < centerY) angle = 360 - angle
                return angle.toFloat()
            }
        })

    init {
        setWillNotDraw(false)
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        return CostPieChartState(superState, costsByCategory)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val pieChartState = state as? CostPieChartState
        super.onRestoreInstanceState(pieChartState?.superState ?: state)

        costsByCategory = pieChartState?.data ?: emptyMap()
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = resolveSize(chartSize, widthMeasureSpec)
        setMeasuredDimension(size, size)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        if (costsByCategory.isEmpty()) return
        rect.set(0f, 0f, width.toFloat(), height.toFloat())

        val totalAmount = costsByCategory.values.sum().toFloat()
        var startAngle = 0f
        sectors.clear()

        costsByCategory.forEach { (category, amount) ->
            val sweepAngle = (amount / totalAmount) * 360f
            sectors.add(
                PieChartCategoryInfo(
                    category, amount, startAngle, startAngle + sweepAngle
                )
            )
            canvas.drawArc(rect, startAngle, sweepAngle, true, createPaintByCategory(category))
            startAngle += sweepAngle
        }
        canvas.drawCircle(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            (height / 4).toFloat(),
            eraserPaint
        )
    }

    fun setData(costList: List<Cost>) {
        costsByCategory = costList.groupBy { it.category }
            .mapValues { it.value.sumOf(Cost::amount) }
            .toList()
            .sortedByDescending { it.second }
            .toMap()

        invalidate()
    }

    private fun createPaintByCategory(categoryName: String): Paint {
        val categoryColor = CostCategoryType.from(categoryName)
        return createPaint(categoryColor.categoryColor)
    }

    private fun createPaint(@ColorRes colorResId: Int): Paint {
        return Paint().apply {
            color = ContextCompat.getColor(context, colorResId)
            style = Paint.Style.FILL
        }
    }

    private data class PieChartCategoryInfo(
        val name: String,
        val amount: Int,
        val startAngle: Float,
        val endAngle: Float
    )

    companion object {
        private const val CHART_SIZE = 300
    }
}