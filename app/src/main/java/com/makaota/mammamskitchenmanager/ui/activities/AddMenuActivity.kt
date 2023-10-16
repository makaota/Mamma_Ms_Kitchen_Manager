package com.makaota.mammamskitchenmanager.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.makaota.mammamskitchenmanager.R
import com.makaota.mammamskitchenmanager.databinding.ActivityAddMenuBinding
import com.makaota.mammamskitchenmanager.firestore.FirestoreClass
import com.makaota.mammamskitchenmanager.models.Product
import com.makaota.mammamskitchenmanager.utils.Constants
import com.makaota.mammamskitchenmanager.utils.GlideLoader
import java.io.IOException

class AddMenuActivity : BaseActivity(), View.OnClickListener {

    private lateinit var selectedCategoryItem: String
    lateinit var binding: ActivityAddMenuBinding
    private var mSelectedImageFileUri: Uri? = null
    private var mProductImageURL: String = ""
    private var mProductDetails: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAddMenuActivityActionBar()


        if (intent.hasExtra(Constants.EXTRA_PRODUCT_DETAILS)) {
            // Get the product details from intent as a ParcelableExtra.
            mProductDetails = intent.getParcelableExtra(Constants.EXTRA_PRODUCT_DETAILS)!!
        }

        if (mProductDetails != null){

            binding.btnSubmit.visibility = View.GONE
            binding.btnUpdate.visibility = View.VISIBLE

            binding.tvTitle.text = "EDIT PRODUCT"

            binding.etProductTitle.setText(mProductDetails!!.title)
            binding.actvMenuCategory.setText(mProductDetails!!.category)
            binding.menu.isEnabled = false
            binding.etProductPrice.setText(mProductDetails!!.price)
            binding.etProductDescription.setText(mProductDetails!!.description)
            binding.etProductQuantity.setText(mProductDetails!!.stock_quantity)
            mProductDetails!!.product_id
            // Load the image using the GlideLoader class with the use of Glide Library.
            GlideLoader(this@AddMenuActivity).loadProductPicture(mProductDetails!!.image,binding.ivProductImage)

        }

        binding.ivAddUpdateProduct.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
        binding.btnUpdate.setOnClickListener(this)

        // List of Menu category to be selected
        val menuCategory = listOf(
            Constants.SCAMBANE, Constants.CHIPS, Constants.RUSSIAN, Constants.ADDITIONAL_MEALS,
            Constants.DRINKS
        )

        val adapter = ArrayAdapter(this, R.layout.category_list_items, menuCategory)
        val autoCompleteTextView = binding.actvMenuCategory

        autoCompleteTextView.setAdapter(adapter)

        autoCompleteTextView.setOnItemClickListener { parent, view, position, l ->

            selectedCategoryItem = parent.getItemAtPosition(position).toString()

        }

    }

    private fun updateProductDetails(){

        val productHashMap = HashMap<String, Any>()


        val productTile = binding.etProductTitle.text.toString().trim { it <= ' ' }
        val productPrice = binding.etProductPrice.text.toString().trim { it <= ' ' }
        val productDescription = binding.etProductDescription.text.toString().trim { it <= ' ' }
        val productStockQuantity = binding.etProductQuantity.text.toString().trim { it <= ' ' }

        if (productTile != mProductDetails!!.title){
            productHashMap[Constants.PRODUCT_TITLE] = productTile
        }

        if (productPrice != mProductDetails!!.price){
            productHashMap[Constants.PRODUCT_PRICE] = productPrice
        }

        if (productDescription != mProductDetails!!.description){
            productHashMap[Constants.PRODUCT_DESCRIPTION] = productDescription
        }

        if (productStockQuantity != mProductDetails!!.stock_quantity){
            productHashMap[Constants.STOCK_QUANTITY] = productStockQuantity
        }


        if (mProductImageURL.isNotEmpty()){
            productHashMap[Constants.IMAGE] = mProductImageURL
        }

        FirestoreClass().updateProductDetails(this, productHashMap, mProductDetails!!)

    }

    fun productDetailsUpdateSuccess() {
        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this@AddMenuActivity,
            "Product Updated Successfully",
            Toast.LENGTH_SHORT
        ).show()


        // Redirect to the Main Screen after profile completion.
        startActivity(Intent(this@AddMenuActivity, DashboardActivity::class.java))
        finish()
    }


    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                // The permission code is similar to the user profile image selection.
                R.id.iv_add_update_product -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showImageChooser(this@AddMenuActivity)
                    } else {
                        /*Requests permissions to be granted to this application. These permissions
                         must be requested in your manifest, they should not be granted to your app,
                         and they should have protection level*/
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }
                R.id.btn_submit -> {
                    if (validateProductDetails()) {
                        uploadProductImage()
                    }
                }
                R.id.btn_update ->{
                    showProgressDialog(resources.getString(R.string.please_wait))
                    updateProductDetails()
                }
            }
        }
    }
    /**
     * A function to validate the product details.
     */
    private fun validateProductDetails(): Boolean {
        return when {

            mSelectedImageFileUri == null -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_select_product_image), true)
                false
            }

            TextUtils.isEmpty(binding.etProductTitle.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_title), true)
                false
            }

            TextUtils.isEmpty(binding.actvMenuCategory.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_category), true)
                false
            }


            TextUtils.isEmpty(binding.etProductPrice.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_price), true)
                false
            }

            TextUtils.isEmpty(binding.etProductDescription.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_product_description),
                    true
                )
                false
            }

            else -> {
                true
            }
        }
    }

    /**
     * This function will identify the result of runtime permission after the user allows or deny permission based on the unique code.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this@AddMenuActivity)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == Constants.PICK_IMAGE_REQUEST_CODE
            && data!!.data != null
        ) {
            // Replace the add icon with edit icon once the image is selected.
            binding.ivAddUpdateProduct.setImageDrawable(
                ContextCompat.getDrawable(
                    this@AddMenuActivity,
                    R.drawable.ic_vector_edit
                )
            )

            // The uri of selection image from phone storage.
            mSelectedImageFileUri = data.data!!
            try {
                // Load the product image in the ImageView.
                GlideLoader(this@AddMenuActivity).loadProductPicture(
                    mSelectedImageFileUri!!,
                    binding.ivProductImage
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection.
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }


    /**
     * A function for actionBar Setup.
     */
    private fun setupAddMenuActivityActionBar() {

        setSupportActionBar(binding.toolbarAddProductActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarAddProductActivity.setNavigationOnClickListener { onBackPressed() }
    }

    fun imageUploadSuccess(imageURL: String) {
        mProductImageURL = imageURL
        //hideProgressDialog()
        showErrorSnackBar("Product Image is uploaded successfully: $imageURL",false)
        uploadProductDetails()
    }

    private fun uploadProductDetails() {

        // Get the logged in username from the SharedPreferences that we have stored at a time of login.
        val username = this.getSharedPreferences(
            Constants.USER_MANAGER_PREFERENCES,
            Context.MODE_PRIVATE
        )
            .getString(Constants.LOGGED_IN_USERNAME, "")!!


        // Here we get the text from editText and trim the space
        val product = Product(
            user_id = FirestoreClass().getCurrentUserId(),
            user_name = username,
            title = binding.etProductTitle.text.toString().trim { it <= ' ' },
            category = selectedCategoryItem,
            price = binding.etProductPrice.text.toString().trim { it <= ' ' },
            description = binding.etProductDescription.text.toString().trim { it <= ' ' },
            stock_quantity = binding.etProductQuantity.text.toString().trim{ it <= ' '},
            image = mProductImageURL,
        )

        FirestoreClass().uploadProductDetails(this@AddMenuActivity, product)


    }

    /**
     * A function to return the successful result of Product upload.
     */
    fun productUploadSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this@AddMenuActivity,
            resources.getString(R.string.product_uploaded_success_message),
            Toast.LENGTH_SHORT
        ).show()


        finish()
    }


    /**
     * A function to upload the selected product image to firebase cloud storage.
     */
    private fun uploadProductImage() {

        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().uploadImageToCloudStorage(
            this@AddMenuActivity,
            mSelectedImageFileUri,
            Constants.PRODUCT_IMAGE
        )
    }



}