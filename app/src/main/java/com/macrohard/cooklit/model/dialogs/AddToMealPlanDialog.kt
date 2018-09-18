package com.macrohard.cooklit.model.dialogs

import android.app.Activity
import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TimePicker
import android.widget.Toast

import com.macrohard.cooklit.R
import com.macrohard.cooklit.database.model.Recipe
import com.macrohard.cooklit.database.model.RecipeViewModel
import com.macrohard.cooklit.model.activities.MealPlanActivity

import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.HashMap

class AddToMealPlanDialog(private val activity: Activity, theme_res_id: Int, private val currentRecipe: Recipe) : Dialog(activity, theme_res_id) {

    private var confirmAddToMealPlanButton: Button? = null
    private var cancelAddToMealPlanButton: Button? = null

    private var mealPlanCheckbox: CheckBox? = null

    private var weekDayButtons: MutableMap<Button, Boolean>? = null

    private var timePicker: TimePicker? = null

    private var remindOnceRadioButton: RadioButton? = null
    private var remindEverytimeRadioButton: RadioButton? = null

    private val results: List<Recipe>
        get() {
            val savedDates = ArrayList<Recipe>()
            val time = formattedTime
            val remindRepeat = remindEverytimeRadioButton!!.isChecked
            for (weekday in weekDayButtons!!.keys) {
                if (weekDayButtons!![weekday]) {
                    val recipe = Recipe(0, currentRecipe.name, currentRecipe.uri)
                    recipe.day = weekday.text as String
                    recipe.time = time
                    recipe.repeat = remindRepeat
                    if (remindRepeat) {
                        recipe.formattedDate = Date()
                    } else {
                        recipe.formattedDate = getNextWeekDayFromDate(recipe.day)
                    }
                    savedDates.add(recipe)
                }
            }

            return savedDates
        }

    private val formattedTime: String
        get() {
            var hour = timePicker!!.currentHour.toString()
            var minute = timePicker!!.currentMinute.toString()
            if (hour.length == 1) {
                hour = "0$hour"
            }
            if (minute.length == 1) {
                minute = "0$minute"
            }
            return "$hour:$minute"
        }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_add_to_meal_plan)

        confirmAddToMealPlanButton = this.findViewById<View>(R.id.confirmAddToMealPlan_button) as Button
        cancelAddToMealPlanButton = this.findViewById<View>(R.id.cancelAddToMealPlan_button) as Button

        remindOnceRadioButton = this.findViewById(R.id.reminder_once)
        remindEverytimeRadioButton = this.findViewById(R.id.reminder_regular)

        mealPlanCheckbox = this.findViewById(R.id.openMealPlanCheckbox)

        timePicker = this.findViewById(R.id.mealPlanTimePicker)
        initializeWeekDayButtons()

        setOnShowListener {
            remindOnceRadioButton!!.isChecked = true
            resetWeekdayList()
        }

        confirmAddToMealPlanButton!!.setOnClickListener {
            dismiss()
            val savedRecipes = results
            saveToDatabase(savedRecipes)
            val addToMealPlanConfirmationToast = Toast.makeText(context.applicationContext, "Recipe added to your meal plan", Toast.LENGTH_SHORT)
            addToMealPlanConfirmationToast.show()
            dismiss()
            if (mealPlanCheckbox!!.isChecked) {
                val mealPlanIntent = Intent(activity, MealPlanActivity::class.java)
                activity.startActivity(mealPlanIntent)
            }
        }

        cancelAddToMealPlanButton!!.setOnClickListener { cancel() }

    }

    private fun initializeWeekDayButtons() {
        weekDayButtons = HashMap()
        val weekDaysContainer = this.findViewById<LinearLayout>(R.id.weekdays_container)
        for (i in 0 until weekDaysContainer.childCount) {
            val weekDayButton = this.findViewById<Button>(weekDaysContainer.getChildAt(i).id)
            weekDayButton.setOnClickListener {
                if (!weekDayButtons!![weekDayButton]) {
                    activateWeekDayButton(weekDayButton)
                } else {
                    deActivateWeekDayButton(weekDayButton)
                }
            }
            weekDayButtons!![weekDayButton] = false
        }
    }

    private fun activateWeekDayButton(weekDayButton: Button) {
        val whiteColorId = context.resources.getColor(R.color.white)
        val circularButtonSelectedDrawable = context.resources.getDrawable(R.drawable.default_round_button_selected)
        weekDayButton.setTextColor(whiteColorId)
        weekDayButton.background = circularButtonSelectedDrawable
        weekDayButtons!![weekDayButton] = true
    }

    private fun deActivateWeekDayButton(weekDayButton: Button) {
        val circularButtonsDrawable = context.resources.getDrawable(R.drawable.default_round_button)
        val cyanColorId = context.resources.getColor(R.color.cookLitBlue)
        weekDayButton.setTextColor(cyanColorId)
        weekDayButton.background = circularButtonsDrawable
        weekDayButtons!![weekDayButton] = false
    }

    private fun resetWeekdayList() {
        for (button in weekDayButtons!!.keys) {
            deActivateWeekDayButton(button)
        }
    }

    private fun getNextWeekDayFromDate(weekDay: String): Date {
        val now = Calendar.getInstance()
        val today = now.get(Calendar.DAY_OF_WEEK)
        var requiredDay = 0
        when (weekDay) {
            "M" -> requiredDay = Calendar.MONDAY
            "T" -> requiredDay = Calendar.TUESDAY
            "W" -> requiredDay = Calendar.WEDNESDAY
            "Th" -> requiredDay = Calendar.THURSDAY
            "F" -> requiredDay = Calendar.FRIDAY
            "S" -> requiredDay = Calendar.SATURDAY
            "Su" -> requiredDay = Calendar.SUNDAY
        }

        if (today != requiredDay) {
            val daysToAdd = 7 - today + requiredDay
            now.add(Calendar.DAY_OF_YEAR, daysToAdd)
        }

        val allocatedTime = Calendar.getInstance()
        allocatedTime.set(Calendar.HOUR, timePicker!!.currentHour)
        allocatedTime.set(Calendar.MINUTE, timePicker!!.currentMinute)


        if (today == requiredDay && now.after(allocatedTime)) {
            now.add(Calendar.DAY_OF_YEAR, 7)
        }

        return now.time
    }

    private fun saveToDatabase(recipesToBeSaved: List<Recipe>) {
        val recipeViewModel = ViewModelProviders.of(this.activity as FragmentActivity).get<RecipeViewModel>(RecipeViewModel::class.java!!)
        for (recipe in recipesToBeSaved) {
            recipeViewModel.insert(recipe)
        }
    }

}

