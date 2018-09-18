package com.macrohard.cooklit.controller

import android.arch.lifecycle.LiveData
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.TextView

import com.macrohard.cooklit.R
import com.macrohard.cooklit.database.model.Ingredient

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class IngredientListAdapter(context: Context) : RecyclerView.Adapter<IngredientListAdapter.IngredientViewHolder>() {

    private val mInflater: LayoutInflater
    private var mIngredients: List<Ingredient>? = null // Cached copy of Ingredients
    internal var sdf = SimpleDateFormat("yyyy-MM-dd")
    internal var mToday = today

    val today: Date?
        get() {
            var today: Date? = null
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH) + 1
            val day = c.get(Calendar.DAY_OF_MONTH)
            val date = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day)
            Log.d("today is: ", date)
            try {
                today = sdf.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return today
        }

    init {
        mInflater = LayoutInflater.from(context)
    }


    internal inner class IngredientViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ingredientSelectButton: CheckBox
        private val ingredientNameView: TextView
        private val ingredientDateView: TextView


        init {
            ingredientSelectButton = itemView.findViewById(R.id.checkBox)
            ingredientNameView = itemView.findViewById(R.id.nameView)
            ingredientDateView = itemView.findViewById(R.id.expirydateView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false)
        return IngredientViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val current = mIngredients!![position]
        var expireDate: Date? = null
        try {
            expireDate = sdf.parse(current.expiryDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        holder.ingredientNameView.text = current.name
        holder.ingredientDateView.text = current.expiryDate

        if (expireDate!!.compareTo(mToday) < 0) {
            holder.ingredientNameView.setTextColor(Color.RED)
            holder.ingredientDateView.setTextColor(Color.RED)
        } else {
            holder.ingredientNameView.setTextColor(Color.parseColor("#808080"))
            holder.ingredientDateView.setTextColor(Color.parseColor("#808080"))
        }

        holder.ingredientSelectButton.isChecked = current.selected

        holder.ingredientSelectButton.setOnCheckedChangeListener(null)

        // if true, curent checkbox will be selected, else unselected
        holder.ingredientSelectButton.isChecked = current.selected

        holder.ingredientSelectButton.setOnCheckedChangeListener { buttonView, isChecked ->
            //set the ingreident's last status
            current.selected = isChecked
        }

    }

    fun anyChecked(): Boolean {
        for (i in mIngredients!!.indices) {
            if (mIngredients!![i].selected) {
                return true
            }
        }
        return false
    }

    fun setIngredients(ingredients: List<Ingredient>) {
        mIngredients = ingredients
        notifyDataSetChanged()
    }

    // getItemCount() is called many times, and when it is first called,
    // mIngredients has not been updated (means initially, it's null, and we can't return null).
    override fun getItemCount(): Int {
        return if (mIngredients != null)
            mIngredients!!.size
        else
            0
    }


}
