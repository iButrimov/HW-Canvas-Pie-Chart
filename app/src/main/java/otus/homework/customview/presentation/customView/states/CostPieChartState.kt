package otus.homework.customview.presentation.customView.states

import android.os.Parcelable
import android.view.View
import kotlinx.parcelize.Parcelize

@Parcelize
class CostPieChartState(
    private val superSavedState: Parcelable?,
    val data: Map<String, Int>
) : View.BaseSavedState(superSavedState), Parcelable