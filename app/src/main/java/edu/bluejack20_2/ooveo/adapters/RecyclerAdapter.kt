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
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.homes.DetailMerchantActivity
import edu.bluejack20_2.ooveo.model.MerchantModel
import edu.bluejack20_2.ooveo.R
import edu.bluejack20_2.ooveo.model.FavouriteModel
import edu.bluejack20_2.ooveo.model.ScheduleModel
import java.util.ArrayList

class RecyclerAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<MerchantModel> = ArrayList()
    private lateinit var mID : String
    private val db = FirebaseFirestore.getInstance()
    private var mAuth = FirebaseAuth.getInstance();
    private lateinit var listFavModel : ArrayList<FavouriteModel>

    fun submitList(barberList: List<MerchantModel>){
        items = barberList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MerchantViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.merchant_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val newList = items[position]

        when(holder){
            is MerchantViewHolder ->{
                holder.binding(items.get(position))

                val btnBookNow =  holder.itemView.findViewById<Button>(R.id.btnBookNow)
                val btnAddFav =  holder.itemView.findViewById<Button>(R.id.btnAddFav)
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

                btnAddFav.setOnClickListener {
                    getAllSchedule(mAuth.uid.toString() , newList.id , holder)
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


    private fun getAllSchedule(userID : String , merchantID : String , holder : RecyclerView.ViewHolder){
        db.collection("favourites").whereEqualTo("userID" , userID).whereEqualTo("merchantID" , merchantID).get()
            .addOnSuccessListener {
                listFavModel = ArrayList()
                listFavModel.clear()
                for (document in it.documents){

                    listFavModel.add(
                        FavouriteModel(
                            document.id as String,
                            document.data?.get("userID") as String,
                            document.data?.get("merchantID") as String,
                        )
                    )

//                        println("TESTT")
                }


                if(!listFavModel.isEmpty()){
                    Toast.makeText(holder.itemView.context , holder.itemView.context.getString(R.string.merchantAddFav) , Toast.LENGTH_SHORT).show()
                }else{
                    val service : MutableMap<String, Any> = HashMap()


                    service["userID"] = mAuth.uid.toString()
                    service["merchantID"] = merchantID

                    db.collection("favourites").add(service)
                        .addOnSuccessListener {
//                Toast.makeText(this@RecyclerAdapter , "Berhasil" , Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
//                Toast.makeText(this@RecyclerAdapter , "GAGAL" , Toast.LENGTH_SHORT).show()
                        }
                }

            }

    }

}