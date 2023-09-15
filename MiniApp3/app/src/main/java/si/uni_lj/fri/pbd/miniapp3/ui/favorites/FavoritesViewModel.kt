package si.uni_lj.fri.pbd.miniapp3.ui.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails
import si.uni_lj.fri.pbd.miniapp3.repository.RecipeRepository

class FavoritesViewModel(application: Application?) : AndroidViewModel(application!!) {
    private var repository: RecipeRepository
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private var recipes: LiveData<List<RecipeDetails>>? = null

    init {
        // Starts loading recipes from the database
        isLoading.value = true
        repository = RecipeRepository(application)
        recipes = repository.favoriteRecipes
    }

    // Returns recipes from the Database
    fun getRecipes(): LiveData<List<RecipeDetails>>? {
        return recipes
    }
}