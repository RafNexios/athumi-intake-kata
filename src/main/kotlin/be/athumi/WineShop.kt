package be.athumi

class WineShop(var items: List<Wine>) {

    companion object {
        val DEFAULT_PRICE_INCREASE = 1
        val DEFAULT_PRICE_DECREASE = 1

        val MAX_PRICE = 100
        val MIN_PRICE = 0
        val EVENT_EXPIRATION_FIRST_BREAKPOINT = 7
        val EVENT_PRICE_INCREASE_FIRST_BREAKPOINT = 2
        val EVENT_EXPIRATION_SECOND_BREAKPOINT = 2
        val EVENT_PRICE_INCREASE_SECOND_BREAKPOINT = 4
    }


    fun next() {
        // Wine Shop logic
        for (wine in items) {

            when(getWineType(wine)) {
                WineTypeEnum.SPECIAL -> handleSpecialWine(wine)
                WineTypeEnum.AGING -> handleAgingWine(wine)
                WineTypeEnum.EVENT -> handleEventWine(wine)
                WineTypeEnum.DEFAULT -> handleDefaultWine(wine)
            }
            if (wine.price < MIN_PRICE) {
                setToMinimumPrice(wine)
            }
        }
    }

    private fun handleEventWine(wine: Wine) {
        wine.expiresInYears -= 1
        if (wine.expiresInYears < EVENT_EXPIRATION_SECOND_BREAKPOINT) {
            increasePrice(wine, EVENT_PRICE_INCREASE_SECOND_BREAKPOINT)
        } else if (wine.expiresInYears < EVENT_EXPIRATION_FIRST_BREAKPOINT) {
            increasePrice(wine, EVENT_PRICE_INCREASE_FIRST_BREAKPOINT)
        } else {
            increasePrice(wine, DEFAULT_PRICE_INCREASE)
        }

        if (wine.expiresInYears < 0) setToMinimumPrice(wine)
    }

    private fun handleDefaultWine(wine: Wine) {
        wine.expiresInYears -= 1
        if (wine.expiresInYears < 0) {
            decreasePrice(wine, 2 * DEFAULT_PRICE_DECREASE)
        } else {
            decreasePrice(wine, DEFAULT_PRICE_DECREASE)
        }
    }

    private fun handleAgingWine(wine: Wine) {
        wine.expiresInYears -= 1
        if (wine.expiresInYears < 0) {
            increasePrice(wine, 2 * DEFAULT_PRICE_DECREASE)
        } else {
            increasePrice(wine, DEFAULT_PRICE_DECREASE)
        }
    }

    private fun handleSpecialWine(wine: Wine) {
        // Nothing needs to happen with special wines yet
        return
    }

    private fun getWineType(wine: Wine): WineTypeEnum {
        if (wine.name == "Wine brewed by Alexander the Great") {
            return WineTypeEnum.SPECIAL
        } else if (wine.name == "Bourdeaux Conservato" || wine.name == "Bourgogne Conservato") {
            return WineTypeEnum.AGING
        } else if (wine.name.contains("Conservato")) {
            return WineTypeEnum.AGING
        } else if (wine.name.contains("Event")) {
            return WineTypeEnum.EVENT
        } else {
            return WineTypeEnum.DEFAULT
        }
    }

    private fun setToMinimumPrice(wine: Wine) {
        wine.price = MIN_PRICE
    }

    private fun increasePrice(wine: Wine, amount: Int) {
        if (wine.price < MAX_PRICE) {
            wine.price = wine.price + amount
        }
    }

    private fun decreasePrice(wine: Wine, amount: Int) {
        if (wine.price > MIN_PRICE) {
            wine.price = wine.price - amount
        }
    }
}