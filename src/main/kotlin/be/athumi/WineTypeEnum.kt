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
    DEFAULT{
        override fun processWine(wine: Wine) {
            decrementExpiresInYears(wine)
            if (isWineExpired(wine)) {
                decreasePrice(wine, 2 * DEFAULT_PRICE_DECREASE)
            } else {
                decreasePrice(wine, DEFAULT_PRICE_DECREASE)
            }
        }
    }, AGING {
        override fun processWine(wine: Wine) {
            decrementExpiresInYears(wine)
            if (isWineExpired(wine)) {
                increasePrice(wine, 2 * DEFAULT_PRICE_DECREASE)
            } else {
                increasePrice(wine, DEFAULT_PRICE_DECREASE)
            }
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
    };

    abstract fun processWine(wine: Wine)
}