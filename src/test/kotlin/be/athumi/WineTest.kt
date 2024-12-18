package be.athumi

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class WineTest {

    @Test
    fun `tasting or testing wine`() {
        assertThat(Wine("name", 0, 0)).isNotNull
    }

    @Test
    fun `a shop without wine is no shop, is it`() {
        val shop = WineShop(listOf(Wine("name", 0, 0)))

        assertThat(shop).isNotNull

        shop.next()

        assertThat(shop).isNotNull
    }

    @Test
    fun `a shop should decrease expiration time each year`() {
        val startingPrice = 10
        val expirationTime = 10
        val shop = WineShop(listOf(Wine("Random Wine", startingPrice, expirationTime)))

        shop.next()
        assertEquals(expirationTime - 1, shop.items[0].expiresInYears);
        shop.next()
        assertEquals(expirationTime - 2, shop.items[0].expiresInYears);
    }

    @Test
    fun `a shop should decrease price each year for a random wine that isn't expired`() {
        val startingPrice = 10
        val expirationTime = 10
        val shop = WineShop(listOf(Wine("Random Wine", startingPrice, expirationTime)))
        shop.next()
        assertEquals(startingPrice - 1, shop.items[0].price);
        shop.next()
        assertEquals(startingPrice - 2, shop.items[0].price);
    }

    @Test
    fun `a shop should decrease price twice as fast each year for a random wine that is expired`() {
        val startingPrice = 10
        val expirationTime = 0
        val shop = WineShop(listOf(Wine("Random Wine", startingPrice, expirationTime)))
        shop.next()
        assertEquals(startingPrice - 2, shop.items[0].price);
        shop.next()
        assertEquals(startingPrice - 4, shop.items[0].price);
    }

    @Test
    fun `price should never go below zero`() {
        val startingPrice = 0
        val expirationTime = 10
        val shop = WineShop(listOf(Wine("Random Wine", startingPrice, expirationTime)))
        shop.next()
        assertEquals(startingPrice, shop.items[0].price);
        shop.next()
        assertEquals(startingPrice, shop.items[0].price);
    }

    @Test
    fun `aging wines should increase in price`() {
        val startingPrice = 10
        val expirationTime = 10
        val wineList: List<Wine> = listOf(Wine("Bourdeaux Conservato", startingPrice, expirationTime),
                                            Wine("Bourgogne Conservato", startingPrice, expirationTime))

        val shop = WineShop(wineList)
        shop.next()
        for (wine in wineList) {
            assertContains(wine.name, "Conservato");
            assertEquals(startingPrice + 1, wine.price);
        }
    }

    @Test
    fun `aging wines should increase in price twice as fast when expired`() {
        val startingPrice = 10
        val expirationTime = 10
        val wineList: List<Wine> = listOf(Wine("Bourdeaux Conservato", startingPrice, expirationTime),
            Wine("Bourgogne Conservato", startingPrice, 0))

        val shop = WineShop(wineList)
        shop.next()

        val agingWine: Wine = shop.items[0];
        assertEquals("Bourdeaux Conservato", agingWine.name);
        assertEquals(startingPrice + 1, agingWine.price);

        val expiredAgingWine: Wine = shop.items[1];
        assertEquals("Bourgogne Conservato", expiredAgingWine.name);
        assertEquals(startingPrice + 2, expiredAgingWine.price);
    }

    @Test
    fun `wines cannot increase in price higher than 100`() {
        val startingPrice = 99
        val expirationTime = 10
        val shop = WineShop(listOf(Wine("Bourgogne Conservato", startingPrice, expirationTime)))

        shop.next()
        assertEquals(startingPrice + 1, shop.items[0].price);
        shop.next()
        assertEquals(startingPrice + 1, shop.items[0].price);
        shop.next()
        assertEquals(startingPrice + 1, shop.items[0].price);
    }

    @Test
    fun `wines more expensive than 100 should remain in price each passing year`() {
        val startingPrice = 150
        val expirationTime = 10
        val shop = WineShop(listOf(Wine("Bourgogne Conservato", startingPrice, expirationTime)))

        shop.next()
        assertEquals(startingPrice, shop.items[0].price);
    }

    @Test
    fun `special wines should not change in price or expiration date`() {
        val startingPrice = 10
        val expirationTime = 10
        val shop = WineShop(listOf(Wine("Wine brewed by Alexander the Great", startingPrice, expirationTime)))
        shop.next()
        assertEquals("Wine brewed by Alexander the Great", shop.items[0].name);
        assertEquals(startingPrice, shop.items[0].price)
        assertEquals(expirationTime, shop.items[0].expiresInYears)
    }

    @Test
    fun `event wines should increase in price relative to expiration date`() {
        var startingPrice = 10
        var expirationTime = 8
        var shop = WineShop(listOf(Wine("Event wine", startingPrice, expirationTime)))

        shop.next()
        assertEquals("Event wine", shop.items[0].name);
        assertEquals(startingPrice + 1, shop.items[0].price)
        assertEquals(expirationTime - 1, shop.items[0].expiresInYears);

        shop.next()
        assertEquals("Event wine", shop.items[0].name);
        assertEquals(startingPrice + 3, shop.items[0].price)
        assertEquals(expirationTime - 2, shop.items[0].expiresInYears);

        startingPrice = 10
        expirationTime = 3
        shop = WineShop(listOf(Wine("Event wine", startingPrice, expirationTime)))

        shop.next()
        assertEquals("Event wine", shop.items[0].name);
        assertEquals(startingPrice + 2, shop.items[0].price)
        assertEquals(expirationTime - 1, shop.items[0].expiresInYears);

        shop.next()
        assertEquals("Event wine", shop.items[0].name);
        assertEquals(startingPrice + 6, shop.items[0].price)
        assertEquals(expirationTime - 2, shop.items[0].expiresInYears);
    }

    @Test
    fun `event wines should set price to zero when event is over`() {
        var startingPrice = 10
        var expirationTime = 0
        var shop = WineShop(listOf(Wine("Event wine", startingPrice, expirationTime)))

        shop.next()
        assertEquals("Event wine", shop.items[0].name);
        assertEquals(0, shop.items[0].price)
    }


}