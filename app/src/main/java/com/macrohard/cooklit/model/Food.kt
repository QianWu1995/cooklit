package com.macrohard.cooklit.model

import org.json.JSONException
import org.json.JSONObject

/**
 * Created by qianwu on 2018-05-21.
 */

class Food {
    private var name: String? = null
    private var brandName: String? = null
    private var itemDescription: String? = null
    private var calories: Int = 0
    private var caloriesFromFat: Int = 0
    private var totalFat: Int = 0
    private var saturatedFat: Int = 0
    private var transFattyAcid: Int = 0
    private var cholesterol: Int = 0
    private var sodium: Int = 0
    private var totalCarbohydrate: Int = 0
    private var dietaryFibre: Int = 0
    private var sugars: Int = 0
    private var protein: Int = 0
    private var vitaminADV: Int = 0
    private var vitaminCDV: Int = 0
    private var calciumDV: Int = 0
    private var ironDV: Int = 0
    private var servingWeightGrams: Int = 0

    /*public int retriveInt(int i){
        if(i == null){

        }
    }*/
    constructor(`object`: JSONObject) {

        try {

            this.name = `object`.getString("item_name")
            this.brandName = `object`.getString("brand_name")

            //            this.itemDescription = object.getString("itemDescription");
            //            this.calories = object.getInt("nf_calories");
            //            this.caloriesFromFat = object.getInt("nf_caloriesFromFat");
            //            this.totalFat = object.getInt("nf_totalFat");
            //            this.saturatedFat = object.getInt("nf_saturatedFat");
            //            this.transFattyAcid = object.getInt("nf_transFattyAcid");
            //            this.cholesterol = object.getInt("nf_cholesterol");
            //            this.sodium = object.getInt("nf_sodium");
            //            this.totalCarbohydrate = object.getInt("nf_totalCarbohydrate");
            //            this.dietaryFibre = object.getInt("nf_dietaryFibre");
            //            this.sugars = object.getInt("nf_sugars");
            //            this.protein = object.getInt("nf_protein");
            //            this.vitaminADV = object.getInt("nf_vitaminADV");
            //            this.vitaminCDV = object.getInt("nf_vitaminCDV");
            //            this.calciumDV = object.getInt("nf_calciumDV");
            //            this.ironDV = object.getInt("nf_ironDV");
            //            this.servingWeightGrams = object.getInt("nf_servingWeightGrams")

        } catch (e: JSONException) {
            e.printStackTrace()
        }


    }

    /*@Override
    public String toString() {
        return super.toString();
        return ""+name+" "+brandName+" "+itemDescription+" "+calories+ " " + caloriesFromFat+ " "
                +totalFat+" " + saturatedFat+ " "+ transFattyAcid+ " "+ cholesterol+ " "+ sodium
                + totalCarbohydrate+ " "+dietaryFibre+ " " + sugars+ " "+ protein+ " " + vitaminADV+ " "+
    }*/

    constructor(name: String, brandName: String, itemDescription: String, calories: Int,
                caloriesFromFat: Int, totalFat: Int, saturatedFat: Int, transFattyAcid: Int,
                cholesterol: Int, sodium: Int, totalCarbohydrate: Int, dietaryFibre: Int, sugars: Int,
                protein: Int, vitaminADV: Int, vitaminCDV: Int, calciumDV: Int, ironDV: Int,
                servingWeightGrams: Int) {

        this.name = name
        this.brandName = brandName
        this.itemDescription = itemDescription
        this.calories = calories
        this.caloriesFromFat = caloriesFromFat
        this.totalFat = totalFat
        this.saturatedFat = saturatedFat
        this.transFattyAcid = transFattyAcid
        this.cholesterol = cholesterol
        this.sodium = sodium
        this.totalCarbohydrate = totalCarbohydrate
        this.dietaryFibre = dietaryFibre
        this.sugars = sugars
        this.protein = protein
        this.vitaminADV = vitaminADV
        this.vitaminCDV = vitaminCDV
        this.calciumDV = calciumDV
        this.ironDV = ironDV
        this.servingWeightGrams = servingWeightGrams

    }

    internal fun update(name: String, brandName: String, itemDescription: String, calories: Int,
                        caloriesFromFat: Int, totalFat: Int, saturatedFat: Int, transFattyAcid: Int,
                        cholesterol: Int, sodium: Int, totalCarbohydrate: Int, dietaryFibre: Int, sugars: Int,
                        protein: Int, vitaminADV: Int, vitaminCDV: Int, calciumDV: Int, ironDV: Int,
                        servingWeightGrams: Int) {

        this.name = name
        this.brandName = brandName
        this.itemDescription = itemDescription
        this.calories = calories
        this.caloriesFromFat = caloriesFromFat
        this.totalFat = totalFat
        this.saturatedFat = saturatedFat
        this.transFattyAcid = transFattyAcid
        this.cholesterol = cholesterol
        this.sodium = sodium
        this.totalCarbohydrate = totalCarbohydrate
        this.dietaryFibre = dietaryFibre
        this.sugars = sugars
        this.protein = protein
        this.vitaminADV = vitaminADV
        this.vitaminCDV = vitaminCDV
        this.calciumDV = calciumDV
        this.ironDV = ironDV
        this.servingWeightGrams = servingWeightGrams

    }

}
