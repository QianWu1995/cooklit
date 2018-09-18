package com.macrohard.cooklit.support

import android.util.Log

import org.json.JSONObject

import java.io.IOException

import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

/**
 * Created by qianwu on 2018-05-21.
 */


class apiCall {

    var mJSONObject: JSONObject


    fun request(type: String) {

        val client = OkHttpClient()
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
                            /*String name = mJSONObject.getString("item_name");
                            String brandname = mJSONObject.getString("brand_name");
                            String describtion = mJSONObject.getString("item_description");

                            int calories = mJSONObject.getInt("nf_calories");
                            int calories_from_fat = mJSONObject.getInt("nf_calories_from_fat");
                            int total_fat = mJSONObject.getInt("nf_total_fat");
                            int saturated_fat = mJSONObject.getInt("nf_saturated_fat");
                            int trans_fatty_acid = mJSONObject.getInt("nf_trans_fatty_acid");
                            int cholesterol = mJSONObject.getInt("nf_cholesterol");
                            int sodium = mJSONObject.getInt("nf_sodium");
                            int total_carbohydrate = mJSONObject.getInt("nf_total_carbohydrate");
                            int dietary_fiber = mJSONObject.getInt("nf_dietary_fiber");
                            int sugars = mJSONObject.getInt("nf_sugars");
                            int protein = mJSONObject.getInt("nf_protein");
                            int vitamin_a_dv = mJSONObject.getInt("nf_vitamin_a_dv");
                            int vitamin_c_dv = mJSONObject.getInt("nf_vitamin_c_dv");
                            int calcium_dv = mJSONObject.getInt("nf_calcium_dv");
                            int iron_dv = mJSONObject.getInt("nf_iron_dv");
                            int serving_weight_grams = mJSONObject.getInt("nf_serving_weight_grams");

                            f.update(name,brandname,describtion,calories,calories_from_fat,total_fat,
                                    saturated_fat,trans_fatty_acid,cholesterol,sodium,total_carbohydrate,
                                    dietary_fiber,sugars,protein,vitamin_a_dv,vitamin_c_dv,calcium_dv,
                                    iron_dv,serving_weight_grams);*/

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
}
