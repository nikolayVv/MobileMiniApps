package si.uni_lj.fri.pbd.miniapp3.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import si.uni_lj.fri.pbd.miniapp3.R
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientDTO
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientsDTO

class SpinnerAdapter(private val ingredients: IngredientsDTO?): BaseAdapter() {
    override fun getCount(): Int {
        return ingredients?.ingredients?.size!!
    }

    override fun getItem(position: Int): IngredientDTO? {
        return ingredients?.ingredients?.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // For each ingredient get the layout and put the value in it
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val newConvertView = LayoutInflater.from(parent?.context).inflate(R.layout.spinner_item, parent, false)
        val textView = newConvertView.findViewById<TextView>(R.id.spinner_item)

        textView.text = ingredients?.ingredients?.get(position)?.strIngredient1
        return newConvertView
    }
}