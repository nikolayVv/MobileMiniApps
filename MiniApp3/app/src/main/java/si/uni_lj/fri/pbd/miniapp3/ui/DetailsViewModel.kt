package si.uni_lj.fri.pbd.miniapp3.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIdDTO
import si.uni_lj.fri.pbd.miniapp3.repository.RecipeRepository

class DetailsViewModel(application: Application?): AndroidViewModel(application!!)  {
    private var repository = RecipeRepository(application)
    private var recipe: LiveData<RecipesByIdDTO?>? = null

    // Starts getting the recipe from the API
    fun startLoadingRecipeFromRepository(idDrink: String) {
        recipe = repository.recipesById(idDrink)
    }

    // Insert the recipe into the database
    fun makeFavorite(recipe: RecipeDetails) {
        repository.insertRecipeInDb(recipe)
    }

    // Deletes the recipe from the database
    fun makeUnfavorite(idDrink: String) {
        repository.deleteRecipeFromDb(idDrink)
    }

    // Returns the recipe from the database
    fun getRecipeDatabase(idDrink: String): LiveData<RecipeDetails> {
        repository.getRecipeFromDbById(idDrink)
        return repository.favoriteRecipe
    }

    // Returns the recipe from the API
    fun getRecipeRepository(): LiveData<RecipesByIdDTO?>? {
        return recipe
    }
}