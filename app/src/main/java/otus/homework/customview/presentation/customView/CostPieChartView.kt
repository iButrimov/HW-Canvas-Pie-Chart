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
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import otus.homework.customview.R
import otus.homework.customview.domain.model.Cost
import otus.homework.customview.domain.model.CostCategoryType
import otus.homework.customview.presentation.customView.states.CostPieChartState
import otus.homework.customview.px

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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = resolveSize(chartSize, widthMeasureSpec)
        setMeasuredDimension(size, size) // Making the view a square (width = height)
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