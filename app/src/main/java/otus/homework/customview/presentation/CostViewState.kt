package otus.homework.customview.presentation

import otus.homework.customview.domain.model.Cost
import otus.homework.customview.domain.model.CostCategoryType

sealed class CostViewState {
    object DefaultState : CostViewState()
    class DataState(
        val costList: List<Cost>,
        val selectedCategory: CostCategoryType?,
        val daysQty: Int,
    ) : CostViewState()
}