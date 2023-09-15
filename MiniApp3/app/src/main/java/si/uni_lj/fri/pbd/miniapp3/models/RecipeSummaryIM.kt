package si.uni_lj.fri.pbd.miniapp3.models

class RecipeSummaryIM(val strDrink: String, val strDrinkThumb: String, val idDrink: String) {

    override fun toString(): String {
        return "RecipeSummaryIM {" +
                "strDrink='" + strDrink + '\'' +
                ", strDrinkThumb='" + strDrinkThumb + '\'' +
                ", idDrink='" + idDrink + '\'' +
                '}'
    }
}