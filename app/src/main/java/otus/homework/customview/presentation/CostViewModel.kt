package otus.homework.customview.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import otus.homework.customview.R
import otus.homework.customview.data.repository.CostRepository
import otus.homework.customview.domain.model.Cost
import otus.homework.customview.domain.model.CostCategoryType
import javax.inject.Inject

@HiltViewModel
class CostViewModel @Inject constructor(
    private val repository: CostRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<CostViewState>(CostViewState.DefaultState)
    val viewState = _viewState.asStateFlow()

    private var costList: List<Cost> = emptyList()

    init {
        Log.d("CHECK_LIST", "CostViewModel: init")
        initState()
    }

    private fun initState() {
        Log.d("CHECK_LIST", "CostViewModel: initState")
        viewModelScope.launch {
            val result = repository.getCostList(R.raw.payload).also {
                costList = it
            }
            Log.d("CHECK_LIST", "result: $result")
            Log.d("CHECK_LIST", "result: id ${result.first().id}")
            Log.d("CHECK_LIST", "result: name ${result.first().name}")
            Log.d("CHECK_LIST", "result: category ${result.first().category}")
            Log.d("CHECK_LIST", "result: getDayOfMonth ${result.first().day}")
            _viewState.value = CostViewState.DataState(
                costList = result,
                selectedCategory = null,
                daysQty = getDaysQty(result)
            )
        }
    }

    private fun getCostListByCategory(category: CostCategoryType): List<Cost> {
        return costList.filter { it.category == category }
    }

    private fun getDaysQty(list: List<Cost>): Int {
        return list.groupBy { it.day }.size
    }

}