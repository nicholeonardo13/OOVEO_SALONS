package edu.bluejack20_2.ooveo.homes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.R
import edu.bluejack20_2.ooveo.adapters.viewPagerAdapter
import edu.bluejack20_2.ooveo.fragments.*
import edu.bluejack20_2.ooveo.model.MerchantModel
import edu.bluejack20_2.ooveo.model.UserModel
import java.util.ArrayList

class HomeActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val homeFragment = HomeFragment()
    private val favouriteFragment = FavouriteFragment()
    private val appointmentFragment = AppointmentFragment()
    private val profileFragment = ProfileFragment()
    private val profileMerchantFragment = ProfileMerchantFragment()

    private lateinit var mAuth: FirebaseAuth
    private lateinit var userModel: UserModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        replacementFragment(homeFragment)

        mAuth = FirebaseAuth.getInstance()

        val docRef =  db.collection("users").document(mAuth.currentUser.uid.toString())
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        userModel = UserModel(
                                document.id.toString(),
                                document["role"].toString(),
                                document["name"].toString(),
                                document["phoneNumber"].toString(),
                                document["email"].toString(),
                                document["gender"].toString(),
                                document["dob"].toString(),
                                document["password"].toString(),
                                document["profilePicture"].toString(),
                                document["mode"].toString()
                        )

                        if(userModel.role == "Merchant"){
                            val bottom_navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
                            bottom_navigation.setOnNavigationItemSelectedListener {
                                when(it.itemId){
                                    R.id.ic_home -> replacementFragment(homeFragment)
                                    R.id.ic_favourite -> replacementFragment(favouriteFragment)
                                    R.id.ic_appointment -> replacementFragment(appointmentFragment)
                                    R.id.ic_profile -> replacementFragment(profileMerchantFragment)
                                }
                                true
                            }
                        }else {
                            val bottom_navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
                            bottom_navigation.setOnNavigationItemSelectedListener {
                                when(it.itemId){
                                    R.id.ic_home -> replacementFragment(homeFragment)
                                    R.id.ic_favourite -> replacementFragment(favouriteFragment)
                                    R.id.ic_appointment -> replacementFragment(appointmentFragment)
                                    R.id.ic_profile -> replacementFragment(profileFragment)
                                }
                                true
                            }
                        }

                    } else {
                        Log.d("GAGAL", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "get failed with ", exception)
                }




//        getAllMerchantData()

    }

    private fun replacementFragment(fragment : Fragment){
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_container, fragment)
            transaction.commit()
        }
    }


    fun setTab(){
        val adapter = viewPagerAdapter(supportFragmentManager)
        adapter.addFragment(HomeFragment() , getString(R.string.homeMenu))
        adapter.addFragment(FavouriteFragment() , getString(R.string.favMenu))
        adapter.addFragment(AppointmentFragment(), getString(R.string.appoinmentMenu))
        adapter.addFragment(ProfileFragment(), getString(R.string.profileMenu))
//        val viewPager = findViewById<ViewPager>(R.id.viewPager)
//        viewPager.adapter = adapter
//        val tabs = findViewById<TabLayout>(R.id.tabs)
//        tabs.setupWithViewPager(viewPager)

//        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_home_24)
//        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_favorite_24)
//        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_baseline_calendar_today_24)
//        tabs.getTabAt(3)!!.setIcon(R.drawable.ic_baseline_account_circle_24)

    }

//    private fun getAllMerchantData(){
//        db.collection("tests").get()
//            .addOnSuccessListener {
//                var listMerchantModel:ArrayList<MerchantModel> = ArrayList()
//                listMerchantModel.clear()
//
//                for (document in it){
//
//                    listMerchantModel.add(
//                        MerchantModel(
//                        document.id as String,
//                        document.data.get("akhir") as String,
//                            document.data.get("awal") as String,
//                    )
//                    )
//                }
//
//                var merchantAdapter = MerchantAdapter(listMerchantModel)
////                val recycleMerchants = findViewById<RecyclerView>(R.id.recycleMerchants)
////                   recycleMerchants.layoutManager = LinearLayoutManager(this)
////                    recycleMerchants.adapter = merchantAdapter
//
//            }
//            .addOnFailureListener{
//                Log.d("DB Error", "get failed with ")
//            }

    }
