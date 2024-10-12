package otus.homework.customview.domain.model

import androidx.annotation.ColorRes
import otus.homework.customview.R

enum class CostCategoryType(
    val categoryName: String,
    @ColorRes val categoryColor: Int
) {
    PRODUCTS("Продукты", R.color.category_red),
    HEALTH("Здоровье", R.color.category_orange),
    CAFE("Кафе и рестораны", R.color.category_yellow),
    ALCOHOL("Алкоголь", R.color.category_green),
    DELIVERY("Доставка еды", R.color.category_blue),
    TRANSPORT("Транспорт", R.color.category_indigo),
    SPORT("Спорт", R.color.category_purple),
    OTHER("Прочее", R.color.category_grey);

    companion object {
        fun from(value: String): CostCategoryType {
            if (value.isEmpty()) return OTHER
            return values().find { it.categoryName.equals(value, ignoreCase = true) } ?: OTHER
        }
    }
}