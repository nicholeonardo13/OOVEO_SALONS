package edu.bluejack20_2.ooveo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.homes.HomeActivity

class ReviewActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    //Spinner things
    private lateinit var autoCompeleteTv: AutoCompleteTextView
    private lateinit var typeSpinner: TextInputLayout

    private lateinit var serviceNameTv: TextView
    private lateinit var stylistNameTv: TextView
    private lateinit var rangeTimeTv: TextView
    private lateinit var totalPriceTv: TextView
    private lateinit var feedbackEdt: EditText
    private lateinit var submitReview: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        init()
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        //Spinner things
        val arrayTypes = arrayOf(resources.getStringArray(R.array.rating))
        val rating = resources.getStringArray(R.array.rating)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item_type, rating)
        autoCompeleteTv.setAdapter(arrayAdapter)

        val cartID = intent.getStringExtra("cartID").toString()
        val serviceName = intent.getStringExtra("serviceName").toString()
        val stylistName = intent.getStringExtra("stylistName").toString()
        val rangeTime = intent.getStringExtra("rangeTime").toString()
        val totalPrice = intent.getStringExtra("totalPrice").toString()

        serviceNameTv.text = serviceName
        stylistNameTv.text = stylistName
        rangeTimeTv.text = rangeTime
        totalPriceTv.text = totalPrice

        submitReview.setOnClickListener(View.OnClickListener {
            //Get value from Spinner
            val intent = Intent(this, HomeActivity::class.java)
            val ratingValue = autoCompeleteTv.text.toString().toInt()
            val feedbackTxt = feedbackEdt.text.toString()

            if(autoCompeleteTv.text.toString().isEmpty()){
                Toast.makeText(this, "Please choose Rating", Toast.LENGTH_SHORT).show()
            }else if(feedbackTxt.isEmpty()){
                Toast.makeText(this, "Please insert Feedback", Toast.LENGTH_SHORT).show()
            }else{
                val review: MutableMap<String, Any> = HashMap()
                review["cartID"] = db.collection("carts").document(cartID)
                review["feedback"] = feedbackTxt
                review["rating"] = ratingValue as Number

                db.collection("reviews").add(review).addOnSuccessListener {
                    db.collection("carts").document(cartID).update(
                        "review_status", "yes"
                    ).addOnSuccessListener {
                        Log.wtf("Update carts review status", "success")
                        startActivity(intent)
                        finish()
                    }

                    Toast.makeText(this, "Add Review Success", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to Add Review, Try later", Toast.LENGTH_SHORT).show()
                }
            }



        })

    }

    fun init(){
        serviceNameTv = findViewById(R.id.tvReviewServiceName)
        stylistNameTv = findViewById(R.id.tvReviewStylistName)
        rangeTimeTv = findViewById(R.id.tvReviewRangeTime)
        totalPriceTv = findViewById(R.id.tvReviewPrice)
        feedbackEdt = findViewById(R.id.edtReviewFeedback)
        submitReview = findViewById(R.id.btnReviewSubmit)
        autoCompeleteTv = findViewById(R.id.autoCompleteReviewTextView)
    }

}