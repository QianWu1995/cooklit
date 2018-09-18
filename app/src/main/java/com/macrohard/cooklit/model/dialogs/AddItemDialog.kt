package com.macrohard.cooklit.model.dialogs

import android.app.Activity
import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast

import com.macrohard.cooklit.R
import com.macrohard.cooklit.database.model.Ingredient
import com.macrohard.cooklit.database.model.IngredientViewModel
import com.macrohard.cooklit.database.model.Recipe
import com.macrohard.cooklit.database.model.RecipeViewModel

import java.util.Calendar

class AddItemDialog(internal var a: Activity, theme_res_id: Int) : Dialog(a, theme_res_id) {

    private var mEditNameView: EditText? = null
    private var mDatePickerView: DatePicker? = null

    private val i: Short = 0
    internal var replyIntent = Intent()

    internal var idcounter: Int? = 1

    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

    fun hideKeyboard(view: View) {
        val inputMethodManager = a.getSystemService(a.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_additem)

        mEditNameView = findViewById(R.id.name)
        mDatePickerView = findViewById(R.id.date)

        mEditNameView!!.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        }

        setCurrentDateOnView()


        val add_button = findViewById<Button>(R.id.add)


        add_button.setOnClickListener {
            if (TextUtils.isEmpty(mEditNameView!!.text)) {
                a.setResult(a.RESULT_CANCELED, replyIntent)
            } else {
                val name = mEditNameView!!.text.toString()
                val year = mDatePickerView!!.year
                val month = mDatePickerView!!.month + 1
                val day = mDatePickerView!!.dayOfMonth
                val date = year.toString() + "-" + (if (month < 10) "0$month" else month) + "-" + day
                Log.d("date", date)
                val ingredient = Ingredient(name, date)
                val ingredientViewModel = ViewModelProviders.of(this@AddItemDialog.a as FragmentActivity).get<IngredientViewModel>(IngredientViewModel::class.java!!)
                ingredientViewModel.insert(ingredient)

            }

            dismiss()
        }
    }


    fun setCurrentDateOnView() {

        mDatePickerView = findViewById<View>(R.id.date) as DatePicker

        val c = Calendar.getInstance()
        year = c.get(Calendar.YEAR)
        month = c.get(Calendar.MONTH)
        day = c.get(Calendar.DAY_OF_MONTH)


        // set current date into datepicker
        mDatePickerView!!.init(year, month, day, null)

    }

    companion object {
        val NAME = "com.macrohard.cooklit.NAME"
        val QUANTITY = "com.macrohard.cooklit.QUANTITY"
        val DATE = "com.macrohard.cooklit.DATE"
    }
}
