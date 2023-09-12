package com.makaota.mammamskitchenmanager.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.makaota.mammamskitchenmanager.R
import com.makaota.mammamskitchenmanager.databinding.ActivitySettingsBinding
import com.makaota.mammamskitchenmanager.firestore.FirestoreClass
import com.makaota.mammamskitchenmanager.models.UserManager
import com.makaota.mammamskitchenmanager.utils.Constants

class SettingsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var mUserDetails: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSettingsActionBar()
        binding.btnLogout.setOnClickListener(this)
        binding.tvEdit.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                // Call the User Profile Activity to add the Edit Profile feature to the app. Pass the user details through intent.
                // START
                R.id.tv_edit -> {
                    val intent = Intent(this@SettingsActivity, UserProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intent)
                }
                // END

                // Add Logout feature when user clicks on logout button.
                // START
                R.id.btn_logout -> {

                    FirebaseAuth.getInstance().signOut()

                    val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                // END

//                R.id.ll_address ->{
//                    val intent = Intent(this@SettingsActivity, AddressListActivity::class.java)
//                    startActivity(intent)
//                }
            }
        }
    }

    private fun setupSettingsActionBar() {

        val toolbarSettingsActivity = findViewById<Toolbar>(R.id.toolbar_settings_activity)
        setSupportActionBar(toolbarSettingsActivity)


        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbarSettingsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    // Create a function to get the user details from firestore.
    // START
    /**
     * A function to get the user details from firestore.
     */
    private fun getUserDetails() {

        // Show the progress dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class to get the user details from firestore which is already created.
        FirestoreClass().getUserDetails(this@SettingsActivity)
    }

    fun userDetailsSuccess(user: UserManager) {

        mUserDetails = user
        // Set the user details to UI.
        // START
        // Hide the progress dialog
        hideProgressDialog()

        binding.tvName.text = "${user.firstName} ${user.lastName}"
        binding.tvEmail.text = user.email
        // END
    }


}