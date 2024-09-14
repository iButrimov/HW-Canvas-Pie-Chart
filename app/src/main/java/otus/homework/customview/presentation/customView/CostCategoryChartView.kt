package otus.homework.customview.presentation.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import otus.homework.customview.domain.model.Cost

class CostCategoryChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {

    private val costs = mutableListOf<Cost>()

    private var maxValue = 0f
    private var minValue = 0f

    private val blackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 8f
    }

    private val redPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 8f
    }

    private val path = Path()

    fun setData(costList: List<Cost>, daysQty: Int) {
        Log.d("CHECK_LIST", "onDraw setData costList = $costList")
        costs.clear()
        costs.addAll(costList)

//        maxValue = context.dpToPx(list.maxOf { it.amount }).toFloat()
//        minValue = context.dpToPx(list.minOf { it.amount }).toFloat()

        maxValue = costs.maxOf { it.amount }.toFloat()
        minValue = costs.minOf { it.amount }.toFloat()
        Log.d("CHECK_LIST", "onDraw maxValue = $maxValue")
        Log.d("CHECK_LIST", "onDraw minValue = $minValue")
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        if (costs.isEmpty()) return
        Log.d("CHECK_LIST", "onDraw invoked list size = ${costs.size}")
        val wStep = width.toFloat() / costs.size.toFloat()
        val hStep = height.toFloat() / (maxValue - minValue)
        Log.d("CHECK_LIST", "onDraw hStep = $hStep")
        path.reset()
        path.moveTo(0f, maxValue)
        var x = 0f
        var y = 0f
        costs.onEach { cost ->
            y = (maxValue - (cost.amount.toFloat() - minValue)) * hStep
            Log.d("CHECK_LIST", "onDraw onEach y = $y for ${cost.name}")
            path.lineTo(x, y)
            x += wStep

        }
        canvas.drawPath(path, redPaint)
    }

}