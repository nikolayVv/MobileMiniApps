package si.uni_lj.fri.pbd.miniapp3.models.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RecipeSummaryDTO {
    @SerializedName("strDrink")
    @Expose
    val strDrink: String? = null

    @SerializedName("strDrinkThumb")
    @Expose
    val strDrinkThumb: String? = null

    @SerializedName("idDrink")
    @Expose
    val idDrink: String? = null
}