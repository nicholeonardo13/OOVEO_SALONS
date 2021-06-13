package edu.bluejack20_2.ooveo.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.*
import edu.bluejack20_2.ooveo.model.ScheduleModel


class ScheduleAdapter(var merchantID: String, var serviceID: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var mAuth: FirebaseAuth

    private var items: List<ScheduleModel> = ArrayList()
    private lateinit var db : FirebaseFirestore

    fun submitList(scheduleList: List<ScheduleModel>){
        items = scheduleList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ScheduleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.schedule_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val newList = items[position]

        when(holder){
            is ScheduleViewHolder -> {
                holder.binding(items.get(position))

                val btnChoose = holder.itemView.findViewById<Button>(R.id.btnChooseSchedule)

                val id = newList.id
                val stylistID = newList.stylistID
                val date = newList.date
                val hour = newList.hour

                mAuth = FirebaseAuth.getInstance()

                btnChoose.setOnClickListener {
                    val mIntent = Intent(
                        holder.itemView.context,
                        AppointmentCartActivity::class.java
                    )
                    mIntent.putExtra("id", id)
                    mIntent.putExtra("date", date)
                    mIntent.putExtra("hour", hour)
                    mIntent.putExtra("stylistID", stylistID)
                    mIntent.putExtra("merchantID", merchantID)
                    mIntent.putExtra("serviceID", serviceID)
                    mIntent.putExtra("userID", mAuth.currentUser.uid)
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