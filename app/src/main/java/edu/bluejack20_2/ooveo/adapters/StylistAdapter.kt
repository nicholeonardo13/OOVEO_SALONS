package edu.bluejack20_2.ooveo.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.ChooseScheduleActivity
import edu.bluejack20_2.ooveo.R
import edu.bluejack20_2.ooveo.ScheduleStylistAdminActivity
import edu.bluejack20_2.ooveo.model.StylistModel

class StylistAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var items: List<StylistModel> = ArrayList()
    private lateinit var db : FirebaseFirestore

    fun submitList(serviceList: List<StylistModel>){
        items = serviceList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StylistViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.stylist_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val newList = items[position]

        when(holder){
            is StylistViewHolder ->{
                holder.binding(items.get(position))

                val btnChoose = holder.itemView.findViewById<Button>(R.id.btnChooseStylist)
                val id = newList.id

                btnChoose.setOnClickListener {
                    val mIntent = Intent(holder.itemView.context, ChooseScheduleActivity::class.java)
                    mIntent.putExtra("id",id)
                    holder.itemView.context.startActivity(mIntent)
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
        val stylist_profile: ImageView = itemView.findViewById(R.id.ivStylist)

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