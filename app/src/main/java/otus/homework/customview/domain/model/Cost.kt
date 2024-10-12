package otus.homework.customview.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cost(
    val id: Long,
    val name: String,
    val amount: Int,
    val category: String,
    val time: Long
) : Parcelable