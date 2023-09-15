package si.uni_lj.fri.pbd.miniapp3.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RecipeDetails {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null

    @ColumnInfo(name = "isFavorite")
    var favorite: Boolean? = null

    @ColumnInfo(name = "idDrink")
    var idDrink: String? = null

    @ColumnInfo(name = "strDrink")
    var strDrink: String? = null

    @ColumnInfo(name = "strCategory")
    var strCategory: String? = null

    @ColumnInfo(name = "strDrinkAlternate")
    var strDrinkAlternate: String? = null

    @ColumnInfo(name = "strInstructions")
    var strInstructions: String? = null

    @ColumnInfo(name = "strDrinkThumb")
    var strDrinkThumb: String? = null

    @ColumnInfo(name = "strVideo")
    var strVideo: String? = null

    @ColumnInfo(name = "strTags")
    var strTags: String? = null

    @ColumnInfo(name = "strIBA")
    var strIBA: String? = null

    @ColumnInfo(name = "strAlcoholic")
    var strAlcoholic: String? = null

    @ColumnInfo(name = "strGlass")
    var strGlass: String? = null

    @ColumnInfo(name = "strIngredient1")
    var strIngredient1: String? = null

    @ColumnInfo(name = "strIngredient2")
    var strIngredient2: String? = null

    @ColumnInfo(name = "strIngredient3")
    var strIngredient3: String? = null

    @ColumnInfo(name = "strIngredient4")
    var strIngredient4: String? = null

    @ColumnInfo(name = "strIngredient5")
    var strIngredient5: String? = null

    @ColumnInfo(name = "strIngredient6")
    var strIngredient6: String? = null

    @ColumnInfo(name = "strIngredient7")
    var strIngredient7: String? = null

    @ColumnInfo(name = "strIngredient8")
    var strIngredient8: String? = null

    @ColumnInfo(name = "strIngredient9")
    var strIngredient9: String? = null

    @ColumnInfo(name = "strIngredient10")
    var strIngredient10: String? = null

    @ColumnInfo(name = "strIngredient11")
    var strIngredient11: String? = null

    @ColumnInfo(name = "strIngredient12")
    var strIngredient12: String? = null

    @ColumnInfo(name = "strIngredient13")
    var strIngredient13: String? = null

    @ColumnInfo(name = "strIngredient14")
    var strIngredient14: String? = null

    @ColumnInfo(name = "strIngredient15")
    var strIngredient15: String? = null

    @ColumnInfo(name = "strMeasure1")
    var strMeasure1: String? = null

    @ColumnInfo(name = "strMeasure2")
    var strMeasure2: String? = null

    @ColumnInfo(name = "strMeasure3")
    var strMeasure3: String? = null

    @ColumnInfo(name = "strMeasure4")
    var strMeasure4: String? = null

    @ColumnInfo(name = "strMeasure5")
    var strMeasure5: String? = null

    @ColumnInfo(name = "strMeasure6")
    var strMeasure6: String? = null

    @ColumnInfo(name = "strMeasure7")
    var strMeasure7: String? = null

    @ColumnInfo(name = "strMeasure8")
    var strMeasure8: String? = null

    @ColumnInfo(name = "strMeasure9")
    var strMeasure9: String? = null

    @ColumnInfo(name = "strMeasure10")
    var strMeasure10: String? = null

    @ColumnInfo(name = "strMeasure11")
    var strMeasure11: String? = null

    @ColumnInfo(name = "strMeasure12")
    var strMeasure12: String? = null

    @ColumnInfo(name = "strMeasure13")
    var strMeasure13: String? = null

    @ColumnInfo(name = "strMeasure14")
    var strMeasure14: String? = null

    @ColumnInfo(name = "strMeasure15")
    var strMeasure15: String? = null

    @ColumnInfo(name = "strImageSource")
    var strImageSource: String? = null

    override fun toString(): String {
        return "RecipeDetails {" +
                "id=" + id +
                ", isFavorite=" + favorite +
                ", idDrink='" + idDrink + '\'' +
                ", strDrink='" + strDrink + '\'' +
                ", strCategory='" + strCategory + '\'' +
                ", strIBA='" + strIBA + '\'' +
                ", strInstructions='" + strInstructions + '\'' +
                ", strDrinkThumb='" + strDrinkThumb + '\'' +
                ", strVideo='" + strVideo + '\'' +
                ", strIngredient1='" + strIngredient1 + '\'' +
                ", strIngredient2='" + strIngredient2 + '\'' +
                ", strIngredient3='" + strIngredient3 + '\'' +
                ", strIngredient4='" + strIngredient4 + '\'' +
                ", strIngredient5='" + strIngredient5 + '\'' +
                ", strIngredient6='" + strIngredient6 + '\'' +
                ", strIngredient7='" + strIngredient7 + '\'' +
                ", strIngredient8='" + strIngredient8 + '\'' +
                ", strIngredient9='" + strIngredient9 + '\'' +
                ", strIngredient10='" + strIngredient10 + '\'' +
                ", strIngredient11='" + strIngredient11 + '\'' +
                ", strIngredient12='" + strIngredient12 + '\'' +
                ", strIngredient13='" + strIngredient13 + '\'' +
                ", strIngredient14='" + strIngredient14 + '\'' +
                ", strIngredient15='" + strIngredient15 + '\'' +
                ", strMeasure1='" + strMeasure1 + '\'' +
                ", strMeasure2='" + strMeasure2 + '\'' +
                ", strMeasure3='" + strMeasure3 + '\'' +
                ", strMeasure4='" + strMeasure4 + '\'' +
                ", strMeasure5='" + strMeasure5 + '\'' +
                ", strMeasure6='" + strMeasure6 + '\'' +
                ", strMeasure7='" + strMeasure7 + '\'' +
                ", strMeasure8='" + strMeasure8 + '\'' +
                ", strMeasure9='" + strMeasure9 + '\'' +
                ", strMeasure10='" + strMeasure10 + '\'' +
                ", strMeasure11='" + strMeasure11 + '\'' +
                ", strMeasure12='" + strMeasure12 + '\'' +
                ", strMeasure13='" + strMeasure13 + '\'' +
                ", strMeasure14='" + strMeasure14 + '\'' +
                ", strMeasure15='" + strMeasure15 + '\'' +
                ", strImageSource='" + strImageSource + '\'' +
                '}'
    }
}