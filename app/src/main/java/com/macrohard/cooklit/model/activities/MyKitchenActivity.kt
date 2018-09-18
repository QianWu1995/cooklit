package com.macrohard.cooklit.model.activities

import android.app.Activity
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextPaint
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton

import android.widget.ImageView
import android.widget.Switch
import android.widget.Toast

import com.github.amlcurran.showcaseview.ShowcaseView
import com.github.amlcurran.showcaseview.targets.ActionItemTarget
import com.github.amlcurran.showcaseview.targets.ActionViewTarget
import com.github.amlcurran.showcaseview.targets.PointTarget
import com.github.amlcurran.showcaseview.targets.ViewTarget
import com.macrohard.cooklit.database.model.Ingredient
import com.macrohard.cooklit.database.model.IngredientViewModel
import com.macrohard.cooklit.R
import com.macrohard.cooklit.controller.IngredientListAdapter
import com.macrohard.cooklit.database.model.Recipe
import com.macrohard.cooklit.model.dialogs.AddItemDialog

class MyKitchenActivity : AppCompatActivity() {
    private var mIngredientViewModel: IngredientViewModel? = null
    private var ingredientList: Array<String>? = null
    private val mRecipes: LiveData<List<Recipe>>? = null
    private val recipeList: String? = null
    private var peanut: Switch? = null
    private var alcohol: Switch? = null
    private var vegetarian: Switch? = null
    private var lowCalories: Switch? = null
    private var deleteButton: Button? = null
    private var openMealPlanButton: Button? = null
    private var bucketListButton: Button? = null
    private var cooklitButton: Button? = null
    private var addItemButton: FloatingActionButton? = null

    internal var p_sig: Boolean = false
    internal var a_sig: Boolean = false
    internal var v_sig: Boolean = false
    internal var l_sig: Boolean = false

    private var recyclerView: RecyclerView? = null
    private var adapter: IngredientListAdapter? = null
    private var checkBoxHeader: CheckBox? = null

    private var cookLitImage: ImageView? = null

    private val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_kitchen)
        mapActivityItems()
        setListeners()

        val basicSharedPreference = getSharedPreferences(FirstTimeActivity.SHARED_PREFERENCE_APP_BASICS, Context.MODE_PRIVATE)
        if (!basicSharedPreference.getBoolean(FirstTimeActivity.SHARED_PREFERENCE_TUTORIAL_DONE, false)) {
            startTutorial()
            //basicSharedPreference.edit().putBoolean(FirstTimeActivity.SHARED_PREFERENCE_TUTORIAL_DONE, true);
        }

        /*Button nutritionButton = findViewById(R.id.nutrition);
        nutritionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2;
                i2 = new Intent(MyKitchenActivity.this, NutritionActivity.class);
                startActivity(i2);
            }
        });*/

    }

    fun mapActivityItems() {


        setContentView(R.layout.activity_my_kitchen)

        p_sig = false
        a_sig = false
        v_sig = false
        l_sig = false

        cookLitImage = findViewById<View>(R.id.cooklit_image) as ImageView

        addItemButton = findViewById<View>(R.id.fab) as FloatingActionButton
        deleteButton = findViewById(R.id.delete_button)

        recyclerView = findViewById(R.id.recyclerview)
        adapter = IngredientListAdapter(this)

        recyclerView!!.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        peanut = findViewById(R.id.switch_p)
        alcohol = findViewById(R.id.switch_a)
        vegetarian = findViewById(R.id.switch_v)
        lowCalories = findViewById(R.id.lc_switch)
        openMealPlanButton = findViewById<View>(R.id.openMealPlan) as Button
        bucketListButton = findViewById<View>(R.id.bucketlist_button) as Button
        cooklitButton = findViewById<View>(R.id.cooklit_button) as Button

        mIngredientViewModel = ViewModelProviders.of(this).get(IngredientViewModel::class.java!!)
        mIngredientViewModel!!.allIngredients.observe(this, Observer { ingredients -> adapter!!.setIngredients(ingredients) })

        // Checkbox Header for select all
        checkBoxHeader = findViewById(R.id.checkBox_header)

    }

    private fun setListeners() {
        lowCalories!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                l_sig = true
                Log.d("l", "true")
            } else {
                l_sig = false
                Log.d("l", "false")
            }
        }
        alcohol!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                a_sig = true
                Log.d("a", "true")
            } else {
                a_sig = false
                Log.d("a", "false")
            }
        }
        vegetarian!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                v_sig = true
                Log.d("v", "true")
            } else {
                v_sig = false
                Log.d("v", "false")
            }
        }
        peanut!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                p_sig = true
                Log.d("p", "true")
            } else {
                p_sig = false
                Log.d("p", "false")
            }
        }
        // Add Button

        addItemButton!!.setOnClickListener {
            val aid = AddItemDialog(this@MyKitchenActivity, R.style.default_Dialog)
            aid.show()
        }

        // Delete Button
        deleteButton!!.setOnClickListener {
            val ingre_size = mIngredientViewModel!!.selectedIngredients.size

            for (i in 0 until ingre_size) {
                mIngredientViewModel!!.deleteIngreident(mIngredientViewModel!!.selectedIngredients[i])

            }
        }

        openMealPlanButton!!.setOnClickListener {
            val mealPlanIntent = Intent(this@MyKitchenActivity, MealPlanActivity::class.java)
            startActivity(mealPlanIntent)
        }

        bucketListButton!!.setOnClickListener {
            val bucketListIntent = Intent(applicationContext, BucketListActivity::class.java)
            startActivityForResult(bucketListIntent, NEW_INGREDIENT_ACTIVITY_REQUEST_CODE)
        }

        cooklitButton!!.setOnClickListener { v -> letsCooklit(v) }

        cookLitImage!!.setOnClickListener { view -> letsCooklit(view) }

        checkBoxHeader!!.setOnClickListener {
            // set all check box true/false
            for (i in 0 until mIngredientViewModel!!.allIngredients.value!!.size) {
                mIngredientViewModel!!.allIngredients.value!![i].selected = checkBoxHeader!!.isChecked
            }
            //update view
            adapter!!.notifyDataSetChanged()
        }
    }

    fun letsCooklit(v: View) {
        if (isNetworkAvailable) {
            val i2: Intent
            i2 = Intent(this@MyKitchenActivity, RecipeResultListActivity::class.java)
            val ingre_size = mIngredientViewModel!!.selectedIngredients.size
            if (ingre_size == 0) {
                Snackbar.make(recyclerView!!, "Please select at least one ingredient", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            } else {
                ingredientList = arrayOfNulls(ingre_size)
                for (i in 0 until ingre_size) {
                    ingredientList[i] = mIngredientViewModel!!.selectedIngredients[i].name
                }
            }
            if (ingre_size != 0) {
                i2.putExtra("ingredients", ingredientList)
                i2.putExtra("v", v_sig)
                i2.putExtra("p", p_sig)
                i2.putExtra("a", a_sig)
                i2.putExtra("l", l_sig)
                startActivity(i2)
            }
        } else {
            Snackbar.make(recyclerView!!, "Internet is not available, please retry", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NEW_INGREDIENT_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val num = data.extras!!.size()
            print(num)
            for (i in 1..num / 2) {
                val ingredient = Ingredient(data.getStringExtra("name" + i.toString()),
                        data.getStringExtra("date" + i.toString()))
                mIngredientViewModel!!.insert(ingredient)
            }


        } else {
            Toast.makeText(
                    applicationContext,
                    R.string.empty_field,
                    Toast.LENGTH_LONG).show()
        }
    }

    private fun startTutorial() {

        //        final TextPaint bluePaint  = new TextPaint();
        //        bluePaint.setColor(Color.BLUE);
        //        bluePaint.setTextSize(70F);
        //
        //        final ShowcaseView showcaseForTutorial = new ShowcaseView.Builder(this)
        //                .setTarget(new ViewTarget( R.id.fab, this))
        //                .setStyle(R.style.tutorial_style)
        //                .setContentTitle("The Add Item Button")
        //                .setContentText("Use this button to tell us what you have in your kitchen")
        //                .setContentTitlePaint(bluePaint)
        //                .hideOnTouchOutside()
        ////                .setOnClickListener(new View.OnClickListener() {
        ////                    @Override
        ////                    public void onClick(View view) {
        ////
        ////                        new ShowcaseView.Builder(MyKitchenActivity.this)
        ////                                .setTarget(new ViewTarget( R.id.cooklit_image, MyKitchenActivity.this))
        ////                                .setStyle(R.style.tutorial_style)
        ////                                .setContentTitle("The COOKLIT Button")
        ////                                .setContentText("And we will tell you what you can cook!")
        ////                                .setContentTitlePaint(bluePaint)
        ////                                .setOnClickListener(new View.OnClickListener() {
        ////                                    @Override
        ////                                    public void onClick(View view) {
        ////
        ////                                    }
        ////                                })
        ////                                .build();
        ////
        ////                    }
        ////                })
        //                .build();


    }

    companion object {
        val NEW_INGREDIENT_ACTIVITY_REQUEST_CODE = 1
    }


}
