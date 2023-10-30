package com.makaota.mammamskitchenmanager.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.makaota.mammamskitchenmanager.R
import com.makaota.mammamskitchenmanager.databinding.ActivityOpenCloseStoreBinding
import com.makaota.mammamskitchenmanager.firestore.FirestoreClass
import com.makaota.mammamskitchenmanager.models.OpenCloseStore
import com.makaota.mammamskitchenmanager.models.Product
import com.makaota.mammamskitchenmanager.ui.fragments.ManageMenuFragment
import com.makaota.mammamskitchenmanager.utils.Constants

class OpenCloseStoreActivity : BaseActivity() {

    private val TAG = "OpenCloseStoreActivity"

    private val docRef = "KUadjV036C6fvZrjmIWn"

    lateinit var binding: ActivityOpenCloseStoreBinding

    private val mFirestore = FirebaseFirestore.getInstance()

    private lateinit var mOpenCloseStore: OpenCloseStore
    private var isStoreOpen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenCloseStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()



        mOpenCloseStore = OpenCloseStore(false)

     //   openCloseStoreCollection(mOpenCloseStore)

        getOpenCloseStoreInfo()

        binding.btnOpenClose.setOnClickListener {

            if (binding.btnOpenClose.text == "Open Store"){


                showProgressDialog(resources.getString(R.string.please_wait))

                val db = Firebase.firestore
                val batch = db.batch()
                // change the value of mOpenCloseStore
                isStoreOpen = true

                val documentRef = db.collection(Constants.OPEN_CLOSE_STORE)
                    .document(docRef)
                // Update the value of the order status
                batch.update(documentRef, "storeOpen", isStoreOpen)
                Log.i(TAG, "isStoreOpen = ${isStoreOpen}")
                batch.commit()
                    .addOnSuccessListener {
                        // Update successful
                     //   hideProgressDialog()

                    }
                    .addOnFailureListener { e ->
                        // Handle error
                      //  hideProgressDialog()
                    }

                hideProgressDialog()
               // onBackPressed()

                Toast.makeText(this,"Store is Open",Toast.LENGTH_SHORT).show()

                binding.btnOpenClose.text = "Close Store"


            }else{

                showProgressDialog(resources.getString(R.string.please_wait))

                val db = Firebase.firestore
                val batch = db.batch()
                // change the value of mOpenCloseStore
                isStoreOpen = false

                val documentRef = db.collection(Constants.OPEN_CLOSE_STORE)
                    .document(docRef)
                // Update the value of the order status
                batch.update(documentRef, "storeOpen", isStoreOpen)
                Log.i(TAG, "isStoreOpen = ${isStoreOpen}")
                batch.commit()
                    .addOnSuccessListener {
                        // Update successful
                        //   hideProgressDialog()

                    }
                    .addOnFailureListener { e ->
                        // Handle error
                        //  hideProgressDialog()
                    }

                hideProgressDialog()
                // onBackPressed()


                Toast.makeText(this,"Store is Closed",Toast.LENGTH_SHORT).show()

                binding.btnOpenClose.text = "Open Store"

            }
        }
    }

    private fun openCloseStoreCollection(openCloseStore: OpenCloseStore){

        showProgressDialog(resources.getString(R.string.please_wait))

        // The "users" is collection name. If the collection is already created then it will not create the same one again.
        mFirestore.collection(Constants.OPEN_CLOSE_STORE)
            .document()
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge later on instead of replacing the fields.
            .set(openCloseStore)
            .addOnSuccessListener {

                hideProgressDialog()

            }
            .addOnFailureListener {

                hideProgressDialog()
            }

    }

   private fun getOpenCloseStoreInfo() {
        // The collection name for OPEN CLOSE STORE
        mFirestore.collection(Constants.OPEN_CLOSE_STORE)
            .document(docRef)
            .get() // Will get the document snapshots.
            .addOnSuccessListener { document ->

                // Here we get the product details in the form of document.
                Log.e(javaClass.simpleName, document.toString())

                // Convert the snapshot to the object of open close store data model class.
                val openCloseStore = document.toObject(OpenCloseStore::class.java)!!

                mOpenCloseStore = openCloseStore

                if (!openCloseStore.isStoreOpen){
                    binding.btnOpenClose.text = "Open Store"
                }
                else{
                    binding.btnOpenClose.text = "Close Store"
                }

            }
            .addOnFailureListener { e ->

            }
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarOpenCloseStoreActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarOpenCloseStoreActivity.setNavigationOnClickListener { onBackPressed() }
    }
}