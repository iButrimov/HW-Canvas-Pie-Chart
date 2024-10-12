package otus.homework.customview.domain.model

data class Cost(
    val id: Long,
    val name: String,
    val amount: Int,
    val category: String,
    val time: Long
)