package com.makaota.mammamskitchenmanager.ui.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.makaota.mammamskitchenmanager.R
import com.makaota.mammamskitchenmanager.databinding.ActivityUserProfileBinding
import com.makaota.mammamskitchenmanager.firestore.FirestoreClass
import com.makaota.mammamskitchenmanager.models.UserManager
import com.makaota.mammamskitchenmanager.utils.Constants

class UserProfileActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var mUserDetails: UserManager
    private  var mSelectedImageFileUri: Uri? = null
    private var mUserProfileImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Retrieve the User details from intent extra.
        // START
        // Create a instance of the User model class.

        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            // Get the user details from intent as a ParcelableExtra.
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }
        // END
        binding.etFirstName.setText(mUserDetails.firstName)
        binding.etLastName.setText(mUserDetails.lastName)
        binding.etEmail.isEnabled = false
        binding.etEmail.setText(mUserDetails.email)

        // If the profile is incomplete then user is from login screen and wants to complete the profile.
        if (mUserDetails.profileCompleted == 0) {
            // Update the title of the screen to complete profile.
            binding.tvTitle.text = resources.getString(R.string.title_complete_profile)

            // Here, the some of the edittext components are disabled because it is added at a time of Registration.
            binding.etFirstName.isEnabled = false
            binding.etLastName.isEnabled = false

        } else {

            // Call the setup action bar function.
            setupUserProfileActionBar()

            // Update the title of the screen to edit profile.
            binding.tvTitle.text = resources.getString(R.string.title_edit_profile)
        }

        binding.btnSubmit.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                // Add the click event to the SAVE button.
                // START
                R.id.btn_submit ->{
                    showProgressDialog(resources.getString(R.string.please_wait))
                    updateUserProfileDetails()
                }

            } // END
        }
    } // END

    private fun updateUserProfileDetails(){

        val userHashMap = HashMap<String, Any>()

        // Update the code if user is about to Edit Profile details instead of Complete Profile.
        // Get the FirstName from editText and trim the space
        val firstName = binding.etFirstName.text.toString().trim { it <= ' ' }
        if (firstName != mUserDetails.firstName) {
            userHashMap[Constants.FIRST_NAME] = firstName
        }

        // Get the LastName from editText and trim the space
        val lastName = binding.etLastName.text.toString().trim { it <= ' ' }
        if (lastName != mUserDetails.lastName) {
            userHashMap[Constants.LAST_NAME] = lastName
        }

        userHashMap[Constants.COMPLETE_PROFILE] = 1


        FirestoreClass().updateUserProfileData(this, userHashMap)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // showErrorSnackBar("The storage permission is granted.", false)

                Constants.showImageChooser(this)

            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
//                if (data != null) {
//                    try {
//                        // The uri of selected image from phone storage.
//                        mSelectedImageFileUri = data.data!!
//
//
//                        //  binding.ivUserPhoto.setImageURI(Uri.parse(selectedImageFileUri.toString()))
//                        GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!, binding.ivUserPhoto)
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                        Toast.makeText(
//                            this@UserProfileActivity,
//                            resources.getString(R.string.image_selection_failed),
//                            Toast.LENGTH_SHORT
//                        )
//                            .show()
//                    }
//                }
//            }
//        } else if (resultCode == Activity.RESULT_CANCELED) {
//            // A log is printed when user close or cancel the image selection.
//            Log.e("Request Cancelled", "Image selection cancelled")
//        }
//    }

    private fun setupUserProfileActionBar() {

        val toolbarRegisterActivity = findViewById<Toolbar>(R.id.toolbar_user_profile_activity)
        setSupportActionBar(toolbarRegisterActivity)


        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbarRegisterActivity.setNavigationOnClickListener { onBackPressed() }
    } // END

    // Create a function to validate the input entries for profile details.
    // START
    /**
     * A function to validate the input entries for profile details.
     */
//    private fun validateUserProfileDetails(): Boolean {
//        return when {
//
//            // We have kept the user profile picture is optional.
//            // The FirstName, LastName, and Email Id are not editable when they come from the login screen.
//            // The Radio button for Gender always has the default selected value.
//
//            // Check if the mobile number is not empty as it is mandatory to enter.
//            TextUtils.isEmpty(binding.etMobileNumber.text.toString().trim { it <= ' ' }) -> {
//                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
//                false
//            }
//            else -> {
//                true
//            }
//        }
//    }

    fun userProfileUpdateSuccess() {
        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this@UserProfileActivity,
            resources.getString(R.string.msg_profile_update_success),
            Toast.LENGTH_SHORT
        ).show()


        // Redirect to the Main Screen after profile completion.
        startActivity(Intent(this@UserProfileActivity, LoginActivity::class.java))
        finish()
    }

    // Create a function to notify the success result of image upload to the Cloud Storage.
    // START
    /**
     * A function to notify the success result of image upload to the Cloud Storage.
     *
     * @param imageURL After successful upload the Firebase Cloud returns the URL.
     */
    fun imageUploadSuccess(imageURL: String) {

        // Hide the progress dialog
        // hideProgressDialog()
        mUserProfileImageURL = imageURL
        updateUserProfileDetails()
    }
    // END

}