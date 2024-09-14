package otus.homework.customview.data.repository

import androidx.annotation.RawRes
import otus.homework.customview.domain.model.Cost
import otus.homework.customview.data.mapper.CostMapper
import javax.inject.Inject

class CostRepository @Inject constructor(
    private val mapper: CostMapper,
) {
    fun getCostList(@RawRes payloads: Int): List<Cost> {
        return mapper.map(payloads)
    }
}