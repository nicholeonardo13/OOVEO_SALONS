package edu.bluejack20_2.ooveo

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.*

class RegisterMerchantActivity : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var address: EditText
    private lateinit var location: EditText
    private lateinit var phoneNumber: EditText
    private lateinit var uploadBanner: Button
    private lateinit var uploadBannerTextView: TextView
    private lateinit var nextBtn: MaterialButton
    private lateinit var mAuth: FirebaseAuth

    //UPLOAD BANNER
    private val PICK_IMAGE_REQUEST = 1234
    private var filePath: Uri? = null
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var bannerURL: String = ""

    lateinit var txtName: String
    lateinit var txtPhone: String
    lateinit var txtAddress: String
    lateinit var txtLocation: String
    lateinit var txtViewUploadBanner: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_merchant)
        init()



        //UPLAOD BANNER
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
        mAuth = FirebaseAuth.getInstance();

        uploadBanner!!.setOnClickListener(View.OnClickListener {
            txtName = name.text.toString()
            if(txtName.isEmpty()){
                Toast.makeText(this, "Please fill Merchant Name first", Toast.LENGTH_SHORT).show()
            }
            choosePicture()
        })

        nextBtn!!.setOnClickListener(View.OnClickListener {
            txtName = name.text.toString()
            txtPhone = phoneNumber.text.toString()
            txtAddress = address.text.toString()
            txtLocation = location.text.toString()
            txtViewUploadBanner = uploadBannerTextView.text.toString()

            if(txtName.isEmpty()){
                Toast.makeText(this, "Name must be filled!", Toast.LENGTH_SHORT).show()
            }else if(txtName.length < 3 ){
                Toast.makeText(this, "Name must be more than 3 character!", Toast.LENGTH_SHORT).show()
            }else if(txtAddress.isEmpty()){
                Toast.makeText(this, "Address must be filled!", Toast.LENGTH_SHORT).show()
            }else if(txtAddress.length < 10){
                Toast.makeText(this, "Address must be more than 10 characters!", Toast.LENGTH_SHORT).show()
            }else if(txtLocation.isEmpty()){
                Toast.makeText(this, "Location must be filled!", Toast.LENGTH_SHORT).show()
            }else if(txtPhone.isEmpty()){
                Toast.makeText(this, "Phone must be filled!", Toast.LENGTH_SHORT).show()
            }else if(txtPhone.length != 12){
                Toast.makeText(this, "Phone Number must be 12 characters", Toast.LENGTH_SHORT).show()
            }else if(bannerURL.isEmpty()){
                Toast.makeText(this, "Please choose a banner", Toast.LENGTH_SHORT).show()
            }else{
                var intent = Intent(this, RegisterMerchantActivity2::class.java)
                intent.putExtra("name", txtName)
                intent.putExtra("address", txtAddress)
                intent.putExtra("location", txtLocation)
                intent.putExtra("phone", txtPhone)
                //INI HARUS DIGANTI JADI URL YG UDH DI DOWNLOAD
                intent.putExtra("banner", bannerURL)
                startActivity(intent)
            }

        })

    }

    private fun choosePicture() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"), PICK_IMAGE_REQUEST)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.data != null
        ) {
            filePath = data.data
            try {
//                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)

                //INI SET txtViewBanner nya jadi nama file nya
                txtViewUploadBanner = uploadBannerTextView.text.toString()
                val imgName = MediaStore.Images.Media.DISPLAY_NAME.toString()
                txtName = name.text.toString()
                uploadBannerTextView.text = "$txtName.jpg"
//                profileUser!!.setImageBitmap(bitmap)
                //UPLAOD BANNERNYA
                uploadFile()

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadFile(): String {
        var imageURL: String = ""
        if (filePath != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading... ")
            progressDialog.show()

            val imageRef = storageReference!!.child("merchant_banner/" + UUID.randomUUID().toString())
            imageRef.putFile(filePath!!)
                    .addOnSuccessListener {

                        progressDialog.dismiss()
                        imageRef.downloadUrl.addOnSuccessListener {

                            bannerURL = it.toString()
                            Toast.makeText(applicationContext, "File Uploaded", Toast.LENGTH_SHORT).show()
                        }


                    }
                    .addOnFailureListener {
                        progressDialog.dismiss()
                        Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                    }
                    .addOnProgressListener { taskSnapShot ->
                        val progress =
                                100.00 * taskSnapShot.bytesTransferred / taskSnapShot.totalByteCount
                        progressDialog.setMessage("Uploaded " + progress.toInt() + "%... ")

                    }
        }
        return imageURL
    }

    fun init(){
        name = findViewById(R.id.edtMerchantRegisterName)
        address = findViewById(R.id.edtMerchantRegisterAddress)
        location = findViewById(R.id.edtMerchantRegisterLocation)
        phoneNumber = findViewById(R.id.edtMerchantRegisterPhoneNumber)
        uploadBanner = findViewById(R.id.btnMerchantRegisterUploadbanner)
        uploadBannerTextView = findViewById(R.id.tvMerchantRegisterBanner)
        nextBtn = findViewById(R.id.btnRegisterMerchantNext)
    }
}