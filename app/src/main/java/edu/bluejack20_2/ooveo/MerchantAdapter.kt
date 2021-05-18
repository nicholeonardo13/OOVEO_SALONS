package edu.bluejack20_2.ooveo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MerchantAdapter(private val merchantModelList: ArrayList<MerchantModel>) : RecyclerView.Adapter<MerchantAdapter.MerchantViewHolder>() {

    val merchantList = ArrayList<MerchantModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MerchantViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.merchant_item , parent , false)
        return MerchantViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MerchantViewHolder, position: Int) {
        var currentItem = merchantList[position]
//
//        holder.merchantName.text = currentItem.merchantName
//        holder.merchantAddress.text = currentItem.merchantAddress
//        holder.merchantName.text = currentItem.merchantName
//        holder.merchantAddress.text = currentItem.merchantAddress
        Glide.with(holder.itemView).load(currentItem).into(holder.itemView.findViewById(R.id.ivMerchant))
//        holder.bindMerchant(merchantModelList[position])
    }

    override fun getItemCount(): Int {
        return merchantModelList.size
    }


    class MerchantViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val merchantName : TextView = itemView.findViewById(R.id.txtMerchant)
        val merchantAddress : TextView = itemView.findViewById(R.id.txtAlamat)

        fun bindMerchant(merchantModel:MerchantModel){

        }
    }

}