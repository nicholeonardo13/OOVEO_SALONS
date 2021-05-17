package edu.bluejack20_2.ooveo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ServiceAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: List<ServiceModel> = ArrayList()

    fun submitList(serviceList: List<ServiceModel>){
        items = serviceList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ServiceViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.service_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ServiceViewHolder ->{
                holder.binding(items.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ServiceViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView){

        val service_name: TextView = itemView.findViewById(R.id.service_name)
        val service_price: TextView = itemView.findViewById(R.id.service_price)
        val service_description: TextView = itemView.findViewById(R.id.service_desc)

        fun binding(service: ServiceModel){
            service_name.setText(service.name)
            service_price.setText(service.price.toString())
            service_description.setText(service.description)
        }


    }
}