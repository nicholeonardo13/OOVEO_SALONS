package edu.bluejack20_2.ooveo

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import edu.bluejack20_2.ooveo.model.MerchantModel
import edu.bluejack20_2.ooveo.model.StylistModel
import java.io.IOException

class EditMerchantActivity : AppCompatActivity() {

    private lateinit var saveChangesBtn: Button
    private lateinit var textName: EditText
    private lateinit var textAddress: EditText
    private lateinit var textPhoneNumber: EditText
    private lateinit var profileUser: ImageView

    private lateinit var currentPhotoProfile: String

    private var filePath: Uri? = null
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private val PICK_IMAGE_REQUEST = 1234

    private lateinit var merchant: MerchantModel
    private lateinit var db: FirebaseFirestore
    private lateinit var edtProfilePicture: String
    private lateinit var ids : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_merchant)


        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
        db = FirebaseFirestore.getInstance()

        ids = intent.extras?.getString("id").toString()
        var ownerID = intent.extras?.getString("ownerID")

        saveChangesBtn = findViewById(R.id.btnUpdateMerchantSaveChanges)
        textName = findViewById(R.id.edtUpdateMerchantName)
        textAddress = findViewById(R.id.edtUpdateMerchantAddress)
        textPhoneNumber = findViewById(R.id.edtUpdateMerchantPhoneNumber)
        profileUser = findViewById(R.id.ivUpdateMerchantImage)

        var merchantIDS = ""


        val docRef = db.collection("merchants").document(ids)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    merchant = MerchantModel(
                        document.id.toString(),
                        document["name"].toString(),
                        document["address"].toString(),
                        document["image"].toString(),
                        document["phoneNumber"].toString(),
                        document["location"].toString(),
                        document["type"].toString(),
                        document["about"].toString(),
                    )
                    textName.setText(merchant.name)
                    textAddress.setText(merchant.address)
                    textPhoneNumber.setText(merchant.phoneNumber)


                    currentPhotoProfile = merchant.image

                    val requestOption = RequestOptions()
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)

                    Glide.with(this)
                        .applyDefaultRequestOptions(requestOption)
                        .load(currentPhotoProfile)
                        .into(profileUser)
                    Log.d("TAMPILIN DATA", "DocumentSnapshot data: ${document.data}")

                } else {
                    Log.d("GAGAL", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }

        profileUser!!.setOnClickListener(View.OnClickListener {
            choosePicture()
        })

        saveChangesBtn!!.setOnClickListener(View.OnClickListener { taskSnapshot ->
            var txtName: String = textName.text.toString()
            var txtAddress: String = textAddress.text.toString()
            var txtPhoneNumber: String = textPhoneNumber.text.toString()


            if(txtName.isEmpty()){
                Toast.makeText(this, getString(R.string.nameHarusDiisi), Toast.LENGTH_SHORT).show()
            }else if(txtName.length < 3 ){
                Toast.makeText(this, getString(R.string.nameHarusLebihdaritiga), Toast.LENGTH_SHORT).show()
            }else if(txtAddress.equals("")){
                Toast.makeText(this, getString(R.string.addressHarusDiisi), Toast.LENGTH_SHORT).show()
            }else if(txtPhoneNumber.length < 12) {
                Toast.makeText(this, getString(R.string.phoneNumberharus12), Toast.LENGTH_SHORT).show()
            }else  {
                updateDataUser(txtName, txtAddress , txtPhoneNumber)
                val intent = Intent(this, MerchantAdminActivity::class.java)
                intent.putExtra("ownerID", ownerID)
                startActivity(intent)
                finish()
            }
        })
    }


    private fun choosePicture() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                profileUser!!.setImageBitmap(bitmap)
                //LGSG UPLOAD AJA GPP, KARENA NAMANYA GA RANDOM JADI BAKAL KE OVERRIDE HARUSNYA
                edtProfilePicture = uploadFile()

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

            val imageRef = storageReference!!.child("merchant_image/" + ids)
            imageRef.putFile(filePath!!)
                .addOnSuccessListener {

                    progressDialog.dismiss()
                    imageRef.downloadUrl.addOnSuccessListener {

                        currentPhotoProfile = it.toString()
                    }


                    Toast.makeText(applicationContext, getString(R.string.fileKeUpload), Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, getString(R.string.failedText), Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener { taskSnapShot ->
                    val progress =
                        100.00 * taskSnapShot.bytesTransferred / taskSnapShot.totalByteCount
                    progressDialog.setMessage(getString(R.string.keUploaded) + progress.toInt() + "%... ")

                }
        }
        return imageURL
    }


    private fun updateDataUser(edtName: String, address: String , phoneNumber : String) {
        val userRef = db.collection("merchants").document(ids)
        //KALO EMAIL GA DI GANTI, GAUSAH GANTI phone numbernya

        userRef.update(
            "name", edtName, "address", address , "phoneNumber" , phoneNumber ,  "image", currentPhotoProfile

        ).addOnSuccessListener {
            Log.d("SUKSES", "DocumentSnapshot successfully updated!")
            Toast.makeText(this, getString(R.string.suksesSave), Toast.LENGTH_SHORT).show()

        }
            .addOnFailureListener { e ->
                Log.w("ERROR", "Error updating document", e)
                Toast.makeText(this, getString(R.string.saveErrors), Toast.LENGTH_SHORT).show()
            }
    }
}