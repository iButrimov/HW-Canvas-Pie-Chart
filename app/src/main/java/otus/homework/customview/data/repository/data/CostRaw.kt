package otus.homework.customview.data.repository.data

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

data class CostRaw(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("category")
    val category: String,
    @SerializedName("time")
    val time: Long
) {
    fun getDayOfMonth(): Int {
        val date = Date(time * 1000L)
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar[Calendar.DAY_OF_MONTH]
    }

    fun getDate(millis: Long, dateFormat: String): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        return SimpleDateFormat(dateFormat).format(calendar.time)
    }
}
