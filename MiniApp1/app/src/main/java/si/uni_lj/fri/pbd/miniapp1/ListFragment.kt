package si.uni_lj.fri.pbd.miniapp1

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.os.Bundle

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.Fragment

import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<*>? = null

    // Setting up the details fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        // Set the recycler adapter on the recyclerView, which is in the list fragment layout
        recyclerView = view.findViewById(R.id.recycler_view)
        layoutManager = LinearLayoutManager(view.context)
        recyclerView?.layoutManager = layoutManager
        adapter = RecyclerAdapter((activity as MainActivity))
        recyclerView?.adapter = adapter

        // Load the new memo fragment as active (create onClick listener)
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener{
            val mainActivityView = (activity as MainActivity)
            mainActivityView.setFragment(NewFragment(), "newFragment")
        }

        return view
    }
}