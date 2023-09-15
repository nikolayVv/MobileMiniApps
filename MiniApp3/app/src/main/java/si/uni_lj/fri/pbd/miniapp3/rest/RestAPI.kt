package si.uni_lj.fri.pbd.miniapp3.rest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import si.uni_lj.fri.pbd.miniapp3.models.dto.*


interface RestAPI {
    @get:GET("list.php?i=list")
    val allIngredients: Call<IngredientsDTO?>?

    @GET("filter.php")
    fun recipesByIngredient(@Query("i") ingredient: String): Call<RecipesByIngredientDTO?>?

    @GET("lookup.php")
    fun recipesById(@Query("i") idDrink: String): Call<RecipesByIdDTO?>?
}