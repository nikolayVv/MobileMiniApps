package si.uni_lj.fri.pbd.miniapp3.ui.search


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientsDTO
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIngredientDTO
import si.uni_lj.fri.pbd.miniapp3.repository.RecipeRepository

// Code from https://stackoverflow.com/questions/55883817/how-to-connect-viewmodel-with-repository-so-that-data-is-propagated-to-the-view
// On creating is calling the repository and gets the ingredients
class SearchViewModel(application: Application?): AndroidViewModel(application!!) {
    private var repository = RecipeRepository(application)
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private var ingredients: LiveData<IngredientsDTO?>? = null
    private var recipes: LiveData<RecipesByIngredientDTO?>? = null

    init {
        // Starts loading the ingredients from the API
        isLoading.value = true
        ingredients = repository.allIngredients()
    }

    // Starts to load recipes from the API
    fun startLoadingRecipes(ingredient: String) {
        isLoading.value = true
        recipes = repository.recipesByIngredient(ingredient)
    }

    // Returns the recipes from the API
    fun getRecipes(): LiveData<RecipesByIngredientDTO?>? {
        return recipes
    }

    // Returns loading status
    fun getIsLoading(): MutableLiveData<Boolean> {
        return isLoading
    }

    // Returns the ingredients from the API
    fun getIngredients(): LiveData<IngredientsDTO?>? {
        return ingredients
    }
}