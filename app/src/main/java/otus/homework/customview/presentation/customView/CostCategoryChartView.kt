package otus.homework.customview.presentation.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import otus.homework.customview.domain.model.Cost
import otus.homework.customview.domain.model.CostCategoryType
import otus.homework.customview.px
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class CostCategoryChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {

    private var costsByCategory: Map<String, List<Cost>> = emptyMap()

    private val chartSize = CHART_SIZE.px
    private val margin = STROKE_WIDTH / 2

    private val path = Path()
    private var currentStrokePaint = Paint()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDraw(canvas: Canvas) {
        if (costsByCategory.isEmpty()) return

        val maxAmount = costsByCategory.flatMap { it.value }.maxOfOrNull { it.amount } ?: 0

        // Перевод времени и определение границ периода
        val dates = costsByCategory.flatMap { it.value }
            .map { LocalDateTime.ofInstant(Instant.ofEpochSecond(it.time), ZoneId.systemDefault()) }
        val firstDate = dates.minOrNull() ?: return
        val lastDate = dates.maxOrNull() ?: return

        // Определение общего количества дней для дальнейшего определения интервала
        val daysCount = Duration.between(
            firstDate.toLocalDate().atStartOfDay(),
            lastDate.toLocalDate().atStartOfDay()
        ).toDays()

        val dayStep = width / daysCount

        costsByCategory.values.forEach { costList ->
            path.reset()
            path.moveTo(0f + margin, height.toFloat() - margin)
            val currentCategory = costList[0].category

            costList.sortedBy { it.time }.forEach { cost ->
                val purchaseDay =
                    LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(cost.time),
                        ZoneId.systemDefault()
                    )
                val daysFromStart = Duration.between(
                    firstDate.toLocalDate().atStartOfDay(),
                    purchaseDay.toLocalDate().atStartOfDay()
                ).toDays()
                val x = dayStep * daysFromStart + margin
                val y = height - (height.toFloat() * cost.amount / maxAmount) - margin
                path.lineTo(x, y)

            }
            currentStrokePaint = createPaintByCategory(currentCategory)
            canvas.drawPath(path, currentStrokePaint)
            canvas.drawPath(path, currentStrokePaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec).coerceAtMost(chartSize)
        val height = MeasureSpec.getSize(heightMeasureSpec).coerceAtMost(chartSize)
        setMeasuredDimension(width, height)
    }

    fun setData(costList: List<Cost>) {
        Log.d("CHECK_LIST", "onDraw setData costList = $costList")

        // Группировка по имени категрии
        costsByCategory = costList.groupBy { it.category }
        invalidate()
    }

    private fun createPaintByCategory(categoryName: String): Paint {
        val categoryColor = CostCategoryType.from(categoryName)
        return createPaint(categoryColor.categoryColor)
    }

    private fun createPaint(@ColorRes colorResId: Int): Paint {
        return Paint().apply {
            color = ContextCompat.getColor(context, colorResId)
            strokeWidth = STROKE_WIDTH
            style = Paint.Style.STROKE
            pathEffect = CornerPathEffect(50f)
        }
    }

    companion object {
        private const val STROKE_WIDTH = 8f
        private const val CHART_SIZE = 300
    }
}