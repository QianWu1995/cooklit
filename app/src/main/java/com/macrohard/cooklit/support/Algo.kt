package com.macrohard.cooklit.support

import java.util.ArrayList

object Algo {
    internal fun sort(`in`: ArrayList<String>, keyword: String): Boolean { // in is the total ingredients[
        // "2 Tbsp vegetable oil",
        //"1 lb small red potato, sliced 1/4-inch thick]

        for (i in `in`.indices) {
            val ary = `in`[i].split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            for (ii in ary.indices) {
                if (keyword === ary[ii]) {
                    return true
                }
            }
        }
        return false
    }
}

