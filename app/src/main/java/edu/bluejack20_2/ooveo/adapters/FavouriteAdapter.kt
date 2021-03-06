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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import edu.bluejack20_2.ooveo.R
import edu.bluejack20_2.ooveo.homes.DetailMerchantActivity
import edu.bluejack20_2.ooveo.model.MerchantModel
import java.util.ArrayList

class FavouriteAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<MerchantModel> = ArrayList()

    fun submitList(barberList: List<MerchantModel>){
        items = barberList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MerchantViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.favourite_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val newList = items[position]

        when(holder){
            is MerchantViewHolder ->{
                holder.binding(items.get(position))

                val btnBookNow =  holder.itemView.findViewById<Button>(R.id.btnBookNow)
                btnBookNow.setOnClickListener {
                    Log.d("Click", "Merchant Di Klik")
                    val id = newList.id
                    val img = newList.image
                    val name = newList.name
                    val address = newList.address
                    val phoneNumber = newList.phoneNumber
                    val location = newList.location
                    val type = newList.type
                    val about = newList.about
                    val mIntent = Intent(holder.itemView.context, DetailMerchantActivity::class.java)
                    mIntent.putExtra("id",id)
                    mIntent.putExtra("image",img)
                    mIntent.putExtra("name",name)
                    mIntent.putExtra("phoneNumber",phoneNumber)
                    mIntent.putExtra("location",location)
                    mIntent.putExtra("type",type)
                    mIntent.putExtra("address",address)
                    mIntent.putExtra("about",about)
                    holder.itemView.context.startActivity(mIntent)
                    (holder.itemView.context as Activity).finish()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class MerchantViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView){

        val merchant_image: ImageView = itemView.findViewById(R.id.merchant_image)
        val merchant_name: TextView = itemView.findViewById(R.id.merchant_name)
        val merchant_address: TextView = itemView.findViewById(R.id.merchant_address)

        fun binding(barber: MerchantModel){
            merchant_name.setText(barber.name)
            merchant_address.setText(barber.address)

            val requestOption = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOption)
                .load(barber.image)
                .into(merchant_image)

        }
    }
}