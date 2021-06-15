package edu.bluejack20_2.ooveo.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.LanjutkanSAActivity
import edu.bluejack20_2.ooveo.R
import edu.bluejack20_2.ooveo.model.ReviewModel
import edu.bluejack20_2.ooveo.model.ScheduleModel

class ReviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private lateinit var mAuth: FirebaseAuth

    private var items: List<ReviewModel> = ArrayList()
    private lateinit var db : FirebaseFirestore

    fun submitList(scheduleList: List<ReviewModel>){
        items = scheduleList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ReviewViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val newList = items[position]

        when(holder){
            is ReviewViewHolder ->{
                holder.binding(items.get(position))

//                val btnChoose = holder.itemView.findViewById<Button>(R.id.btnChooseSchedule)

                val id = newList.id
//                val stylistID = newList.

//                mAuth = FirebaseAuth.getInstance()
//
//                btnChoose.setOnClickListener {
//                    val mIntent = Intent(holder.itemView.context, LanjutkanSAActivity::class.java)
//                    mIntent.putExtra("id",id)
//                    mIntent.putExtra("stylistID",stylistID)
//                    mIntent.putExtra("merchantID",merchantID)
//                    mIntent.putExtra("serviceID",serviceID)
//                    mIntent.putExtra("userID",mAuth.currentUser.uid)
//                    holder.itemView.context.startActivity(mIntent)
//                }

            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ReviewViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView){

        val feed: TextView = itemView.findViewById(R.id.review_feed)
        val rating: TextView = itemView.findViewById(R.id.review_rating)
//        val status: TextView = itemView.findViewById(R.id.service_desc)

        fun binding(rev: ReviewModel){
            feed.setText(rev.feedback)
            rating.setText("Rating : " + rev.rating.toString())
//            status.setText(schedule.status)
        }


    }
}