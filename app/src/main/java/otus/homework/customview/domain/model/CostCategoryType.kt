package otus.homework.customview.domain.model

enum class CostCategoryType(val value: String) {
    PRODUCTS("Продукты"),
    HEALTH("Здоровье"),
    CAFE("Кафе и рестораны"),
    ALCOHOL("Алкоголь"),
    DELIVERY("Доставка еды"),
    TRANSPORT("Транспорт"),
    SPORT("Спорт"),
    OTHER("Прочее");

    companion object {
        fun from(value: String): CostCategoryType {
            if (value.isEmpty()) return OTHER
            return values().find { it.value.equals(value, ignoreCase = true) } ?: OTHER
        }
    }
}