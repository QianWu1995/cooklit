package com.macrohard.cooklit.support.adapters

import android.content.Context
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

import com.macrohard.cooklit.R
import com.squareup.picasso.Picasso

import java.util.ArrayList


class RecipeListViewAdapter(private val context: Context, resource: Int, private val imageUri: ArrayList<String>, private val titles: ArrayList<String>, private val tags: ArrayList<String>) : ArrayAdapter<String>(context, resource, imageUri) {

    init {
        Log.d("initialization", "done")
        Log.d("size", "" + imageUri.size)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        Log.d("getting view", "" + imageUri.size)

        // Get the data item for this position
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.elementview, parent, false)
        }

        Log.d("position is", position.toString() + "")

        // Lookup view for data population
        val icon = convertView!!.findViewById<ImageView>(R.id.thumbnail)
        val title = convertView.findViewById<TextView>(R.id.thumbnaildescription)
        val tag = convertView.findViewById<TextView>(R.id.tags)

        /*final ImageButton likebutton = convertView.findViewById(R.id.likebutton);
        likebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likebutton.setImageResource(R.drawable.redheart);
            }
        });*/
        // Populate the data into the template view using the data object
        if (position <= imageUri.size - 1) {
            Log.d("title", titles[position])
            title.text = titles[position]
            tag.text = tags[position]
            Log.d("image uri", imageUri[position])
            Picasso.with(context).load(imageUri[position]).into(icon)
            //icon.setImageDrawable(objects2.get(position));

        }

        return convertView

    }

}
