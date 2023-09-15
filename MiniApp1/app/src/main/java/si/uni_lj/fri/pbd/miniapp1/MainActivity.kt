package si.uni_lj.fri.pbd.miniapp1

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    // Create the main activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Load the list fragment (main fragment)
        setFragment(ListFragment(), "listFragment")
    }

    // Loading/Replacing the fragment on the Main Activity
    fun setFragment(fragment: Fragment, tag: String) {
        val tr = supportFragmentManager.beginTransaction()
        tr.replace(R.id.fragment_container, fragment, tag)
        tr.commit()
    }

    // Replace the current fragment on back button pressed
    override fun onBackPressed() {
        // Get the active fragment
        val currFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.fragment_container)
        // Check the tag of the current fragment
        if (currFragment!!.tag.toString() === "listFragment") {
            // If the current fragment is the main fragment (listFragment), close the application
            moveTaskToBack(false)
        } else {
            // If the current fragment is NOT the main fragment (listFragment),
            // load the main fragment (go back to the listFragment)
            setFragment(ListFragment(), "listFragment")
        }
    }



}