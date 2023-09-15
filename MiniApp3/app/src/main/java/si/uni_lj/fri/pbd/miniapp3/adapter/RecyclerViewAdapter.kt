package si.uni_lj.fri.pbd.miniapp3.adapter

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import si.uni_lj.fri.pbd.miniapp3.R
import si.uni_lj.fri.pbd.miniapp3.adapter.RecyclerViewAdapter.CardViewHolder;
import si.uni_lj.fri.pbd.miniapp3.models.RecipeSummaryIM
import si.uni_lj.fri.pbd.miniapp3.ui.DetailsActivity
import java.lang.Exception
import java.util.concurrent.Executors

class RecyclerViewAdapter(val recipes: List<RecipeSummaryIM>?, val caller: String): RecyclerView.Adapter<CardViewHolder?>() {

    // Card with the picture and title of the cocktail
    @Suppress("DEPRECATION")
    inner class CardViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var itemImage: ImageView? = itemView!!.findViewById(R.id.item_image)
        var itemTitle: TextView? = itemView!!.findViewById(R.id.item_title)

        init {
            // Setting up event listener
            itemView!!.setOnClickListener {
                // Setting up the DetailsActivity and sending extra string
                val intent = Intent(itemView.context, DetailsActivity::class.java)
                intent.putExtra("calledFrom", caller)
                intent.putExtra("idDrink", recipes?.get(adapterPosition)?.idDrink!!)
                itemView.context?.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_grid_item, parent, false)
        return CardViewHolder(view)
    }

    // Setting the title and the photo for each cocktail
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        // Code from https://www.geeksforgeeks.org/how-to-load-any-image-from-url-without-using-any-dependency-in-android/
        // Getting a picture from url
        var image: Bitmap?
        executor.execute{
            try {
                val `in` = java.net.URL(recipes?.get(position)?.strDrinkThumb).openStream()
                image = BitmapFactory.decodeStream(`in`)

                handler.post {
                    holder.itemImage!!.setImageBitmap(image)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        holder.itemTitle!!.text = recipes?.get(position)?.strDrink
    }

    override fun getItemCount(): Int {
        return recipes?.size!!
    }
}