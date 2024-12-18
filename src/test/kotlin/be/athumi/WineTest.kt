package be.athumi

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class WineTest {

    val defaultPriceIncrease = WineShop.DEFAULT_PRICE_INCREASE
    val defaultPriceDecrease = WineShop.DEFAULT_PRICE_DECREASE
    val minimumPrice = WineShop.MIN_PRICE
    val maximumPrice = WineShop.MAX_PRICE

    val eventWineFirstBreakpoint = WineShop.EVENT_EXPIRATION_FIRST_BREAKPOINT
    val eventWinePriceIncreaseFirstBreakpoint = WineShop.EVENT_PRICE_INCREASE_FIRST_BREAKPOINT
    val eventWineSecondBreakpoint = WineShop.EVENT_EXPIRATION_SECOND_BREAKPOINT
    val eventWinePriceIncreaseSecondBreakpoint = WineShop.EVENT_PRICE_INCREASE_SECOND_BREAKPOINT

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
        assertEquals(expirationTime - 1, shop.items[0].expiresInYears)

        shop.next()
        assertEquals(expirationTime - 2, shop.items[0].expiresInYears)
    }

    @Test
    fun `a shop should decrease price each year for a random wine that isn't expired`() {
        val startingPrice = 10
        val expirationTime = 10
        val shop = WineShop(listOf(Wine("Random Wine", startingPrice, expirationTime)))

        shop.next()
        var currentExpectedPrice = startingPrice - defaultPriceDecrease
        assertEquals(currentExpectedPrice, shop.items[0].price)

        shop.next()
        currentExpectedPrice -= defaultPriceDecrease
        assertEquals(currentExpectedPrice, shop.items[0].price)
    }

    @Test
    fun `a shop should decrease price twice as fast each year for a random wine that is expired`() {
        val startingPrice = 10
        val expirationTime = 0
        val shop = WineShop(listOf(Wine("Random Wine", startingPrice, expirationTime)))

        shop.next()
        var currentExpectedPrice = startingPrice - (2 * defaultPriceDecrease)
        assertEquals(currentExpectedPrice, shop.items[0].price)

        shop.next()
        currentExpectedPrice -= (2 * defaultPriceDecrease)
        assertEquals(currentExpectedPrice, shop.items[0].price)
    }

    @Test
    fun `price should never go below minimum price`() {
        val startingPrice = minimumPrice
        val expirationTime = 10
        val shop = WineShop(listOf(Wine("Random Wine", startingPrice, expirationTime)))

        shop.next()
        assertEquals(minimumPrice, shop.items[0].price)

        shop.next()
        assertEquals(minimumPrice, shop.items[0].price)
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
            assertEquals(startingPrice + defaultPriceIncrease, wine.price);
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
        assertEquals(startingPrice + defaultPriceIncrease, agingWine.price);

        val expiredAgingWine: Wine = shop.items[1];
        assertEquals("Bourgogne Conservato", expiredAgingWine.name);
        assertEquals(startingPrice + (2 * defaultPriceIncrease), expiredAgingWine.price);
    }

    @Test
    fun `wines cannot increase in price higher than maximum price`() {
        val startingPrice = maximumPrice - defaultPriceDecrease
        val expirationTime = 10
        val shop = WineShop(listOf(Wine("Bourgogne Conservato", startingPrice, expirationTime)))

        shop.next()
        assertEquals(maximumPrice, shop.items[0].price);

        shop.next()
        assertEquals(maximumPrice, shop.items[0].price);

        shop.next()
        assertEquals(maximumPrice, shop.items[0].price);
    }

    @Test
    fun `aging wines more expensive than maximum price should remain in price each passing year`() {
        val startingPrice = maximumPrice + 50
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
        var expirationTime = eventWineFirstBreakpoint + 1
        var shop = WineShop(listOf(Wine("Event wine", startingPrice, expirationTime)))

        shop.next()
        assertEquals("Event wine", shop.items[0].name);
        var currentExpectedPrice = startingPrice + defaultPriceIncrease
        assertEquals(currentExpectedPrice, shop.items[0].price)
        assertEquals(expirationTime - 1, shop.items[0].expiresInYears);

        shop.next()
        assertEquals("Event wine", shop.items[0].name);
        assertEquals(currentExpectedPrice + eventWinePriceIncreaseFirstBreakpoint, shop.items[0].price)
        assertEquals(expirationTime - 2, shop.items[0].expiresInYears);

        
        startingPrice = 10
        expirationTime = eventWineSecondBreakpoint + 1
        shop = WineShop(listOf(Wine("Event wine", startingPrice, expirationTime)))

        shop.next()
        assertEquals("Event wine", shop.items[0].name);
        currentExpectedPrice = startingPrice + eventWinePriceIncreaseFirstBreakpoint
        assertEquals(currentExpectedPrice, shop.items[0].price)
        assertEquals(expirationTime - 1, shop.items[0].expiresInYears);

        shop.next()
        assertEquals("Event wine", shop.items[0].name);
        assertEquals(currentExpectedPrice + eventWinePriceIncreaseSecondBreakpoint, shop.items[0].price)
        assertEquals(expirationTime - 2, shop.items[0].expiresInYears);
    }

    @Test
    fun `event wines should set price to zero when event is over`() {
        var startingPrice = 10
        var expirationTime = 0
        var shop = WineShop(listOf(Wine("Event wine", startingPrice, expirationTime)))

        shop.next()
        assertEquals("Event wine", shop.items[0].name);
        assertEquals(minimumPrice, shop.items[0].price)
    }

    @Test
    fun `testing inventory printouts`() {

        // Initial inventory printout 3 years ago
        val shop = WineShop(listOf(Wine("Standard Wine", 20, 10),
            Wine("Bourdeaux Conservato", 0, 2),
            Wine("Another Standard Wine", 7, 5),
            Wine("Wine brewed by Alexander the Great", 150, 0),
            Wine("Wine brewed by Alexander the Great", 80, 10),
            Wine("Event Wine", 20, 15),
            Wine("Event Wine", 49, 10),
            Wine("Event Wine", 49, 5),
            Wine("Eco Brilliant Wine", 6, 3)))

        // Simulating the passing 3 years
        shop.next()
        shop.next()
        shop.next()

        // Checking that everything is as the inventory printout suggests
        verifyOutput(shop.items[0].name, "Standard Wine", shop.items[0].price, 17, shop.items[0].expiresInYears, 7)
        verifyOutput(shop.items[1].name, "Bourdeaux Conservato", shop.items[1].price, 4, shop.items[1].expiresInYears, -1)
        verifyOutput(shop.items[2].name, "Another Standard Wine", shop.items[2].price, 4, shop.items[2].expiresInYears, 2)
        verifyOutput(shop.items[3].name, "Wine brewed by Alexander the Great", shop.items[3].price, 150, shop.items[3].expiresInYears, 0)
        verifyOutput(shop.items[4].name, "Wine brewed by Alexander the Great", shop.items[4].price, 80, shop.items[4].expiresInYears, 10)
        verifyOutput(shop.items[5].name, "Event Wine", shop.items[5].price, 23, shop.items[5].expiresInYears, 12)
        verifyOutput(shop.items[6].name, "Event Wine", shop.items[6].price, 52, shop.items[6].expiresInYears, 7)
        verifyOutput(shop.items[7].name, "Event Wine", shop.items[7].price, 55, shop.items[7].expiresInYears, 2)
        verifyOutput(shop.items[8].name, "Eco Brilliant Wine", shop.items[8].price, 3, shop.items[8].expiresInYears, 0)
    }

    private fun verifyOutput(
        wineName: String,
        expectedName: String,
        winePrice: Int,
        expectedPrice: Int,
        wineExpirationDate: Int,
        expectedExpirationDate: Int
    ) {
        assertEquals(wineName, expectedName)
        assertEquals(winePrice, expectedPrice)
        assertEquals(wineExpirationDate, expectedExpirationDate)
    }


}