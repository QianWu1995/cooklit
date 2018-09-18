package com.macrohard.cooklit.model.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import java.util.Calendar


import com.macrohard.cooklit.R

class AddItemActivity : AppCompatActivity() {

    private var mEditNameView: EditText? = null
    private var mDatePickerView: DatePicker? = null
    private var add_button: Button? = null


    private val i: Short = 0
    internal var replyIntent = Intent()

    internal var idcounter: Int? = 1

    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

    fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(this.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_additem)

        mEditNameView = findViewById(R.id.name)
        mDatePickerView = findViewById(R.id.date)

        mEditNameView!!.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        }


        setCurrentDateOnView()



        add_button = findViewById(R.id.add)


        add_button!!.setOnClickListener {
            if (TextUtils.isEmpty(mEditNameView!!.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val name = mEditNameView!!.text.toString()
                val year = mDatePickerView!!.year
                val month = mDatePickerView!!.month + 1
                val day = mDatePickerView!!.dayOfMonth
                val date = year.toString() + "-" + (if (month < 10) "0$month" else month) + "-" + day
                Log.d("date", date)
                replyIntent.putExtra(NAME, name)
                replyIntent.putExtra(DATE, date)
                setResult(Activity.RESULT_OK, replyIntent)
            }

            finish()
        }


    }

    // display current date
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

        internal val DATE_DIALOG_ID = 999
    }

}
