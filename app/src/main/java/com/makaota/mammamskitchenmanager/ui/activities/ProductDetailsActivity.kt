package com.makaota.mammamskitchenmanager.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import com.makaota.mammamskitchenmanager.R
import com.makaota.mammamskitchenmanager.databinding.ActivityProductDetailsBinding
import com.makaota.mammamskitchenmanager.firestore.FirestoreClass
import com.makaota.mammamskitchenmanager.models.Product
import com.makaota.mammamskitchenmanager.utils.Constants
import com.makaota.mammamskitchenmanager.utils.GlideLoader

class ProductDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityProductDetailsBinding

    private lateinit var mProductDetails: Product
    private var mProductId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.edit_menu_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.action_edit_menu -> {
                        val intent = Intent(this@ProductDetailsActivity, AddMenuActivity::class.java)
                        intent.putExtra(Constants.EXTRA_PRODUCT_DETAILS, mProductDetails)
                        startActivity(intent)

                        return true
                    }
                }
                return true
            }

        })

        setupMenuDetailsActivityActionBar()

        // Get the product id through the intent extra and print it in the log.
        // START
        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
            Log.i("Product Id", mProductId)
        }
        // END

        getProductDetails()

    }

    // Create a function to notify the success result of the product details based on the product id.
    // START
    /**
     * A function to notify the success result of the product details based on the product id.
     *
     * @param product A model class with product details.
     */
    fun productDetailsSuccess(product: Product) {

        mProductDetails = product
        // Hide Progress dialog.
        hideProgressDialog()

        // Populate the product details in the UI.
        GlideLoader(this@ProductDetailsActivity).loadProductPicture(
            product.image,
            binding.ivProductDetailImage
        )

        binding.tvProductDetailsTitle.text = product.title
        binding.tvProductDetailsPrice.text = "R${product.price}"
        binding.tvProductDetailsDescription.text = product.description
        binding.tvProductDetailsAvailableQuantity.text = product.stock_quantity

    }

    // Create a function to call the firestore class function that will get the product details from cloud firestore based on the product id.
    // START
    /**
     * A function to call the firestore class function that will get the product details from cloud firestore based on the product id.
     */
    private fun getProductDetails() {

        // Show the product dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of FirestoreClass to get the product details.
        FirestoreClass().getProductDetails(this@ProductDetailsActivity, mProductId)
    }
    // END

    private fun setupMenuDetailsActivityActionBar() {

        val toolbarSettingsActivity = findViewById<Toolbar>(R.id.toolbar_product_details_activity)
        setSupportActionBar(toolbarSettingsActivity)


        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbarSettingsActivity.setNavigationOnClickListener { onBackPressed() }
    }
}