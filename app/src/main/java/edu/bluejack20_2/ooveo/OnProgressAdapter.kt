package edu.bluejack20_2.ooveo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OnProgressAdapter(): RecyclerView.Adapter<OnProgressAdapter.OnProgressHolder>()
{

    private lateinit var imageUrl: String
    private lateinit var date: String
    private lateinit var time: String
    private lateinit var serviceName: String

    class OnProgressHolder (itemView: View):
        RecyclerView.ViewHolder(itemView){
        //Inisialisasi
        private var imageView: ImageView = itemView.findViewById(R.id.ivOnprogressImage)
        private var tvDate: TextView = itemView.findViewById(R.id.tvOnprogressItemDate)
        private var tvLocation: TextView = itemView.findViewById(R.id.tvOnprogressLocation)
        private var tvTime: TextView = itemView.findViewById(R.id.tvOnprogressTime)
        private var tvServiceName: TextView = itemView.findViewById(R.id.tvOnprogressServiceName)

    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: OnProgressHolder, position: Int) {
        //Disini Bind data


    }

    //Buat munculin Holder, Template
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnProgressHolder {
        return OnProgressHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.onprogress_item, parent, false)
        )
    }
}