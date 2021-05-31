package edu.bluejack20_2.ooveo.adapters
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.DeleteServiceActivity
import edu.bluejack20_2.ooveo.R
import edu.bluejack20_2.ooveo.UpdateServiceActivity
import edu.bluejack20_2.ooveo.model.ServiceModel
class ServiceAdminAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<ServiceModel> = ArrayList()
    private lateinit var db : FirebaseFirestore

    fun submitList(serviceList: List<ServiceModel>){
        items = serviceList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ServiceViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.service_admin_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val newList = items[position]

        when(holder){
            is ServiceViewHolder ->{
                holder.binding(items.get(position))

                val btnUpdate = holder.itemView.findViewById<Button>(R.id.btnupdateServiceAdmin)
                val btnDelete = holder.itemView.findViewById<Button>(R.id.btndeleteServiceAdmin)

                val id = newList.id

                btnUpdate.setOnClickListener {
                    val mIntent = Intent(holder.itemView.context, UpdateServiceActivity::class.java)
                    mIntent.putExtra("id",id)
                    holder.itemView.context.startActivity(mIntent)
                }

                btnDelete.setOnClickListener {
                    println("DELETE DI KLIK")
                    val mIntent = Intent(holder.itemView.context, DeleteServiceActivity::class.java)
                    mIntent.putExtra("id",id)
                    holder.itemView.context.startActivity(mIntent)
                }

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