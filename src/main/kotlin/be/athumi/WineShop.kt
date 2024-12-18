package be.athumi

class WineShop(var items: List<Wine>) {

    val DEFAULT_PRICE_INCREASE = 1
    val DEFAULT_PRICE_DECREASE = 1

    val MAX_PRICE = 100
    val MIN_PRICE = 0
    val EVENT_EXPIRATION_FIRST_BREAKPOINT = 8
    val EVENT_EXPIRATION_SECOND_BREAKPOINT = 3

    fun next() {
        // Wine Shop logic
        for (i in items.indices) {
            if (items[i].name != "Bourdeaux Conservato" && items[i].name != "Bourgogne Conservato" && !items[i].name.startsWith("Event")) {
                if (items[i].price > MIN_PRICE) {
                    if (items[i].name != "Wine brewed by Alexander the Great") {
                        items[i].price = items[i].price - DEFAULT_PRICE_DECREASE
                    }
                }
            } else {
                if (items[i].price < MAX_PRICE) {
                    items[i].price = items[i].price + DEFAULT_PRICE_INCREASE

                    if (items[i].name.startsWith("Event")) {
                        if (items[i].expiresInYears < EVENT_EXPIRATION_FIRST_BREAKPOINT) {
                            if (items[i].price < MAX_PRICE) {
                                items[i].price = items[i].price + DEFAULT_PRICE_INCREASE
                            }
                        }

                        if (items[i].expiresInYears < EVENT_EXPIRATION_SECOND_BREAKPOINT) {
                            if (items[i].price < MAX_PRICE) {
                                items[i].price = items[i].price + (2 * DEFAULT_PRICE_INCREASE)
                            }
                        }
                    }
                }
            }

            if (items[i].name != "Wine brewed by Alexander the Great") {
                items[i].expiresInYears = items[i].expiresInYears - 1
            } else if (items[i].price < MIN_PRICE) {
                items[i].price = MIN_PRICE
            }

            if (items[i].expiresInYears < 0) {
                if (!items[i].name.contains("Conservato")) {
                    if (!items[i].name.contains("Event")) {
                        if (items[i].price > MIN_PRICE) {
                            if (items[i].name != "Wine brewed by Alexander the Great") {
                                items[i].price = items[i].price - DEFAULT_PRICE_DECREASE
                            }
                        }
                    } else {
                        items[i].price = 0
                    }
                } else {
                    if (items[i].price < MAX_PRICE) {
                        items[i].price = items[i].price + DEFAULT_PRICE_INCREASE
                    }
                }
            }

            if (items[i].price < MIN_PRICE) {
                items[i].price = MIN_PRICE
            }
        }
    }
}