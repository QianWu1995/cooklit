package com.macrohard.cooklit.model.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView

import com.macrohard.cooklit.R

import org.json.JSONArray
import org.json.JSONException

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.HashMap

class BucketListActivity : AppCompatActivity() {
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    internal var intent = Intent()
    internal var idcounter: Int? = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bucketlist)
        val itemTxt = findViewById<View>(R.id.itemToAdd) as EditText
        val btn = findViewById<View>(R.id.Add) as Button
        val btn2 = findViewById<View>(R.id.Submit) as Button
        val delete = findViewById<View>(R.id.Delete) as Button
        val checkBoxes = HashMap<String, CheckBox>()
        val items = getItems(this, "1")

        val tl = findViewById<View>(R.id.innertable) as TableLayout

        if (items.size != 0) {
            createView(tl, checkBoxes, items)
        }

        delete.setOnClickListener {
            val newItems = ArrayList<String>()
            for (name in items) {
                if (!checkBoxes[name].isChecked()) {
                    newItems.add(name)
                }
            }
            for (name in newItems) {
                if (!items.contains(name)) {
                    checkBoxes.remove(name)
                }
            }
            items.clear()
            tl.removeAllViews()
            for (name in newItems) {
                items.add(name)
            }
            createView(tl, checkBoxes, items)
            setItemsPersistence(this@BucketListActivity, "1", items)
        }

        btn.setOnClickListener {
            val name = itemTxt.text.toString()
            items.add(name)

            val tr = TableRow(this@BucketListActivity)
            tr.layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.FILL_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT)
            val lp = tr.layoutParams as RelativeLayout.LayoutParams
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)
            tr.layoutParams = lp


            val cb = CheckBox(this@BucketListActivity)
            cb.height = 120
            cb.width = 120
            checkBoxes[name] = cb

            val item = TextView(this@BucketListActivity)
            item.text = name
            item.textSize = 40f

            tr.addView(cb)
            tr.addView(item)
            tl.addView(tr)

            itemTxt.setText("")
            setItemsPersistence(this@BucketListActivity, "1", items)
        }
        btn2.setOnClickListener {
            for (name in items) {
                if (checkBoxes[name].isChecked()) {
                    val c = Calendar.getInstance()
                    year = c.get(Calendar.YEAR)
                    month = c.get(Calendar.MONTH)
                    day = c.get(Calendar.DAY_OF_MONTH)
                    val d = SimpleDateFormat("2018-07-31").format(Calendar.getInstance().time)
                    val stryear = year.toString()
                    val strmonth = month.toString()
                    val strday = day.toString()
                    val date = "$stryear-$strmonth-$strday"
                    intent.putExtra("name" + idcounter.toString(), name)
                    intent.putExtra("date" + idcounter.toString(), d)
                    idcounter += 1
                    setResult(Activity.RESULT_OK, intent)
                }
            }
            //setItemsPersistence(BucketListActivity.this, "1", items);
            delete.performClick()
            finish()
        }
    }

    fun createView(tl: TableLayout, checkBoxes: HashMap<String, CheckBox>, items: ArrayList<String>) {
        for (i in items.indices) {
            val cb = CheckBox(this@BucketListActivity)
            cb.height = 120
            cb.width = 120

            checkBoxes[items[i]] = cb
            checkBoxes[items[i]] = cb
            val tr = TableRow(this@BucketListActivity)
            tr.layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.FILL_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT)
            val lp = tr.layoutParams as RelativeLayout.LayoutParams
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)
            lp.addRule(RelativeLayout.RIGHT_OF)
            tr.layoutParams = lp

            val item = TextView(this@BucketListActivity)
            item.text = items[i]
            item.textSize = 40f

            tr.addView(cb)
            tr.addView(item)
            tl.addView(tr)
        }
    }

    companion object {

        //    @Override
        //    protected void onRestart() {
        //        super.onRestart();
        //
        //    }

        fun setItemsPersistence(context: Context, key: String, names: ArrayList<String>) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = prefs.edit()
            val arr = JSONArray()
            for (i in names.indices) {
                arr.put(names[i])
            }
            if (!names.isEmpty()) {
                editor.putString(key, arr.toString())
            } else {
                editor.putString(key, null)
            }
            editor.commit()
        }

        fun getItems(context: Context, key: String): ArrayList<String> {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val json = prefs.getString(key, null)
            val names = ArrayList<String>()
            if (json != null) {
                try {
                    val arr = JSONArray(json)
                    for (i in 0 until arr.length()) {
                        val name = arr.optString(i)
                        names.add(name)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            return names
        }
    }
}