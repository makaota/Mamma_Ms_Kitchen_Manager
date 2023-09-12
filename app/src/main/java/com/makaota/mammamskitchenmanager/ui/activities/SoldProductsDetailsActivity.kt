package com.makaota.mammamskitchenmanager.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.makaota.mammamskitchenmanager.R
import com.makaota.mammamskitchenmanager.databinding.ActivitySoldProductsDetailsBinding
import com.makaota.mammamskitchenmanager.models.SoldProduct
import com.makaota.mammamskitchenmanager.utils.Constants
import com.makaota.mammamskitchenmanager.utils.GlideLoader
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SoldProductsDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivitySoldProductsDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoldProductsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Receive the sold product details through intent.
        // START
        var productDetails: SoldProduct = SoldProduct()

        if (intent.hasExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS)) {
            productDetails = intent.getParcelableExtra<SoldProduct>(Constants.EXTRA_SOLD_PRODUCT_DETAILS)!!
        }
        // END
        // Call the function to setup action bar.
        // START
        setupActionBar()
        // END

        // Call the function to populate the data in UI.
        // START
        setupUI(productDetails)
        // END
    }

    // Create a function to setup action bar.
    // START
    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarSoldProductDetailsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarSoldProductDetailsActivity.setNavigationOnClickListener { onBackPressed() }
    }
    // END

    // Create a function to setupUI.
    // START
    /**
     * A function to setup UI.
     *
     * @param productDetails Order details received through intent.
     */
    private fun setupUI(productDetails: SoldProduct) {

        binding.tvSoldProductDetailsId.text = productDetails.order_id

        // Date Format in which the date will be displayed in the UI.
        val dateFormat = "dd MMM yyyy HH:mm"
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = productDetails.order_date
        binding.tvSoldProductDetailsDate.text = formatter.format(calendar.time)

        GlideLoader(this@SoldProductsDetailsActivity).loadProductPicture(
            productDetails.image,
            binding.ivProductItemImage
        )
        binding.tvProductItemName.text = productDetails.title
        binding.tvProductItemPrice.text ="R${productDetails.price}"
        binding.tvSoldProductQuantity.text = productDetails.sold_quantity

        binding.tvSoldProductSubTotal.text = productDetails.sub_total_amount
        binding.tvSoldProductShippingCharge.text = productDetails.shipping_charge
        binding.tvSoldProductTotalAmount.text = productDetails.total_amount
    }
    // END
}
// END