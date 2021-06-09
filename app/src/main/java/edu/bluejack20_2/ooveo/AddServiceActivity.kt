package edu.bluejack20_2.ooveo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class AddServiceActivity : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    private lateinit var ids : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service)

        ids = intent.extras?.getString("id").toString()

        db = FirebaseFirestore.getInstance()

        val btnAddNewService = findViewById<Button>(R.id.btnAddNewService)
        val edtServiceName = findViewById<EditText>(R.id.edtAddServiceName)
        val edtServicePrice = findViewById<EditText>(R.id.edtAddServicePrice)
        val edtServiceDesc = findViewById<EditText>(R.id.edtAddServiceDescription)

        btnAddNewService.setOnClickListener {
            val serviceName = edtServiceName.text.toString()
            val servicePrice = edtServicePrice.text.toString()
            val serviceDesc = edtServiceDesc.text.toString()

            if(serviceName.isEmpty()){
                Toast.makeText(this, "Name must be filled!", Toast.LENGTH_SHORT).show()
            }else if(servicePrice.isEmpty() || Integer.parseInt(servicePrice) >  0){
                Toast.makeText(this, "Price must be filled!", Toast.LENGTH_SHORT).show()
            }else if(serviceDesc.isEmpty()){
                Toast.makeText(this, "Description must be filled!", Toast.LENGTH_SHORT).show()
            }else {

                val price = servicePrice.toLong()
                addNewService(serviceName , price , serviceDesc)

                edtServiceName.setText("")
                edtServicePrice.setText("")
                edtServiceDesc.setText("")

                var intent = Intent(this@AddServiceActivity, ManageServiceActivity::class.java)
                intent.putExtra("id", ids)
                startActivity(intent)
                finish()

            }
        }

    }

    private fun addNewService(name : String , price : Long , desc : String) {
        val service : MutableMap<String, Any> = HashMap()

        service["name"] = name
        service["price"] = price
        service["description"] = desc
        service["merchantID"] = ids

        db.collection("services").add(service)
            .addOnSuccessListener {
                Toast.makeText(this@AddServiceActivity , "Berhasil" , Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this@AddServiceActivity , "GAGAL" , Toast.LENGTH_SHORT).show()
            }
    }
}