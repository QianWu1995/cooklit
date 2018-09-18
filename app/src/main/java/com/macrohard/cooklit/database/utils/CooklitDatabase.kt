package com.macrohard.cooklit.database.utils

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context
import android.os.AsyncTask

import com.macrohard.cooklit.database.dao.CooklitDao
import com.macrohard.cooklit.database.model.Ingredient
import com.macrohard.cooklit.database.model.Recipe

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList


@Database(entities = arrayOf(Ingredient::class, Recipe::class), version = 5)
abstract class CooklitDatabase : RoomDatabase() {
    abstract fun CooklitDao(): CooklitDao

    private class PopulateDbAsync internal constructor(db: CooklitDatabase) : AsyncTask<Void, Void, Void>() {
        private val mDao: CooklitDao

        init {
            mDao = db.CooklitDao()
        }

        override fun doInBackground(vararg params: Void): Void? {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.

            mDao.deleteAllIngredient()
            mDao.deleteAllRecipes()
            var ingredient = Ingredient("Chicken", "2018-05-07")
            mDao.insertIngredient(ingredient)
            ingredient = Ingredient("Avocado", "2018-05-30")
            mDao.insertIngredient(ingredient)
            ingredient = Ingredient("Banana", "2018-06-30")
            mDao.insertIngredient(ingredient)
            ingredient = Ingredient("Apple", "2018-07-30")
            mDao.insertIngredient(ingredient)
            ingredient = Ingredient("Kiwi", "2018-07-31")
            mDao.insertIngredient(ingredient)
            ingredient = Ingredient("Pineapple", "2018-08-02")
            mDao.insertIngredient(ingredient)
            ingredient = Ingredient("Meet", "2018-08-03")
            mDao.insertIngredient(ingredient)
            ingredient = Ingredient("Beef", "2018-10-02")
            mDao.insertIngredient(ingredient)
            ingredient = Ingredient("Onion", "2018-01-01")
            mDao.insertIngredient(ingredient)
            ingredient = Ingredient("Ginger", "2018-02-02")
            mDao.insertIngredient(ingredient)
            ingredient = Ingredient("Tofu", "2018-03-03")

            mDao.insertIngredient(ingredient)

            return null
        }

    }

    companion object {

        private var INSTANCE: CooklitDatabase? = null

        fun getDatabase(context: Context): CooklitDatabase {
            if (INSTANCE == null) {
                synchronized(CooklitDatabase::class.java) {
                    // if DB does not exist, create DB here
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder<CooklitDatabase>(context.applicationContext,
                                CooklitDatabase::class.java!!, "cooklit_database")
                                .fallbackToDestructiveMigration()
                                //.addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                                .addCallback(sRoomDatabaseCallback)
                                .allowMainThreadQueries()
                                .build()
                    }
                }
            }
            return INSTANCE
        }

        internal val MIGRATION_1_2: Migration = object : Migration(1, 2) {

            override fun migrate(database: SupportSQLiteDatabase) {
                // didn't alter the table, so empty
            }
        }

        internal val MIGRATION_2_3: Migration = object : Migration(2, 3) {

            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE recipe_table " + " ADD COLUMN saved BOOLEAN")
            }
        }

        private val sRoomDatabaseCallback = object : RoomDatabase.Callback() {

            /*
        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
        */

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsync(INSTANCE!!).execute()
            }
        }
    }
}
