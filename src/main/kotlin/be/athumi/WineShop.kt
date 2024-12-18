package be.athumi

class WineShop(var items: List<Wine>) {

    companion object {
        val DEFAULT_PRICE_INCREASE = 1
        val DEFAULT_PRICE_DECREASE = 1

        val MAX_PRICE = 100
        val MIN_PRICE = 0
        val EVENT_EXPIRATION_FIRST_BREAKPOINT = 8
        val EVENT_EXPIRATION_SECOND_BREAKPOINT = 3
    }


    fun next() {
        // Wine Shop logic
        for (wine in items) {
            if (wine.name != "Bourdeaux Conservato" && wine.name != "Bourgogne Conservato" && !wine.name.startsWith("Event")) {
                if (wine.price > MIN_PRICE) {
                    if (wine.name != "Wine brewed by Alexander the Great") {
                        wine.price = wine.price - DEFAULT_PRICE_DECREASE
                    }
                }
            } else {
                if (wine.price < MAX_PRICE) {
                    wine.price = wine.price + DEFAULT_PRICE_INCREASE

                    if (wine.name.startsWith("Event")) {
                        if (wine.expiresInYears < EVENT_EXPIRATION_FIRST_BREAKPOINT) {
                            if (wine.price < MAX_PRICE) {
                                wine.price = wine.price + DEFAULT_PRICE_INCREASE
                            }
                        }

                        if (wine.expiresInYears < EVENT_EXPIRATION_SECOND_BREAKPOINT) {
                            if (wine.price < MAX_PRICE) {
                                wine.price = wine.price + (2 * DEFAULT_PRICE_INCREASE)
                            }
                        }
                    }
                }
            }

            if (wine.name != "Wine brewed by Alexander the Great") {
                wine.expiresInYears = wine.expiresInYears - 1
            } else if (wine.price < MIN_PRICE) {
                wine.price = MIN_PRICE
            }

            if (wine.expiresInYears < 0) {
                if (!wine.name.contains("Conservato")) {
                    if (!wine.name.contains("Event")) {
                        if (wine.price > MIN_PRICE) {
                            if (wine.name != "Wine brewed by Alexander the Great") {
                                wine.price = wine.price - DEFAULT_PRICE_DECREASE
                            }
                        }
                    } else {
                        wine.price = 0
                    }
                } else {
                    if (wine.price < MAX_PRICE) {
                        wine.price = wine.price + DEFAULT_PRICE_INCREASE
                    }
                }
            }

            if (wine.price < MIN_PRICE) {
                wine.price = MIN_PRICE
            }
        }
    }
}