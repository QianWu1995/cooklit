package com.macrohard.cooklit.database.model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

import com.macrohard.cooklit.database.dao.CooklitDao
import com.macrohard.cooklit.database.utils.CooklitDatabase

// RecipeViewModel is reponsible for preapring and managing
//  the data for an Activity. It is used by activities
//  Activities should be able to observe changes in the viewmodel
class RecipeViewModel// AndroidViewModel accpets application as the only param
(application: Application) : AndroidViewModel(application) {

    private val cooklitDao: CooklitDao
    private val mAllRecipes: LiveData<List<Recipe>>

    init {
        cooklitDao = CooklitDatabase.getDatabase(application.applicationContext).CooklitDao()
        //The line below is redundant, remove if possible
        mAllRecipes = cooklitDao.allRecipes
    }


    // getAllRecipes is called by acitivites and it returns allRecipes
    fun getmAllRecipes(): LiveData<List<Recipe>> {
        return mAllRecipes
    }

    // getRecipesbyDate returns List of Recipe that conatins the day
    fun getRecipesByDay(day: String): List<Recipe> {
        return cooklitDao.getRecipesByDay(day)
    }

    // insert is called by activites and insert recipe into repository
    fun insert(recipe: Recipe) {
        cooklitDao.insertRecipe(recipe)
    }

    fun deleteRecipe(recipe: Recipe) {
        cooklitDao.deleteRecipe(recipe)
    }


}
