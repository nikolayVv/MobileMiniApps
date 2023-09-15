package si.uni_lj.fri.pbd.miniapp3.models.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class IngredientDTO(@field:Expose @field:SerializedName("strIngredient") val strIngredient: String) {
    @SerializedName("strIngredient1")
    @Expose
    val strIngredient1: String? = null

}