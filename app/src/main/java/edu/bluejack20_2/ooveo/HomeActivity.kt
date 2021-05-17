package edu.bluejack20_2.ooveo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.fragments.AppointmentFragment
import edu.bluejack20_2.ooveo.fragments.FavouriteFragment
import edu.bluejack20_2.ooveo.fragments.HomeFragment
import edu.bluejack20_2.ooveo.fragments.ProfileFragment
import edu.bluejack20_2.ooveo.fragments.adapters.viewPagerAdapter

class HomeActivity : AppCompatActivity() {


    private val db = FirebaseFirestore.getInstance()
    private val homeFragment = HomeFragment()
    private val favouriteFragment = FavouriteFragment()
    private val appointmentFragment = AppointmentFragment()
    private val profileFragment = ProfileFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

//        readData()
//        setTab()
        replacementFragment(homeFragment)

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
        adapter.addFragment(HomeFragment() , "Home")
        adapter.addFragment(FavouriteFragment() , "Favourite")
        adapter.addFragment(AppointmentFragment(), "Appointment")
        adapter.addFragment(ProfileFragment(), "Profile")
//        val viewPager = findViewById<ViewPager>(R.id.viewPager)
//        viewPager.adapter = adapter
//        val tabs = findViewById<TabLayout>(R.id.tabs)
//        tabs.setupWithViewPager(viewPager)

//        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_home_24)
//        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_favorite_24)
//        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_baseline_calendar_today_24)
//        tabs.getTabAt(3)!!.setIcon(R.drawable.ic_baseline_account_circle_24)

    }

    fun readData(){
//        FirebaseApp.initializeApp();


        val docRef = db.collection("tests").get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d("Ada", "DocumentSnapshot data: ${document.documents}")
                    } else {
                        Log.d("Gada", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("DB Error", "get failed with ", exception)
                }

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
