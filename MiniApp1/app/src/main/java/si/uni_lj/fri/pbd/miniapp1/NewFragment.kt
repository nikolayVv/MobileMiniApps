package si.uni_lj.fri.pbd.miniapp1

import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.os.Bundle
import android.Manifest
import android.content.Intent
import android.provider.MediaStore
import android.widget.*

import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

import com.google.android.material.snackbar.Snackbar

import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
@Suppress("DEPRECATED_IDENTITY_EQUALS", "DEPRECATION")
class NewFragment(private var image: String? = "", private var title: String? = "", private var description: String? = "") : Fragment() {
    private var imageView: ImageView? = null
    private var titleView: EditText? = null
    private var descriptionView: EditText? = null

    companion object {
        private const val REQUEST_CODE = 1
        private const val CAMERA_REQUEST_CODE = 102
    }

    // Setting up the new memo fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View

        // Check the orientation of the device -> On creation of the fragment
        val currentOrientation = resources.configuration.orientation
        view = if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            // If it is horizontal, set the horizontal layout
            inflater.inflate(R.layout.fragment_new_horizontal, container, false)
        } else {
            // If it is vertical, set the vertical layout
            inflater.inflate(R.layout.fragment_new, container, false)
        }

        // Get the elements from the layout
        val takePhotoBtn = view.findViewById<Button>(R.id.btn_take_photo)
        val saveBtn = view.findViewById<Button>(R.id.btn_save)
        imageView = view.findViewById(R.id.image_new)
        descriptionView = view.findViewById(R.id.description_new)
        titleView = view.findViewById(R.id.title_new)

        // Set the data in the fields (image, description, title)
        // Used to save the state when rotating the screen
        if (!image.isNullOrBlank()) {
            val bb: ByteArray = Base64.getDecoder().decode(image)
            val imageBitmap: Bitmap = BitmapFactory.decodeByteArray(bb, 0, bb.size)
            imageView!!.setImageBitmap(imageBitmap)
        }
        descriptionView!!.setText(description)
        titleView!!.setText(title)

        // Take a photo for the memo (create onClick listener)
        takePhotoBtn.setOnClickListener{
            // Ask for camera permission
            val activity = (activity as MainActivity)
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // If permission is not granted ask for it
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE)
            } else {
                // If permission is granted open the camera
                openCamera()
            }
        }

        // Save the newly created memo (create onClick listener)
        saveBtn.setOnClickListener{
            // Prepare the data for the memo
            val drawable: BitmapDrawable = imageView!!.drawable as BitmapDrawable
            val bitmap: Bitmap? = drawable.bitmap
            val bos = ByteArrayOutputStream()
            bitmap!!.compress(CompressFormat.PNG, 100, bos)
            val bb: ByteArray = bos.toByteArray()
            val date = LocalDateTime.now()

            // Check if all the required fields are filled
            if (titleView!!.text.toString().isBlank() || descriptionView!!.text.toString().isBlank()) {
                // If they are empty don't save the new memo and show a message
                Snackbar.make(view, "Title and description fields are required", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            } else {
                // If they are NOT empty save the new memo
                val ra = RecyclerAdapter(activity as MainActivity)
                ra.addMemo(
                    Base64.getEncoder().encodeToString(bb),
                    titleView!!.text.toString(),
                    descriptionView!!.text.toString(),
                    date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")).toString()
                )

                // Load the list fragment
                val activity = (activity as MainActivity)
                activity.setFragment(ListFragment(), "listFragment")
                // Show a message
                Snackbar.make(view, "Memo created successfully", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }

        return view
    }

    // Check if permission is granted
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode === REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If permission is granted open the camera
                openCamera()
            } else {
                Toast.makeText(activity, "Request not granted", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    // Open the camera
    private fun openCamera() {
        val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(camera, CAMERA_REQUEST_CODE)
    }

    // Set the photo from the camera to the image view
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == CAMERA_REQUEST_CODE) {
                // Put the bitmap of the taken photo as image in the image view
                val imageBitmap: Bitmap = data!!.extras!!.get("data") as Bitmap
                imageView!!.setImageBitmap(imageBitmap)
            }
        } catch (e: Exception){
            Toast.makeText(activity, "Photo not saved", Toast.LENGTH_LONG)
                .show()
        }
    }

    // Reload the fragment when screen is rotated
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        super.onConfigurationChanged(newConfig)
        val activity = (activity as MainActivity)
        activity.setFragment(NewFragment("", titleView!!.text.toString(), descriptionView!!.text.toString()), "newFragment")
    }

}