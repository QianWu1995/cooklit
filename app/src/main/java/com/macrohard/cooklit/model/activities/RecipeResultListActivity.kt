package com.macrohard.cooklit.model.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.ListView

import com.macrohard.cooklit.R
import com.macrohard.cooklit.support.adapters.RecipeListViewAdapter

import org.json.JSONObject

import java.io.IOException
import java.util.ArrayList

import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

//import android.support.design.widget.Snackbar;

class RecipeResultListActivity : AppCompatActivity() {


    var RecipeView1: ListView
    var RecipeView2: ListView? = null
    var upperURI = "https://api.edamam.com/search?q="
    var lowerURI = "&app_id=30a51b67&app_key=4fac35f9506d8806f8cda87646dca06e&from=0&to=20"
    var mJSONObject: JSONObject? = null
    var query: String
    var imageuris: ArrayList<String>
    var linkToRecipes: ArrayList<String>
    var urilinks: ArrayList<String>
    var tags: ArrayList<String>
    var ingredients: ArrayList<String>
    var mHandler: Handler
    var likeButton: ImageButton? = null
    var vsig: Boolean = false
    var asig: Boolean = false
    var psig: Boolean = false
    var lsig: Boolean = false
    var vtag: String
    var atag: String
    var ptag: String
    var ltag: String
    var asyncDialog: ProgressDialog
    var t1: Thread
    private val messageHandler = object : Handler() {

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 0) {
                Log.d("handler working", "1")
                if (imageuris.size == 0) {
                    Snackbar.make(RecipeView1, "Sorry! We do not have any recipes for your input please try again", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                }
                val recipeAdapter = RecipeListViewAdapter(this@RecipeResultListActivity, R.layout.elementview, imageuris, urilinks, tags)
                RecipeView1.adapter = recipeAdapter
                RecipeView1.itemsCanFocus = false
                RecipeView1.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
                    if (isNetworkAvailable == true) {

                        val i2 = Intent(this@RecipeResultListActivity, RecipeDetailActivity::class.java)
                        i2.putExtra("uri", linkToRecipes[i])
                        i2.putExtra("imageuri", imageuris[i])
                        i2.putExtra("title", urilinks[i])
                        i2.putExtra("ingredients", ingredients[i])
                        i2.putExtra("tags", tags[i])
                        startActivity(i2)
                    } else {
                        Snackbar.make(view, "Internet is not available, please try again", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show()

                    }
                }
            }
            asyncDialog.dismiss()

        }

    }

    private val mMessageSender = Runnable {
        request(query)
        while (mJSONObject == null) {
        }
        try {
            Log.d("mJsonObject is", mJSONObject!!.toString())
            for (i in 0 until mJSONObject!!.getJSONArray("hits").length()) {
                Log.d("imageuris", mJSONObject!!.getJSONArray("hits").getJSONObject(i).getJSONObject("recipe").getString("image"))
                imageuris.add(mJSONObject!!.getJSONArray("hits").getJSONObject(i).getJSONObject("recipe").getString("image"))
                urilinks.add(mJSONObject!!.getJSONArray("hits").getJSONObject(i).getJSONObject("recipe").getString("label"))
                linkToRecipes.add(mJSONObject!!.getJSONArray("hits").getJSONObject(i).getJSONObject("recipe").getString("url"))

                var tagstring = ""
                for (ii in 0 until mJSONObject!!.getJSONArray("hits").getJSONObject(i).getJSONObject("recipe").getJSONArray("dietLabels").length()) {

                    tagstring += "#" + (mJSONObject!!.getJSONArray("hits").getJSONObject(i).getJSONObject("recipe").getJSONArray("dietLabels").getString(ii) + " ")


                }
                tags.add(tagstring)
                var ings = ""
                for (ii in 0 until mJSONObject!!.getJSONArray("hits").getJSONObject(i).getJSONObject("recipe").getJSONArray("ingredientLines").length()) {

                    ings += mJSONObject!!.getJSONArray("hits").getJSONObject(i).getJSONObject("recipe").getJSONArray("ingredientLines").getString(ii) + "\n\n"


                }
                ingredients.add(ings)
            }


        } catch (e: Exception) {

        }

        messageHandler.sendEmptyMessage(0)
    }

    private val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vtag = ""
        atag = ""
        ptag = ""
        ltag = ""
        asyncDialog = ProgressDialog(this@RecipeResultListActivity)
        setContentView(R.layout.activity_recipe_result)
        val intent = intent
        vsig = intent.getBooleanExtra("v", false)
        asig = intent.getBooleanExtra("a", false)
        psig = intent.getBooleanExtra("p", false)
        lsig = intent.getBooleanExtra("l", false)
        if (vsig) {
            vtag = "&health=vegetarian"
        }
        if (asig) {
            atag = "&health=alcohol-free"
        }
        if (psig) {
            ptag = "&health=peanut-free"
        }
        if (lsig) {
            ltag = "&calories=0-1000"
        }

        Log.d("vsig", vsig.toString() + "")
        Log.d("asig", asig.toString() + "")
        Log.d("psig", psig.toString() + "")
        Log.d("lsig", lsig.toString() + "")

        val ings = intent.getStringArrayExtra("ingredients")
        query = ings[0]
        //Log.d("ing list are",intent.getStringArrayExtra("ingredients")[1]);
        for (i in 1 until ings.size) {
            query += "%20"
            query += ings[i]
        }
        query = upperURI + query + lowerURI + vtag + atag + ptag + ltag
        val myToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(myToolbar)
        supportActionBar!!.setTitle("Recipes")
        Log.d("query is", query)
        mHandler = Handler()
        RecipeView1 = findViewById(R.id.listView1)
        asyncDialog.setMessage("Generating your recipes...")
        asyncDialog.show()

        t1 = Thread(mMessageSender)
        t1.start()
        imageuris = ArrayList()
        urilinks = ArrayList()
        linkToRecipes = ArrayList()
        ingredients = ArrayList()
        tags = ArrayList()


    }

    fun request(type: String) {

        val client = OkHttpClient()
        Log.d("requesting", type)
        val request = Request.Builder().url(type).build()

        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.d("FAIL", "TRUE")

            }

            @Throws(IOException::class)
            override fun onResponse(call: okhttp3.Call, response: Response) {

                try {
                    val jsonData = response.body()!!.string()
                    //Response response = call.execute();
                    if (response.isSuccessful) {
                        try {
                            Log.d("jsonData is", jsonData)
                            mJSONObject = JSONObject(jsonData)
                        } catch (e: Exception) {
                            Log.d("exception caught", "1")
                        }

                    } else {
                        Log.d("jsonData is", "not successful")
                    }
                } catch (e: IOException) {
                    Log.d("exception caught", "2")
                }

            }
        })


    }

    override fun onStop() {
        super.onStop()
        if (t1.isAlive) {
            t1.interrupt()
            Log.d("thread stopped", "1")
        }
        Log.d("thread dead", "1")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (t1.isAlive) {
            t1.interrupt()
            Log.d("thread stopped", "2")
        }
        Log.d("thread dead", "2")
    }

}
