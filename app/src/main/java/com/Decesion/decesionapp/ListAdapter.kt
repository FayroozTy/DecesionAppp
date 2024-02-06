package com.Decesion.decesionapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView



class CustomAdapter(private val mList: List<listModel>, val context: Context) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_list, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        //  holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.textView.text = ItemsViewModel.decesionText + "...."
        holder.date.text = ItemsViewModel.decesionDate.split("T")[0]
        holder.status.text = ItemsViewModel.decesionStatus

        holder.itemView.setOnClickListener {


            val intent = Intent(context, DeatilsActivity::class.java)
            intent.putExtra("decesionText",ItemsViewModel.decesionText)
            intent.putExtra("decesionid",ItemsViewModel.decesionid)

            startActivity(context,intent,null)


            ////////////////////////////////////


        }
//        holder.shareBtn.setOnClickListener {
//
//        }
    }





    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val textView: TextView = itemView.findViewById(R.id.sub)
        val date: TextView = itemView.findViewById(R.id.date)
        val status: TextView = itemView.findViewById(R.id.status)
        // val shareBtn: ImageView = itemView.findViewById(R.id.shareBtn)




    }
}




class listModel(var decesionText:String,var decesionDate:String,var decesionStatus:String,var decesionid:String) {
}

class CommintyModel(var Comm_no:Int,var Comm_Name:String) {
}

