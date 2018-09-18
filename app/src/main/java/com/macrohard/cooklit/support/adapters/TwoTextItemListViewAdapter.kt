package com.macrohard.cooklit.support.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

import com.macrohard.cooklit.R
import com.macrohard.cooklit.database.model.Recipe
import com.squareup.picasso.Picasso

import java.util.ArrayList

class TwoTextItemListViewAdapter(private val context: Context, resource: Int, private val recipes: List<Recipe>) : ArrayAdapter<Recipe>(context, resource, recipes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.mealplan_schedule_view, parent, false)
        }

        // Lookup view for data population
        val text1 = convertView!!.findViewById<TextView>(R.id.viewText1)
        val text2 = convertView.findViewById<TextView>(R.id.viewText2)

        // Populate the data into the template view using the data object
        if (position <= recipes.size - 1) {
            text1.text = recipes[position].name
            text2.text = recipes[position].time
        }

        return convertView

    }

}
