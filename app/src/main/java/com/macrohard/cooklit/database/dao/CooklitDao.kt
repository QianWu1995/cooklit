package com.macrohard.cooklit.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

import com.macrohard.cooklit.database.model.Ingredient
import com.macrohard.cooklit.database.model.Recipe

@Dao
abstract class CooklitDao {

    @get:Query("SELECT * FROM ingredient_table ORDER BY expirydate ASC")
    abstract val allIngredients: LiveData<List<Ingredient>>

    @get:Query("SELECT * FROM recipe_table ORDER BY name ASC")
    abstract val allRecipes: LiveData<List<Recipe>>

    // Dao for Ingredient
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertIngredient(ingredient: Ingredient)

    @Query("DELETE FROM ingredient_table")
    abstract fun deleteAllIngredient()

    @Query("SELECT * FROM ingredient_table WHERE name = :name")
    abstract fun fetchIngredientbyName(name: String): Ingredient

    @Update
    internal abstract fun updateIngredient(ingredient: Ingredient)

    @Delete
    abstract fun deleteIngredient(ingredient: Ingredient)

    // DAO for Recipe
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertRecipe(recipe: Recipe)

    @Query("DELETE FROM recipe_table")
    abstract fun deleteAllRecipes()

    @Query("SELECT * FROM recipe_table WHERE name = :name")
    abstract fun fetchRecipebyName(name: String): Recipe

    @Query("SELECT * FROM recipe_table WHERE day = :day")
    abstract fun getRecipesByDay(day: String): List<Recipe>

    @Delete
    abstract fun deleteRecipe(recipe: Recipe)

}
