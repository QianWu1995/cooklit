package com.macrohard.cooklit.database.model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

import com.macrohard.cooklit.database.utils.CooklitRepository

import java.util.ArrayList

// IngredientViewModel is reponsible for preapring and managing
//  the data for an Activity. It is used by activities
//  Activities should be able to observe changes in the viewmodel
class IngredientViewModel// AndroidViewModel accpets application as the only param
(application: Application) : AndroidViewModel(application) {

    private val mRepository: CooklitRepository
    // getAllIngreidents is called by acitivites and it returns allingredients
    val allIngredients: LiveData<List<Ingredient>>

    //if selected ingredients, add to the list
    val selectedIngredients: List<Ingredient>
        get() {
            val selectedIngredients = ArrayList<Ingredient>()
            val ingredientSize = allIngredients.value!!.size
            for (i in 0 until ingredientSize) {
                if (allIngredients.value!![i].getSelected()) {
                    selectedIngredients.add(allIngredients.value!![i])
                }
            }
            return selectedIngredients
        }

    init {
        mRepository = CooklitRepository(application)
        allIngredients = mRepository.allIngredients
    }

    // insert is called by activites and insert ingredient into repository
    fun insert(ingredient: Ingredient) {
        mRepository.insert(ingredient)
    }

    fun deleteIngreident(ingredient: Ingredient) {
        mRepository.deleteIngredient(ingredient)
    }

    fun deleteAll() {
        mRepository.deleteAllIngredients()
    }
}
