package si.uni_lj.fri.pbd.miniapp3.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import si.uni_lj.fri.pbd.miniapp3.R
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails
import si.uni_lj.fri.pbd.miniapp3.models.Mapper
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIdDTO
import java.lang.Exception
import java.util.concurrent.Executors

class DetailsActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var titleTextViewl: TextView
    private lateinit var btn_fav: Button
    private lateinit var ingredientsTextView: TextView
    private lateinit var measuresTextView: TextView
    private lateinit var instructionsTextView: TextView
    private lateinit var viewModel: DetailsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        viewModel = ViewModelProvider(this)[DetailsViewModel::class.java]

        // Initializing the data
        val idDrink = intent.getStringExtra("idDrink")
        val calledFrom = intent.getStringExtra("calledFrom")
        imageView = findViewById(R.id.image)
        titleTextViewl = findViewById(R.id.title)
        btn_fav = findViewById(R.id.btn_fav)
        ingredientsTextView = findViewById(R.id.ingredients)
        measuresTextView = findViewById(R.id.measures)
        instructionsTextView = findViewById(R.id.instructions)


        // Checks who started the activity
        if (calledFrom.equals("repository")) {
            viewModel.startLoadingRecipeFromRepository(idDrink!!)

            // Gets the recipe from the API
            viewModel.getRecipeRepository()?.observe(this) { recipes: RecipesByIdDTO? ->
                // Checks if the recipe is also in the database to be able to set the button to the right value
                viewModel.getRecipeDatabase(recipes?.recipes?.get(0)?.idDrink!!).observe(this) { recipeOdgovor: RecipeDetails? ->
                    val recipe: RecipeDetails
                    if (recipeOdgovor === null) {
                        btn_fav.setText(R.string.favorite)
                        recipe = Mapper.mapRecipeDetailsDtoToRecipeDetails(false, recipes.recipes[0]!!)
                    } else {
                        btn_fav.setText(R.string.unfavorite)
                        recipe = Mapper.mapRecipeDetailsDtoToRecipeDetails(true, recipes.recipes[0]!!)
                    }
                    setData(recipe)
                }
            }
        } else if (calledFrom.equals("database")) {
            // Gets the recipe from the database
            viewModel.getRecipeDatabase(idDrink!!).observe(this){ recipe: RecipeDetails? ->
                setData(recipe!!)
                btn_fav.setText(R.string.unfavorite)
            }
        }
    }

    // Set the values for the chosen recipe
    private fun setData(recipe: RecipeDetails) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        // Code from https://www.geeksforgeeks.org/how-to-load-any-image-from-url-without-using-any-dependency-in-android/
        // Set image
        var image: Bitmap?
        executor.execute{
            try {
                val `in` = java.net.URL(recipe.strDrinkThumb).openStream()
                image = BitmapFactory.decodeStream(`in`)

                handler.post {
                    imageView.setImageBitmap(image)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Set title
        titleTextViewl.setText(recipe.strDrink)

        // Set ingredients
        val ingredients = arrayOf(
            recipe.strIngredient1, recipe.strIngredient2, recipe.strIngredient3,
            recipe.strIngredient4, recipe.strIngredient5, recipe.strIngredient6,
            recipe.strIngredient7, recipe.strIngredient8, recipe.strIngredient9,
            recipe.strIngredient10, recipe.strIngredient11, recipe.strIngredient12,
            recipe.strIngredient13, recipe.strIngredient15, recipe.strIngredient15
        )
        val ingredientsString = ingredients.takeWhile { !it.toString().equals("null") }.joinToString { it -> it.toString() }
        ingredientsTextView.setText(ingredientsString)

        // Set measures
        val measures = arrayOf(
            recipe.strMeasure1, recipe.strMeasure2, recipe.strMeasure3,
            recipe.strMeasure4, recipe.strMeasure5, recipe.strMeasure6,
            recipe.strMeasure7, recipe.strMeasure8, recipe.strMeasure9,
            recipe.strMeasure10, recipe.strMeasure11, recipe.strMeasure12,
            recipe.strMeasure13, recipe.strMeasure15, recipe.strMeasure15
        )
        val measuresString = measures.takeWhile { !it.toString().equals("null") }.joinToString { it -> it.toString() }
        measuresTextView.setText(measuresString)

        // Set instructions
        instructionsTextView.setText(recipe.strInstructions)

        btn_fav.setOnClickListener {
            if (btn_fav.text.equals("Favorite")) {
                viewModel.makeFavorite(recipe)
                btn_fav.setText(R.string.unfavorite)
            } else {
                viewModel.makeUnfavorite(recipe.idDrink!!)
                btn_fav.setText(R.string.favorite)
            }
        }
    }
}