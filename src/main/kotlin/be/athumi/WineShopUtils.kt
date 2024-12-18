package be.athumi

object WineShopUtils {

    const val DEFAULT_PRICE_INCREASE = 1
    const val DEFAULT_PRICE_DECREASE = 1

    const val MAX_PRICE = 100
    const val MIN_PRICE = 0

    const val EVENT_EXPIRATION_FIRST_BREAKPOINT = 7
    const val EVENT_PRICE_INCREASE_FIRST_BREAKPOINT = 2
    const val EVENT_EXPIRATION_SECOND_BREAKPOINT = 2
    const val EVENT_PRICE_INCREASE_SECOND_BREAKPOINT = 4

    fun increasePrice(wine: Wine, amount: Int) {
        if (wine.price < MAX_PRICE) wine.price += amount
    }

    fun decreasePrice(wine: Wine, amount: Int) {
        if (wine.price > MIN_PRICE) wine.price -= amount
    }

    fun setToMinimumPrice(wine: Wine) {
        wine.price = MIN_PRICE
    }

    fun decrementExpiresInYears(wine: Wine) {
        wine.expiresInYears -= 1
    }

    fun isWineExpired(wine: Wine): Boolean = wine.expiresInYears < 0

}