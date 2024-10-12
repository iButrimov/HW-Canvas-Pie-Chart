package otus.homework.customview.presentation

import otus.homework.customview.domain.model.Cost

sealed class CostViewState {
    object DefaultState : CostViewState()
    class DataState(
        val costList: List<Cost>,
    ) : CostViewState()
}