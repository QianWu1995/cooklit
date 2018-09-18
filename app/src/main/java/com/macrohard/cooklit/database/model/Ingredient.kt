package com.macrohard.cooklit.database.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "ingredient_table")
class Ingredient(@field:PrimaryKey
                 @field:ColumnInfo(name = "name")
                 var name: String, @field:ColumnInfo(name = "expirydate")
                 var expiryDate: String) {

    @Ignore
    internal var selected: Boolean? = false

    // checkbox
    fun getSelected(): Boolean {
        return selected!!
    }

    fun setSelected(select: Boolean) {
        selected = select
    }

}
