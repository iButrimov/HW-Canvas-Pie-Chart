package otus.homework.customview.data.mapper

import android.content.res.Resources
import androidx.annotation.RawRes
import com.google.gson.Gson
import otus.homework.customview.data.repository.data.CostRaw
import otus.homework.customview.domain.model.Cost
import otus.homework.customview.domain.model.CostCategoryType
import java.io.InputStreamReader
import javax.inject.Inject

class CostMapper @Inject constructor(
    private val resources: Resources
) {

    fun map(@RawRes payloads: Int): List<Cost> {
        val inputStream = resources.openRawResource(payloads)
        val reader = InputStreamReader(inputStream)
        val jsonString = reader.readText().also {
            reader.close()
        }
        val gson = Gson()
        val costRaw = gson.fromJson(jsonString, Array<CostRaw>::class.java).toList()
        return map(costRaw)
    }

    private fun map(raw: List<CostRaw>): List<Cost> {
        return raw.map {
            Cost(
                id = it.id,
                name = it.name,
                amount = it.amount,
                category = CostCategoryType.from(it.category),
                day = it.getDate(it.time, "MM-dd")
            )
        }
    }
}