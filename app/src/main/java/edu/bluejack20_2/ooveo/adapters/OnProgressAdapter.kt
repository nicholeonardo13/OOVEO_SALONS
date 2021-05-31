package edu.bluejack20_2.ooveo.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import edu.bluejack20_2.ooveo.R
import edu.bluejack20_2.ooveo.model.OnprogressModel
import java.text.SimpleDateFormat

class OnProgressAdapter(var listCart: MutableList<OnprogressModel>): RecyclerView.Adapter<OnProgressAdapter.OnProgressHolder>()
{

    private lateinit var imageUrl: String
    private lateinit var date: String
    private lateinit var time: String
    private lateinit var serviceName: String

    class OnProgressHolder (itemView: View):
        RecyclerView.ViewHolder(itemView){
        //Inisialisasi
        var imageView: ImageView = itemView.findViewById(R.id.ivOnprogressImage)
        var tvDate: TextView = itemView.findViewById(R.id.tvOnprogressItemDate)
        var tvLocation: TextView = itemView.findViewById(R.id.tvOnprogressLocation)
        var tvTime: TextView = itemView.findViewById(R.id.tvOnprogressTime)
        var tvServiceName: TextView = itemView.findViewById(R.id.tvOnprogressServiceName)

    }

    override fun getItemCount(): Int {
        return listCart.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OnProgressHolder, position: Int) {
        //Disini Bind data
        val user = listCart[position]
        user.location.get().addOnSuccessListener {
            holder.tvLocation.text = it["location"] as String

            val requestOption = RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)

            Glide.with(holder.itemView)
                    .applyDefaultRequestOptions(requestOption)
                    .load(it["image"] as String)
                    .into(holder.imageView)

            user.serviceName.get().addOnSuccessListener {
                holder.tvServiceName.text = it["name"] as String
            }
        }

        var fmt = SimpleDateFormat("EEE, d MMM YYYY")

        holder.tvDate.text = fmt.format(user.date.toDate())
//        holder.tvLocation.text = user.location


//        holder.tvServiceName.text = user.serviceName
        holder.tvTime.text = user.start_time + " - " + user.end_time


    }

    //Buat munculin Holder, Template
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnProgressHolder {
        return OnProgressHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.onprogress_item, parent, false)
        )
    }
}