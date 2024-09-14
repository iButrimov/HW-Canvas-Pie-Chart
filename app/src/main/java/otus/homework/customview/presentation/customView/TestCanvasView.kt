package otus.homework.customview.presentation.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class TestCanvasView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {

    private val blackPaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 10f
        style = Paint.Style.FILL
    }

    private val boldRedPaint = Paint().apply {
        color = Color.RED
        strokeWidth = 30f
    }

    private val greenStrokePaint = Paint().apply {
        color = Color.GREEN
        strokeWidth = 10f
        style = Paint.Style.STROKE
    }

    private val path = Path()

    override fun onDraw(canvas: Canvas) {
        val midW = width / 2f
        val midH = height / 2f

        path.reset()
        path.moveTo(0f,0f)
        path.lineTo(midW - 200f, 200f)
        path.lineTo(midW + 200f, 400f)
        path.quadTo(700f, 700f, 0f, 500f)
        path.cubicTo(300f, 0f, 600f, 1000f, 1000f, 500f)

        repeat(10) {
            path.rLineTo(-50f, 50f)
            path.rLineTo(50f, 50f)
        }

        path.close()

        greenStrokePaint.pathEffect = CornerPathEffect(30f)
        greenStrokePaint.pathEffect = DashPathEffect(floatArrayOf(50f, 20f), 0f)

        canvas.drawPath(path, greenStrokePaint)

    }
}