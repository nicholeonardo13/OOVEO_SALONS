package edu.bluejack20_2.ooveo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class MerchantAdapter(private val merchantModelList: ArrayList<MerchantModel>) : RecyclerView.Adapter<MerchantAdapter.MerchantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MerchantViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.merchant_item , parent , false)
        return MerchantViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MerchantViewHolder, position: Int) {
//        var currentItem = merchantList[position]
//
//        holder.merchantName.text = currentItem.merchantName
//        holder.merchantAddress.text = currentItem.merchantAddress

        holder.bindMerchant(merchantModelList[position])
    }

    override fun getItemCount(): Int {
        return merchantModelList.count()
    }


    class MerchantViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val merchantName : TextView = itemView.findViewById(R.id.txtMerchant)
        val merchantAddress : TextView = itemView.findViewById(R.id.txtAlamat)

        fun bindMerchant(merchantModel:MerchantModel){
            merchantName.text = merchantModel.merchantName
            merchantAddress.text = merchantModel.merchantAddress
        }
    }

}