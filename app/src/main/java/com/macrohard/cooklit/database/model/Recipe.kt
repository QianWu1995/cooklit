package com.macrohard.cooklit.database.model


import android.arch.persistence.room.Entity
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Formatter


@Entity(tableName = "recipe_table")
class Recipe {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    @ColumnInfo(name = "name")
    var name: String
    @ColumnInfo(name = "uri")
    var uri: String
    @ColumnInfo(name = "day")
    var day: String
    @ColumnInfo(name = "time")
    var time: String
    @ColumnInfo(name = "date")
    var date: String
    @ColumnInfo(name = "repeat")
    var repeat: Boolean = false

    var formattedDate: Date?
        get() {
            val answer: Date? = null
            try {
                return SimpleDateFormat("dd/MM/yyyy").parse(date)
            } catch (PE: ParseException) {
                System.err.println("Error when parsing date")
            }

            return answer
        }
        set(date) {
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            this.date = formatter.format(date)
        }


    constructor(id: Int, name: String, uri: String) {
        this.id = id
        this.name = name
        this.uri = uri
    }

    @Ignore
    constructor(id: Int, name: String, uri: String, day: String, time: String, repeat: Boolean) {
        this.id = id
        this.name = name
        this.uri = uri
        this.day = day
        this.time = time
        this.repeat = repeat
    }

}
