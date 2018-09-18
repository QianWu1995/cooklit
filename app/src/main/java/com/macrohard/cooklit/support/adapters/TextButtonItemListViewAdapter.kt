package com.macrohard.cooklit.support.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView

import com.macrohard.cooklit.R
import com.macrohard.cooklit.database.model.Recipe

import java.util.ArrayList

class TextButtonItemListViewAdapter(private val context: Context, resource: Int, private val recipes: ArrayList<String>, private val buttons: ArrayList<Button>) : ArrayAdapter<String>(context, resource, recipes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.mealplan_schedule_view, parent, false)
        }

        // Lookup view for data population


        // Populate the data into the template view using the data object
        if (position <= recipes.size - 1) {
            initializeRecipeName(convertView, position)
            initializeDeleteButton(convertView, position)

        }

        return convertView

    }


    fun initializeRecipeName(view: View, position: Int) {
        val recipeName = view.findViewById<TextView>(R.id.savedRecipeNameText)

        recipeName.setOnClickListener { }
    }

    fun initializeDeleteButton(view: View, position: Int) {
        val deleteSavedRecipeButton = view.findViewById<Button>(R.id.deleteSavedRecipeButton)
        deleteSavedRecipeButton.setOnClickListener {
            //TODO::Sean: Need the query to delete recipe by id
        }
    }


}
