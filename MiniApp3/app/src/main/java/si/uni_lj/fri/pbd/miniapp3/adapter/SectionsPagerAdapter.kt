package si.uni_lj.fri.pbd.miniapp3.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import si.uni_lj.fri.pbd.miniapp3.ui.favorites.FavoritesFragment
import si.uni_lj.fri.pbd.miniapp3.ui.search.SearchFragment

// Code from the 3rd lab session
class SectionsPagerAdapter(fa: FragmentActivity?, private val tabCounter: Int): FragmentStateAdapter(fa!!) {
    // Get the currently chosen tab
    override fun getItemCount(): Int {
        return tabCounter
    }

    // Load the chosen fragment from the tabs
    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return SearchFragment()
            1 -> return FavoritesFragment()
        }
        return Fragment()
    }

}