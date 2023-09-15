package si.uni_lj.fri.pbd.miniapp1

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.text.method.ScrollingMovementMethod
import android.os.Bundle
import android.net.Uri

import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment

import com.google.android.material.snackbar.Snackbar

import java.io.FileOutputStream
import java.io.File
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class DetailsFragment(memo: MemoModel) : Fragment() {
    private var title: TextView? = null
    private var description: TextView? = null
    private var image: ImageView? = null
    private var memo: MemoModel? = memo

    // Setting up the details fragment
    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View?

        // Check the orientation of the device -> On creation of the fragment
        val currentOrientation = resources.configuration.orientation
        view = if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            // If it is horizontal, set the horizontal layout
            inflater.inflate(R.layout.fragment_details_horizontal, container, false)
        } else {
            // If it is vertical, set the vertical layout
            inflater.inflate(R.layout.fragment_details, container, false)
        }

        // Get the elements from the layout
        val btnShare = view.findViewById<Button>(R.id.btn_share)
        val btnDelete = view.findViewById<Button>(R.id.btn_delete)
        val timestamp = view.findViewById<TextView>(R.id.timestamp_details)
        title = view.findViewById(R.id.title_details)
        description = view.findViewById(R.id.description_details)
        image = view.findViewById(R.id.image_details)

        // Set the data (image, description, title, timestamp of the opened memo)
        title!!.text = memo!!.title
        description!!.text = memo!!.description
        timestamp.text = memo!!.timestamp
        val bb: ByteArray = Base64.getDecoder().decode(memo!!.image)
        val imageBitmap: Bitmap = BitmapFactory.decodeByteArray(bb, 0, bb.size)
        image!!.setImageBitmap(imageBitmap)

        // Make the description field scrollable (if text is more than 5 rows)
        description!!.movementMethod = ScrollingMovementMethod()

        // Create email with the title, description and image in it (create onClick listener)
        btnShare.setOnClickListener {
            //Converting the image from Base64 string to Uri
            val iba: ByteArray = Base64.getDecoder().decode(memo!!.image)
            val ib: Bitmap = BitmapFactory.decodeByteArray(iba, 0, iba.size)
            val u: Uri = getImageToShare(ib)

            // Setting up the email
            val intent = Intent(Intent.ACTION_SEND)
            val activity = (activity as MainActivity)
            // image -> attachment
            intent.putExtra(Intent.EXTRA_STREAM, u)
            // title -> subject
            intent.putExtra(Intent.EXTRA_SUBJECT, memo!!.title)
            // description -> text
            intent.putExtra(Intent.EXTRA_TEXT, memo!!.description + "\n\n" + memo!!.timestamp)
            intent.type = "image/png"

            //Giving permission to access the Uri (image)
            val chooser: Intent = Intent.createChooser(intent, "Select email")
            val resInfoList: List<ResolveInfo> = activity.packageManager.queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                activity.grantUriPermission(
                    packageName,
                    u,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            startActivity(chooser)
        }

        // Delete the current memo (create onClick listener)
        btnDelete.setOnClickListener {
            val activity = (activity as MainActivity)
            val ra = RecyclerAdapter(activity)
            // Delete the memo from the shared preferences
            ra.deleteMemo(memo)
            // Load the list fragment => reload data from the shared preferences
            activity.setFragment(ListFragment(), "listFragment")
            // Show message
            Snackbar.make(view, "Memo deleted successfully", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        return view
    }

    // convert image from Bitmap to Uri
    private fun getImageToShare(imageBitmap: Bitmap): Uri {
        val activity = (activity as MainActivity)
        val imagefolder = File(activity.cacheDir, "images")
        var uri: Uri? = null
        try {
            // Creates the folder images, where the image will be saved
            imagefolder.mkdirs()
            // Creates the image png file (compress bitmap and put it in the file)
            val file = File(imagefolder, "shared_image.png")
            val outputStream = FileOutputStream(file)
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
            outputStream.flush()
            outputStream.close()
            // Getting the saved file from the defined fileprovider in res/xml/paths.xml
            // and in AndroidManifest.xml (folder images) + the file name (file shared_image.png) as Uri
            // Uri will look like that: si.uni_lj.fri.pbd.miniapp1.fileprovider/images/shared_image.png
            uri = FileProvider.getUriForFile(activity, "si.uni_lj.fri.pbd.miniapp1.fileprovider", file)
        } catch (e: java.lang.Exception) {
            Toast.makeText(activity, "" + e.message, Toast.LENGTH_LONG).show()
        }
        return uri!!
    }

    // Reload the fragment when screen is rotated
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val activity = (activity as MainActivity)
        activity.setFragment(DetailsFragment(memo!!), "detailsFragment")
    }
}
