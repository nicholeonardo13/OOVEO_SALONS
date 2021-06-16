package edu.bluejack20_2.ooveo.adapters

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import edu.bluejack20_2.ooveo.*
import edu.bluejack20_2.ooveo.model.ServiceModel
import edu.bluejack20_2.ooveo.model.StylistModel

class StylistAdminAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<StylistModel> = ArrayList()
    private lateinit var db : FirebaseFirestore

    fun submitList(serviceList: List<StylistModel>){
        items = serviceList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StylistViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.stylist_admin_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val newList = items[position]

        when(holder){
            is StylistViewHolder ->{
                holder.binding(items.get(position))

                val btnUpdate = holder.itemView.findViewById<Button>(R.id.btnupdateStylistAdmin)
                val btnDelete = holder.itemView.findViewById<Button>(R.id.btndeleteStylistAdmin)
                val btnSchedule = holder.itemView.findViewById<Button>(R.id.btnScheduleStylistAdmin)
                val id = newList.id

                btnUpdate.setOnClickListener {
                    val mIntent = Intent(holder.itemView.context, UpdateStylistActivity::class.java)
                    mIntent.putExtra("id",id)
                    holder.itemView.context.startActivity(mIntent)
                    (holder.itemView.context as Activity).finish()
                }

                btnDelete.setOnClickListener {
                    println("DELETE DI KLIK")
                    val mIntent = Intent(holder.itemView.context, DeleteStylistActivity::class.java)
                    mIntent.putExtra("id",id)
                    holder.itemView.context.startActivity(mIntent)
                    (holder.itemView.context as Activity).finish()
                }

                btnSchedule.setOnClickListener {
                    println("Schedule DI KLIK")
                    val mIntent = Intent(holder.itemView.context, ScheduleStylistAdminActivity::class.java)
                    Log.e("ini id" , id)
                    mIntent.putExtra("id",id)
                    holder.itemView.context.startActivity(mIntent)
                    (holder.itemView.context as Activity).finish()
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class StylistViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView){

        val stylist_name: TextView = itemView.findViewById(R.id.stylist_name)
        val stylist_gender: TextView = itemView.findViewById(R.id.stylist_gender)
        val stylist_profile: ImageView = itemView.findViewById(R.id.ivStylistAdmin)

        fun binding(stylist: StylistModel){
            stylist_name.setText(stylist.name)
            stylist_gender.setText(stylist.gender)

            val requestOption = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOption)
                .load(stylist.profilePicture)
                .into(stylist_profile)

        }


    }

}