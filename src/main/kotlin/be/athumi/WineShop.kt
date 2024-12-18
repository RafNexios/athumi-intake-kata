package be.athumi

import be.athumi.WineShopUtils.setToMinimumPrice

class WineShop(var items: List<Wine>) {

    fun next() {
        // Wine Shop logic
        for (wine in items) {
            getWineType(wine).processWine(wine);
            if (wine.price < WineShopUtils.MIN_PRICE) {
                setToMinimumPrice(wine)
            }
        }
    }

    private fun getWineType(wine: Wine): WineTypeEnum {
        return if (wine.name == "Wine brewed by Alexander the Great") {
            WineTypeEnum.SPECIAL
        } else if (wine.name == "Bourdeaux Conservato" || wine.name == "Bourgogne Conservato") {
            WineTypeEnum.AGING
        } else if (wine.name.contains("Conservato")) {
            WineTypeEnum.AGING
        } else if (wine.name.contains("Event")) {
            WineTypeEnum.EVENT
        } else {
            WineTypeEnum.DEFAULT
        }
    }
}