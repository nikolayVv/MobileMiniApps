package si.uni_lj.fri.pbd.miniapp3.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import retrofit2.Call
import androidx.lifecycle.MutableLiveData
import retrofit2.Callback
import retrofit2.Response
import si.uni_lj.fri.pbd.miniapp3.database.Database
import si.uni_lj.fri.pbd.miniapp3.database.dao.RecipeDao
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientsDTO
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIdDTO
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIngredientDTO
import si.uni_lj.fri.pbd.miniapp3.rest.RestAPI
import si.uni_lj.fri.pbd.miniapp3.rest.ServiceGenerator.createService

// Code from https://stackoverflow.com/questions/55883817/how-to-connect-viewmodel-with-repository-so-that-data-is-propagated-to-the-view
class RecipeRepository(application: Application?) {
    private var api: RestAPI = createService(RestAPI::class.java)
    private val recipeDao: RecipeDao?

    private var ingredients: MutableLiveData<IngredientsDTO> = MutableLiveData()
    private var recipes: MutableLiveData<RecipesByIngredientDTO> = MutableLiveData()
    private var recipe: MutableLiveData<RecipesByIdDTO> = MutableLiveData()
    var favoriteRecipes: LiveData<List<RecipeDetails>>?
    var favoriteRecipe: MutableLiveData<RecipeDetails> = MutableLiveData()

    init {
        // Setting up the background db
        val db: Database? = application?.let {
            Database.getDatabase(it.applicationContext)
        }
        recipeDao = db?.recipeDao()
        favoriteRecipes = recipeDao?.allRecipes()
    }

    // Getting all ingredients from the API
    fun allIngredients(): LiveData<IngredientsDTO?>? {
        api.allIngredients?.enqueue(object: Callback<IngredientsDTO?> {
            override fun onResponse(call: Call<IngredientsDTO?>, response: Response<IngredientsDTO?>) {
                ingredients.value = response.body()
            }

            override fun onFailure(call: Call<IngredientsDTO?>, t: Throwable) {
                Log.d("RecipeRepository", "onFailure: failed to ingredients list from server");
            }
        })

        return this.ingredients
    }

    // Getting all recipes for a chosen ingredient from the API
    fun recipesByIngredient(ingredient: String): LiveData<RecipesByIngredientDTO?> {
        api.recipesByIngredient(ingredient)?.enqueue(object: Callback<RecipesByIngredientDTO?> {
            override fun onResponse(call: Call<RecipesByIngredientDTO?>, response: Response<RecipesByIngredientDTO?>) {
                recipes.value = response.body()
            }

            override fun onFailure(call: Call<RecipesByIngredientDTO?>, t: Throwable) {
                Log.d("RecipeRepository", "onFailure: failed to fetch recipes from server");
            }
        })

        return this.recipes
    }

    // Getting a recipe for a chosen ID from the API
    fun recipesById(idDrink: String): LiveData<RecipesByIdDTO?> {
        api.recipesById(idDrink)?.enqueue(object: Callback<RecipesByIdDTO?> {
            override fun onResponse(call: Call<RecipesByIdDTO?>, response: Response<RecipesByIdDTO?>) {
                recipe.value = response.body()
            }

            override fun onFailure(call: Call<RecipesByIdDTO?>, t: Throwable) {
                Log.d("RecipeRepository", "onFailure: failed to fetch recipe from server");
            }
        })

        return this.recipe
    }

    // Inserting Recipe to the database
    fun insertRecipeInDb(recipe: RecipeDetails) {
        Database.databaseWriteExecutor.execute {
            recipeDao?.insertRecipe(recipe)
        }
    }

    // Deleting Recipe from the database
    fun deleteRecipeFromDb(idDrink: String) {
        Database.databaseWriteExecutor.execute {
            recipeDao?.deleteRecipe(idDrink)
        }
    }

    // Getting a Recipe from the database
    fun getRecipeFromDbById(idDrink: String) {
        Database.databaseWriteExecutor.execute {
            favoriteRecipe.postValue(recipeDao?.getRecipeById(idDrink))
        }
    }


}