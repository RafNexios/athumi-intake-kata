package be.athumi

import be.athumi.WineShopUtils.setToMinimumPrice

class WineShop(var items: List<Wine>) {

    fun next() {
        // Wine Shop logic
        for (wine in items) {
            WineTypeEnum.getWineType(wine).processWine(wine);
            if (wine.price < WineShopUtils.MIN_PRICE) {
                setToMinimumPrice(wine)
            }
        }
    }
}