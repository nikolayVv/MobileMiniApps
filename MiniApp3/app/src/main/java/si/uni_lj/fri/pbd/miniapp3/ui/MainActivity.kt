package si.uni_lj.fri.pbd.miniapp3.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import si.uni_lj.fri.pbd.miniapp3.R
import si.uni_lj.fri.pbd.miniapp3.adapter.SectionsPagerAdapter


class MainActivity : AppCompatActivity() {
    companion object {
        private const val NUM_OF_TABS = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set the toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Set the tabs
        configureTabLayout()
    }

    // Code from the 3rd lab session
    // Configure the tabs and the view pager
    private fun configureTabLayout() {
        val tl = findViewById<TabLayout>(R.id.tab_layout)
        val vp2 = findViewById<ViewPager2>(R.id.view_pager_2)
        val tpa = SectionsPagerAdapter(this, NUM_OF_TABS)

        vp2.adapter = tpa
        TabLayoutMediator(tl, vp2) {
                tab, position ->
            when (position) {
                0 -> tab.setText(R.string.first_fragment_label)
                1 -> tab.setText(R.string.second_fragment_label)
            }
        }.attach()
    }
}