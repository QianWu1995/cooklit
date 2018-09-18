package com.macrohard.cooklit.model.activities

import android.app.AlertDialog
import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.CalendarView
import android.widget.ListView

import com.macrohard.cooklit.R
import com.macrohard.cooklit.database.model.Recipe
import com.macrohard.cooklit.database.model.RecipeViewModel
import com.macrohard.cooklit.support.adapters.TwoTextItemListViewAdapter

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.LinkedList

class MealPlanActivity : AppCompatActivity() {

    private var mealPlanCalender: CalendarView? = null
    private var mealsForDayList: ListView? = null
    private var mRecipeViewModel: RecipeViewModel? = null
    private var recipes: MutableList<Recipe>? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_plan)

        mealPlanCalender = findViewById(R.id.mealPlanCalendar)
        mealPlanCalender!!.firstDayOfWeek = 2
        mealsForDayList = findViewById(R.id.mealsForDay)

        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel::class.java!!)

        getScheduleInfo(Calendar.getInstance().get(Calendar.DAY_OF_WEEK), 0, 0, 0)

        mealPlanCalender!!.setOnDateChangeListener { calendarView, i, i1, i2 ->
            val dayOfWeek = getDayOfWeek(i, i1, i2)
            getScheduleInfo(dayOfWeek, i2, i1, i)
        }

        mealsForDayList!!.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val recipeIntent = Intent(this@MealPlanActivity, RecipeActivity::class.java)
            recipeIntent.putExtra("uri", recipes!![i].uri)
            startActivity(recipeIntent)
        }

        mealsForDayList!!.onItemLongClickListener = AdapterView.OnItemLongClickListener { adapterView, view, i, l ->
            val builder = AlertDialog.Builder(this@MealPlanActivity)
            builder.setTitle("Possible Actions")
                    .setItems(R.array.mealPlan_extraOptions) { dialog, which ->
                        if (which == 0) {
                            val recipeIntent = Intent(this@MealPlanActivity, RecipeActivity::class.java)
                            recipeIntent.putExtra("uri", recipes!![i].uri)
                            startActivity(recipeIntent)
                        } else {
                            mRecipeViewModel!!.deleteRecipe(recipes!![i])
                            recipes!!.removeAt(i)
                            val itemsAdapter = TwoTextItemListViewAdapter(this@MealPlanActivity,
                                    R.layout.mealplan_schedule_view, recipes)
                            mealsForDayList!!.adapter = itemsAdapter
                        }
                    }
            builder.create().show()
            true
        }

    }

    private fun getScheduleInfo(dayOfWeek: Int, date: Int, month: Int, year: Int) {
        if (recipes != null) {
            recipes!!.clear()
        }
        when (dayOfWeek) {
            1 -> recipes = mRecipeViewModel!!.getRecipesByDay("Su")
            2 -> recipes = mRecipeViewModel!!.getRecipesByDay("M")
            3 -> recipes = mRecipeViewModel!!.getRecipesByDay("T")
            4 -> recipes = mRecipeViewModel!!.getRecipesByDay("W")
            5 -> recipes = mRecipeViewModel!!.getRecipesByDay("Th")
            6 -> recipes = mRecipeViewModel!!.getRecipesByDay("F")
            7 -> recipes = mRecipeViewModel!!.getRecipesByDay("S")
        }

        val finalRecipes = ArrayList<Recipe>()

        if (recipes != null) {
            finalRecipes.addAll(recipes!!)
            for (recipe in recipes!!) {
                var selectedDate = Date(mealPlanCalender!!.date)
                val formatter = SimpleDateFormat("dd/MM/yyyy")
                if (year != 0) {
                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, date)
                    selectedDate = calendar.time
                }
                val selectedDateString = formatter.format(selectedDate)
                try {
                    selectedDate = formatter.parse(selectedDateString)
                } catch (e: ParseException) {
                    System.err.println("Could not parse date in MealPlan")
                }

                if (recipe.formattedDate!!.after(selectedDate) || !recipe.repeat && recipe.formattedDate != selectedDate) {
                    finalRecipes.remove(recipe)
                }
            }
        }

        val itemsAdapter = TwoTextItemListViewAdapter(this@MealPlanActivity,
                R.layout.mealplan_schedule_view, finalRecipes)
        recipes!!.clear()
        recipes!!.addAll(finalRecipes)
        mealsForDayList!!.adapter = itemsAdapter
    }


    private fun getDayOfWeek(year: Int, month: Int, day: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

}
