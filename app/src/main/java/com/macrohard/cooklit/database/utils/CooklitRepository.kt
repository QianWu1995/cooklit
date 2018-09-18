package com.macrohard.cooklit.database.utils

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask

import com.macrohard.cooklit.database.dao.CooklitDao
import com.macrohard.cooklit.database.model.Ingredient
import com.macrohard.cooklit.database.model.Recipe

class CooklitRepository(application: Application) {

    private val mCooklitDao: CooklitDao
    val allIngredients: LiveData<List<Ingredient>>

    init {
        val db = CooklitDatabase.getDatabase(application)
        mCooklitDao = db.CooklitDao()
        allIngredients = mCooklitDao.allIngredients
    }

    fun insert(ingredient: Ingredient) {
        insertIngredient(mCooklitDao).execute(ingredient)
    }

    private class insertIngredient internal constructor(private val mAsyncTaskDao: CooklitDao) : AsyncTask<Ingredient, Void, Void>() {

        override fun doInBackground(vararg params: Ingredient): Void? {
            mAsyncTaskDao.insertIngredient(params[0])
            return null
        }

    }

    // delete a ingredient
    private class deleteIngredientAsyncTask internal constructor(private val mAsyncTaskDao: CooklitDao) : AsyncTask<Ingredient, Void, Void>() {

        override fun doInBackground(vararg params: Ingredient): Void? {
            mAsyncTaskDao.deleteIngredient(params[0])
            return null
        }

    }

    fun deleteIngredient(ingredient: Ingredient) {
        deleteIngredientAsyncTask(mCooklitDao).execute(ingredient)

    }

    // delete allIngredients
    private class deleteAllIngredientAsyncTask//constructor
    internal constructor(private val mAsyncTaskDao: CooklitDao) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg voids: Void): Void? {
            mAsyncTaskDao.deleteAllIngredient()
            return null
        }
    }

    fun deleteAllIngredients() {
        deleteAllIngredientAsyncTask(mCooklitDao).execute()
    }


}
