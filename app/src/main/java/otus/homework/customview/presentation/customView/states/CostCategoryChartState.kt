package otus.homework.customview.presentation.customView.states

import android.os.Parcelable
import android.view.View
import kotlinx.parcelize.Parcelize
import otus.homework.customview.domain.model.Cost

@Parcelize
class CostCategoryChartState(
    private val superSavedState: Parcelable?,
    val data: Map<String, List<Cost>>
) : View.BaseSavedState(superSavedState), Parcelable