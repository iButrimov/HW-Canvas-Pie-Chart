package otus.homework.customview.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import otus.homework.customview.R
import otus.homework.customview.data.repository.CostRepository
import otus.homework.customview.domain.model.Cost
import javax.inject.Inject

@HiltViewModel
class CostViewModel @Inject constructor(
    private val repository: CostRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<CostViewState>(CostViewState.DefaultState)
    val viewState = _viewState.asStateFlow()

    private var costList: List<Cost> = emptyList()

    init {
        initState()
    }

    private fun initState() {
        viewModelScope.launch {
            val result = repository.getCostList(R.raw.payload).also {
                costList = it
            }
            _viewState.value = CostViewState.DataState(
                costList = result,
            )
        }
    }
}