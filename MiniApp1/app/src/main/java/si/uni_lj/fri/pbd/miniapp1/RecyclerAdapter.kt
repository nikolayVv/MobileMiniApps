package si.uni_lj.fri.pbd.miniapp1

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.content.SharedPreferences
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import si.uni_lj.fri.pbd.miniapp1.RecyclerAdapter.CardViewHolder
import com.google.gson.Gson
import java.util.*

import org.json.JSONArray
import org.json.JSONObject



class RecyclerAdapter(private val activity: MainActivity? = null) : RecyclerView.Adapter<CardViewHolder?>() {
    private var memos = loadMemos()

    // Class (object) that contains the image, text and timestamp views (each card in the list)
    inner class CardViewHolder(itemView: View?, activity: MainActivity?) : RecyclerView.ViewHolder(itemView!!) {
        var memoImage: ImageView? = itemView!!.findViewById(R.id.memo_image)
        var memoTitle: TextView? = itemView!!.findViewById(R.id.memo_title)
        var memoTimestamp: TextView? = itemView!!.findViewById(R.id.memo_timestamp)

        init {
            // Open details for the selected memo (create onClick listener)
            itemView!!.setOnClickListener{
                val mainActivityView = activity
                // adapterPosition is the index of the chosen memo (card)
                mainActivityView!!.setFragment(DetailsFragment(memos[adapterPosition]), "detailsFragment")
            }
        }
    }

    // Creates each card (memo) and returns it
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CardViewHolder{
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.recycler_item_memo_model, viewGroup, false)
        return CardViewHolder(view, activity)
    }

    // set the data for each memo from the memos array to each cardViewHolder class (image, text, timestamp view)
    // one call = setting one card (memo)
    override fun onBindViewHolder(viewHolder: CardViewHolder, i: Int) {
        val bb: ByteArray = Base64.getDecoder().decode(memos[i].image)
        val imageBitmap: Bitmap = BitmapFactory.decodeByteArray(bb, 0, bb.size)
        viewHolder.memoImage!!.setImageBitmap(imageBitmap)
        viewHolder.memoTitle!!.text = memos[i].title
        viewHolder.memoTimestamp!!.text = memos[i].timestamp
    }

    // returns the number of memos
    override fun getItemCount(): Int {
        return memos.size
    }

    // Add memo in the shared preferences
    fun addMemo(image: String, title: String, description: String, timestamp: String) {
        // Creating the new memo with the received data
        val newMemo = MemoModel(
            itemCount + 1,
            image,
            title,
            description,
            timestamp
        )
        // Adding the new memo to the already existing memos
        val list: MutableList<MemoModel> = memos.toMutableList()
        list.add(newMemo)
        memos = list.toTypedArray()

        // Save memos in the shared preferences
        saveMemos()
    }

    // Delete memo from the shared preferences
    fun deleteMemo(memo: MemoModel?) {
        // Remove the memo from the memos array
        val list: MutableList<MemoModel> = memos.toMutableList()
        list.remove(memo)
        memos = list.toTypedArray()

        // Save memos in the shared preferences
        saveMemos()
    }

    // Save memos in the shared preferences
    private fun saveMemos() {
        // Convert the memos array to json array string
        val gson = Gson()
        val jsonMemos = gson.toJson(memos)
        // Preparing the shared preference called Memos
        val preferences: SharedPreferences = activity!!.getSharedPreferences("Memos", Context.MODE_PRIVATE)
        val editor = preferences.edit()

        // Save the memos json array string in the shared preference Memos, using the tag memos
        editor.putString("memos", jsonMemos)
        editor.apply()
    }

    // Loading memos from the shared preferences
    private fun loadMemos(): Array<MemoModel> {
        // Preparing the shared preference called Memos
        val preferences: SharedPreferences = activity!!.getSharedPreferences("Memos", Context.MODE_PRIVATE)
        try {
            // Load the memos json array string in the shared preference Memos, using the tag memos
            val retrievedMemos = JSONArray(preferences.getString("memos", null))
            // Convert each memo (json object) to MemoModel object and save them in mutable list
            val memosLoaded: MutableList<MemoModel> = arrayListOf()
            for (i in 0 until retrievedMemos.length()) {
                val memo = MemoModel(
                    (retrievedMemos[i] as JSONObject).get("id").toString().toInt(),
                    (retrievedMemos[i] as JSONObject).get("image").toString(),
                    (retrievedMemos[i] as JSONObject).get("title").toString(),
                    (retrievedMemos[i] as JSONObject).get("description").toString(),
                    (retrievedMemos[i] as JSONObject).get("timestamp").toString()
                )
                memosLoaded.add(memo)
            }

            // Set the memos array to the received memos from the shared preferences
            return memosLoaded.toTypedArray()
        } catch (e: Exception) {
            // Set the memos array as empty array if there are NOT saved memos or there was an error
            return arrayOf()
        }
    }
}