package si.uni_lj.fri.pbd.miniapp3.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import si.uni_lj.fri.pbd.miniapp3.R
import si.uni_lj.fri.pbd.miniapp3.adapter.RecyclerViewAdapter
import si.uni_lj.fri.pbd.miniapp3.adapter.SpinnerAdapter
import si.uni_lj.fri.pbd.miniapp3.models.Mapper
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientDTO
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientsDTO
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIngredientDTO
import si.uni_lj.fri.pbd.miniapp3.ui.DetailsViewModel

class SearchFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var spinnerAdapter: SpinnerAdapter? = null
    private var spinner: Spinner? = null
    private var progressBar: ProgressBar? = null

    private lateinit var viewModel: SearchViewModel
    private lateinit var detailsViewModel: DetailsViewModel

    // Code from the 3rd lab session
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        spinner = view.findViewById(R.id.spinner)
        progressBar = view.findViewById(R.id.progressBar)
        layoutManager = LinearLayoutManager(view.context)
        return view
    }

    // Code from https://stackoverflow.com/questions/55883817/how-to-connect-viewmodel-with-repository-so-that-data-is-propagated-to-the-view
    @Suppress("DEPRECATION")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        detailsViewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        // Checks if there is a request going on and shows or hide the progress bar up to that
        viewModel.getIsLoading().observe(viewLifecycleOwner) { isLoading: Boolean ->
            if (isLoading) {
                recyclerView?.visibility = View.GONE
                progressBar?.visibility = View.VISIBLE
            } else {
                recyclerView?.visibility = View.VISIBLE
                progressBar?.visibility = View.GONE
            }
        }
        // When the ingredients are ready, reload the adapter
        viewModel.getIngredients()?.observe(viewLifecycleOwner) { ingredients: IngredientsDTO? ->
            initSpinnerAdapter(ingredients)
        }
    }

    // Initialize the spinner adapter
    private fun initSpinnerAdapter(ingredients: IngredientsDTO?) {
        // Sends the ingredients and set the spinner adapter
        spinnerAdapter = SpinnerAdapter(ingredients)
        spinner?.adapter = spinnerAdapter
        // Code from https://stackoverflow.com/questions/16581536/setonitemselectedlistener-of-spinner-does-not-call
        // Set a listener on the spinner
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            // Called when an ingredient is pressed
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                // Gets the ingredient
                val item: IngredientDTO? = adapterView.getItemAtPosition(position) as IngredientDTO?
                if (item != null) {
                    // Gets all recipes that contain that ingredient
                    viewModel.getIsLoading().postValue(true)
                    viewModel.startLoadingRecipes(item.strIngredient1!!)

                    // When the recipes are ready, reload the adapter
                    viewModel.getRecipes()?.observe(viewLifecycleOwner) { recipes: RecipesByIngredientDTO? ->
                        initAdapter(recipes)
                        viewModel.getIsLoading().postValue(false)
                    }
                }

            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }
    }

    // Initialize the adapter for the recipes
    private fun initAdapter(recipes: RecipesByIngredientDTO?) {
        adapter = RecyclerViewAdapter(
            recipes?.recipes?.map { Mapper.mapRecipeSummaryDtoToRecipeSummaryIm(it)},
            "repository")
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = layoutManager
    }

}