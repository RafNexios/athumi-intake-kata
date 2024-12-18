package be.athumi

import be.athumi.WineShopUtils.DEFAULT_PRICE_DECREASE
import be.athumi.WineShopUtils.DEFAULT_PRICE_INCREASE
import be.athumi.WineShopUtils.EVENT_EXPIRATION_FIRST_BREAKPOINT
import be.athumi.WineShopUtils.EVENT_EXPIRATION_SECOND_BREAKPOINT
import be.athumi.WineShopUtils.EVENT_PRICE_INCREASE_FIRST_BREAKPOINT
import be.athumi.WineShopUtils.EVENT_PRICE_INCREASE_SECOND_BREAKPOINT
import be.athumi.WineShopUtils.decreasePrice
import be.athumi.WineShopUtils.decrementExpiresInYears
import be.athumi.WineShopUtils.increasePrice
import be.athumi.WineShopUtils.isWineExpired
import be.athumi.WineShopUtils.setToMinimumPrice

enum class WineTypeEnum {
    DEFAULT {
        override fun processWine(wine: Wine) {
            decrementExpiresInYears(wine)
            decreasePrice(wine, if (isWineExpired(wine)) 2 * DEFAULT_PRICE_DECREASE else DEFAULT_PRICE_DECREASE)
        }
    }, AGING {
        override fun processWine(wine: Wine) {
            decrementExpiresInYears(wine)
            increasePrice(wine, if (isWineExpired(wine)) 2 * DEFAULT_PRICE_INCREASE else DEFAULT_PRICE_INCREASE)
        }
    }, EVENT {
        override fun processWine(wine: Wine) {
            decrementExpiresInYears(wine)
            if (wine.expiresInYears < EVENT_EXPIRATION_SECOND_BREAKPOINT) {
                increasePrice(wine, EVENT_PRICE_INCREASE_SECOND_BREAKPOINT)
            } else if (wine.expiresInYears < EVENT_EXPIRATION_FIRST_BREAKPOINT) {
                increasePrice(wine, EVENT_PRICE_INCREASE_FIRST_BREAKPOINT)
            } else {
                increasePrice(wine, DEFAULT_PRICE_INCREASE)
            }

            if (isWineExpired(wine)) setToMinimumPrice(wine)
        }
    }, SPECIAL {
        override fun processWine(wine: Wine) {
            // Nothing needs to happen with special wines yet
            return
        }
    }, ECO {
        override fun processWine(wine: Wine) {
            decrementExpiresInYears(wine)
            decreasePrice(wine, if (isWineExpired(wine)) 2 * 2 * DEFAULT_PRICE_DECREASE else 2 * DEFAULT_PRICE_DECREASE)
        }
    };

    abstract fun processWine(wine: Wine)

    companion object{
        fun getWineType(wine: Wine): WineTypeEnum {
            return if (wine.name == "Wine brewed by Alexander the Great") {
                SPECIAL
            } else if (wine.name == "Bourdeaux Conservato" || wine.name == "Bourgogne Conservato") {
                AGING
            } else if (wine.name.contains("Conservato")) {
                AGING
            } else if (wine.name.contains("Event")) {
                EVENT
            } else if (wine.name.contains("Eco")) {
                ECO
            } else {
                DEFAULT
            }
        }
    }
}