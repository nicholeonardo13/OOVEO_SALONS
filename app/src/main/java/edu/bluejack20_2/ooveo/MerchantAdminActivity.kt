package edu.bluejack20_2.ooveo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.model.MerchantModel
import edu.bluejack20_2.ooveo.model.UserModel
import edu.bluejack20_2.ooveo.viewmodels.EditProfileActivityViewModel
import java.util.ArrayList

class MerchantAdminActivity : AppCompatActivity() {

    private lateinit var serviceBtn: Button
    private lateinit var stylistBtn: Button
    private lateinit var merchantProfileBtn: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var userProfile: ImageView

    private lateinit var db: FirebaseFirestore
    private lateinit var edtName: TextView
    private lateinit var merchantModel : MerchantModel
    private lateinit var edtAddress: TextView
    private lateinit var ivProfilePicture: ImageView
    private lateinit var listMerchantModel :ArrayList<MerchantModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merchant_admin)

        var ids = intent.extras?.getString("ownerID")

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        serviceBtn = findViewById<Button>(R.id.btnManageService)
        stylistBtn = findViewById<Button>(R.id.btnManageStylist)
        merchantProfileBtn = findViewById<Button>(R.id.btnMerchantEditProfile)
        userProfile = findViewById<ImageView>(R.id.ivProfileUserImage)
        edtName = findViewById<TextView>(R.id.tvMerchantName)
        edtAddress = findViewById<TextView>(R.id.tvMerchantAddress)
        ivProfilePicture = findViewById<ImageView>(R.id.ivProfileUserImage)

        println(" ")
        println(" ")
        println(" ")
        println("ID CURRENT USER")
        println(mAuth.currentUser.uid.toString())

        val docRef =  db.collection("merchants").whereEqualTo("ownerID" , ids)
        docRef.get()
            .addOnSuccessListener {

                    listMerchantModel = ArrayList()
                    listMerchantModel.clear()
                    for (document in it.documents){

                        merchantModel = MerchantModel(
                            document.id as String,
                            document.data?.get("name") as String,
                            document.data?.get("address") as String,
                            document.data?.get("image") as String,
                            document.data?.get("phoneNumber") as String,
                            document.data?.get("location") as String,
                            document.data?.get("type") as String,
                            document.data?.get("aboutus") as String
                        )

                        listMerchantModel.add(
                            merchantModel
                        )

                    }

                    edtName.setText(merchantModel.name)
                    edtAddress.setText(merchantModel.address)

                    val requestOption = RequestOptions()
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)

                    Glide.with(this)
                        .applyDefaultRequestOptions(requestOption)
                        .load(merchantModel.image)
                        .into(ivProfilePicture)
//                    Log.d("TAMPILIN DATA", "DocumentSnapshot data: ${document.data}")

            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }


        //Change user image profile
        merchantProfileBtn!!.setOnClickListener(View.OnClickListener {
            Log.e("NN", "Ke Edit")
            var intent = Intent(this@MerchantAdminActivity, EditMerchantActivity::class.java)
            intent.putExtra("id", merchantModel.id)
            startActivity(intent)
        })


        //MOVE TO SERVICE
        serviceBtn!!.setOnClickListener(View.OnClickListener {
            var intent = Intent(this@MerchantAdminActivity, ManageServiceActivity::class.java)
            intent.putExtra("id", merchantModel.id)
            startActivity(intent)
        })

        //MOVE TO Stylist
        stylistBtn!!.setOnClickListener(View.OnClickListener {
            var intent = Intent(this@MerchantAdminActivity, ManageStylistActivity::class.java)
            intent.putExtra("id", merchantModel.id)
            startActivity(intent)
        })

    }
}