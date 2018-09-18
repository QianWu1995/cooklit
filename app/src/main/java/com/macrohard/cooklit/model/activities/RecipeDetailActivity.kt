package com.macrohard.cooklit.model.activities

import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

import com.macrohard.cooklit.R
import com.macrohard.cooklit.database.model.Recipe
import com.macrohard.cooklit.database.model.RecipeViewModel
import com.macrohard.cooklit.model.dialogs.AddToMealPlanDialog
import com.squareup.picasso.Picasso

import org.json.JSONObject

import java.io.IOException
import java.util.HashMap


//TODO::Needs to be given a look at

class RecipeDetailActivity : AppCompatActivity() {

    var currentRecipe: Recipe? = null
        private set

    private var title: TextView? = null
    private var ingredientsText: TextView? = null

    private var scrollView: ScrollView? = null

    private var titleImage: ImageView? = null

    private var addToMealPlanDialog: AddToMealPlanDialog? = null

    private var goToRecipeButton: Button? = null
    private var addToMealPlanButton: Button? = null

    private val mJSONObject: JSONObject? = null
    private var imageUri: String? = null
    private var ingredients: String? = null

    private var mHandler: Handler? = null

    private val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        title = findViewById(R.id.recipeTitle)
        ingredientsText = findViewById(R.id.recipeDetailTextview)

        scrollView = findViewById(R.id.scrollView)

        titleImage = findViewById(R.id.recipeImage)

        goToRecipeButton = findViewById(R.id.gotoRecipe)
        addToMealPlanButton = findViewById(R.id.addToMealPlanButton)

        mHandler = Handler()
        val mIntent = intent

        currentRecipe = Recipe(0, mIntent.getStringExtra("title"), mIntent.getStringExtra("uri"))
        val mRecipeViewModel = ViewModelProviders.of(this).get<RecipeViewModel>(RecipeViewModel::class.java!!)

        addToMealPlanDialog = AddToMealPlanDialog(this, R.style.default_Dialog, currentRecipe)

        imageUri = mIntent.getStringExtra("imageuri")
        ingredients = mIntent.getStringExtra("ingredients")

        ingredientsText!!.text = ingredients
        Picasso.with(this@RecipeDetailActivity).load(imageUri).into(titleImage)
        title!!.text = currentRecipe!!.name

        goToRecipeButton!!.setOnClickListener {
            val i2 = Intent(this@RecipeDetailActivity, RecipeActivity::class.java)
            i2.putExtra("uri", currentRecipe!!.uri)
            startActivity(i2)
        }

        addToMealPlanButton!!.setOnClickListener { addToMealPlanDialog!!.show() }

    }
}
