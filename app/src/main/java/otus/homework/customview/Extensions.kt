package otus.homework.customview

import android.content.Context
import kotlin.math.roundToInt

fun Context.pxToDp(px: Int) = px / this.resources.displayMetrics.density
fun Context.dpToPx(value: Number): Int {
    return if (value != 0) {
        (resources.displayMetrics.density * value.toDouble()).roundToInt()
    } else {
        0
    }
}