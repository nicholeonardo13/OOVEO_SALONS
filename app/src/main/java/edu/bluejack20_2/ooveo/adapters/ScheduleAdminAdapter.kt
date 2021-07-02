package edu.bluejack20_2.ooveo.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.*
import edu.bluejack20_2.ooveo.model.ScheduleModel

class ScheduleAdminAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var items: List<ScheduleModel> = ArrayList()
    private lateinit var db : FirebaseFirestore

    fun submitList(scheduleList: List<ScheduleModel>){
        items = scheduleList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ScheduleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.schedule_admin_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val newList = items[position]

        when(holder){
            is ScheduleViewHolder ->{
                holder.binding(items.get(position))

                val btnUpdate = holder.itemView.findViewById<Button>(R.id.btnupdateScheduleAdmin)
                val btnDelete = holder.itemView.findViewById<Button>(R.id.btndeleteScheduleAdmin)

                val id = newList.id
                val stylistID = newList.stylistID

                btnUpdate.setOnClickListener {
                    val mIntent = Intent(holder.itemView.context, UpdateScheduleActivity::class.java)
                    mIntent.putExtra("id",id)
                    mIntent.putExtra("stylistID",stylistID)
                    holder.itemView.context.startActivity(mIntent)
                    (holder.itemView.context as Activity).finish()
                }

                btnDelete.setOnClickListener {
                    println("DELETE DI KLIK")
                    val mIntent = Intent(holder.itemView.context, DeleteScheduleActivity::class.java)
                    mIntent.putExtra("id",id)
                    mIntent.putExtra("stylistID",stylistID)
                    holder.itemView.context.startActivity(mIntent)
                    (holder.itemView.context as Activity).finish()
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ScheduleViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView){

        val date: TextView = itemView.findViewById(R.id.schedule_Date)
        val hour: TextView = itemView.findViewById(R.id.schedule_Hour)
//        val status: TextView = itemView.findViewById(R.id.service_desc)

        fun binding(schedule: ScheduleModel){
            date.setText(schedule.date)
            hour.setText(schedule.hour)
//            status.setText(schedule.status)
        }


    }
}