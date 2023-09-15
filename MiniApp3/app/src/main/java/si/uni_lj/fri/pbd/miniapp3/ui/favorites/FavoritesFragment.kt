package si.uni_lj.fri.pbd.miniapp3.ui.favorites

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import si.uni_lj.fri.pbd.miniapp3.R
import si.uni_lj.fri.pbd.miniapp3.adapter.RecyclerViewAdapter
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails
import si.uni_lj.fri.pbd.miniapp3.models.Mapper

class FavoritesFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private lateinit var viewModel: FavoritesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        layoutManager = LinearLayoutManager(view.context)
        return view
    }

    @Suppress("DEPRECATION")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)

        // Gets the recipes from the database
        viewModel.getRecipes()?.observe(viewLifecycleOwner) { recipes ->
            initAdapter(recipes)
        }
    }

    // Initialize the adapter for the recipes
    private fun initAdapter(recipes: List<RecipeDetails>) {
        adapter = RecyclerViewAdapter(
            recipes.map { Mapper.mapRecipeDetailsToRecipeSummaryIm(it)},
            "database")
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = layoutManager
    }

}