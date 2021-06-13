package edu.bluejack20_2.ooveo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class AddStylistActivity : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    private lateinit var ids : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_stylist)

        ids = intent.extras?.getString("id").toString()

        db = FirebaseFirestore.getInstance()

        var photopict = "";
        val btnAddNewStylist = findViewById<Button>(R.id.btnAddNewStylist)
        val edtStylistName = findViewById<EditText>(R.id.edtAddStylistName)
        val rbFemale = findViewById<RadioButton>(R.id.rbRegisterFemaleStylist)
        val rbMale = findViewById<RadioButton>(R.id.rbRegisterMaleStylist)


        btnAddNewStylist.setOnClickListener {

            var txtName = edtStylistName.text.toString()

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
            }else {

                if(gender == "Female"){
                    photopict = "https://firebasestorage.googleapis.com/v0/b/ooveo-966be.appspot.com/o/user%2Ffemale.png?alt=media&token=4bf1aa52-5e88-4048-ba9d-ee01c7e5c096"
                }else{
                    photopict = "https://firebasestorage.googleapis.com/v0/b/ooveo-966be.appspot.com/o/user%2Fmale.png?alt=media&token=88d1c191-a741-4ae6-a625-418397b03dc0"
                }


                addNewStylist(txtName , gender , photopict)
                edtStylistName.setText("")


                var intent = Intent(this@AddStylistActivity, ManageServiceActivity::class.java)
                intent.putExtra("id", ids)
                startActivity(intent)
                finish()

            }



        }
    }

    private fun addNewStylist(name : String , gender : String , photo : String) {
        val stylist : MutableMap<String, Any> = HashMap()

        stylist["name"] = name
        stylist["gender"] = gender
        stylist["profilePicture"] = photo
        stylist["merchantID"] = ids
//        stylist["startHour"] = ""
//        stylist["endHour"] = ""

        db.collection("stylists").add(stylist)
            .addOnSuccessListener {
                Toast.makeText(this@AddStylistActivity , "Berhasil" , Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this@AddStylistActivity , "GAGAL" , Toast.LENGTH_SHORT).show()
            }
    }
}