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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.SignInMethodQueryResult
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import edu.bluejack20_2.ooveo.homes.HomeActivity
import edu.bluejack20_2.ooveo.model.StylistModel
import edu.bluejack20_2.ooveo.model.UserModel
import java.io.IOException

class UpdateStylistActivity : AppCompatActivity() {


    private lateinit var saveChangesBtn: Button
    private lateinit var textName: EditText
    private lateinit var profileUser: ImageView

    private lateinit var currentPhotoProfile: String

    private var filePath: Uri? = null
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private val PICK_IMAGE_REQUEST = 1234

    private lateinit var stylist: StylistModel
    private lateinit var db: FirebaseFirestore
    private lateinit var edtProfilePicture: String
    private lateinit var ids : String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_stylist)

        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
        db = FirebaseFirestore.getInstance()

        ids = intent.extras?.getString("id").toString()

        saveChangesBtn = findViewById(R.id.btnUpdateStylistSaveChanges)
        textName = findViewById(R.id.edtUpdateStylistName)
        profileUser = findViewById(R.id.ivUpdateStylistImage)
        val rbFemale = findViewById<RadioButton>(R.id.rbUpdateFemaleStylist)
        val rbMale = findViewById<RadioButton>(R.id.rbUpdaterMaleStylist)

        var merchantIDS = ""


        val docRef = db.collection("stylists").document(ids)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    stylist = StylistModel(
                        document.id.toString(),
                        document["name"].toString(),
                        document["gender"].toString(),
                        document["profilePicture"].toString(),
                        document["merchantID"].toString(),
                    )
                    textName.setText(stylist.name)

                    if(stylist.gender.equals("Male")){
                        rbMale.isSelected
                    }else{
                        rbFemale.isSelected
                    }

                    currentPhotoProfile = stylist.profilePicture
                    merchantIDS = stylist.merchantID

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

            var gender: String = ""
            if(rbFemale.isChecked){
                gender = "Female"
            }else if (rbMale.isChecked){
                gender = "Male"
            }

            if(txtName.isEmpty()){
                Toast.makeText(this, "Name must be filled!", Toast.LENGTH_SHORT).show()
            }else if(txtName.length < 3 ){
                Toast.makeText(this, "Name must be more than 3 character!", Toast.LENGTH_SHORT).show()
            }else if(!rbFemale.isChecked && !rbMale.isChecked) {
                Toast.makeText(this, "Gender must be selected!", Toast.LENGTH_SHORT).show()
            }else  {
                updateDataUser(txtName, gender)
                val intent = Intent(this, ManageStylistActivity::class.java)
                intent.putExtra("id", merchantIDS)
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

            val imageRef = storageReference!!.child("stylist/" + ids)
            imageRef.putFile(filePath!!)
                .addOnSuccessListener {

                    progressDialog.dismiss()
                    imageRef.downloadUrl.addOnSuccessListener {

                        currentPhotoProfile = it.toString()
                    }


                    Toast.makeText(applicationContext, "File Uploaded", Toast.LENGTH_SHORT).show()
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


    private fun updateDataUser(edtName: String, gender: String) {
        val userRef = db.collection("stylists").document(ids)
        //KALO EMAIL GA DI GANTI, GAUSAH GANTI phone numbernya

            userRef.update(
                "name", edtName, "gender", gender ,  "profilePicture", currentPhotoProfile

            ).addOnSuccessListener {
                Log.d("SUKSES", "DocumentSnapshot successfully updated!")
                Toast.makeText(this, "Success save changes", Toast.LENGTH_SHORT).show()

            }
                .addOnFailureListener { e ->
                    Log.w("ERROR", "Error updating document", e)
                    Toast.makeText(this, "Failed save changes", Toast.LENGTH_SHORT).show()
                }
        }
    }