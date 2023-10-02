package com.makaota.mammamskitchenmanager.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.makaota.mammamskitchenmanager.R
import com.makaota.mammamskitchenmanager.databinding.ActivitySoldProductsDetailsBinding
import com.makaota.mammamskitchenmanager.models.SoldProduct
import com.makaota.mammamskitchenmanager.ui.adapters.CartItemsListAdapter
import com.makaota.mammamskitchenmanager.ui.adapters.SoldProductsListAdapter
import com.makaota.mammamskitchenmanager.utils.Constants
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class SoldProductsDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivitySoldProductsDetailsBinding
    private lateinit var mSoldProductDetails: SoldProduct
    private lateinit var mOrderStatus: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoldProductsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Receive the sold product details through intent.
        // START
        mSoldProductDetails = SoldProduct()

        if (intent.hasExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS)) {
            mSoldProductDetails = intent.getParcelableExtra<SoldProduct>(Constants.EXTRA_SOLD_PRODUCT_DETAILS)!!
        }
        // END
        // Call the function to setup action bar.
        // START
        setupActionBar()
        // END

        // Call the function to populate the data in UI.
        // START
        setupUI(mSoldProductDetails)
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
    private fun setupUI(soldProductDetails: SoldProduct) {

        binding.tvOrderDetailsId.text = soldProductDetails.title

        // Set the Date in the UI.
        // START
        // Date Format in which the date will be displayed in the UI.
        val dateFormat = "dd MMM yyyy HH:mm"
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = soldProductDetails.order_date

        val orderDateTime = formatter.format(calendar.time)
        binding.tvOrderDetailsDate.text = orderDateTime
        // END

        // Set the order status based on the time for now.
        // START
        // Get the difference between the order date time and current date time in hours.
        // If the difference in hours is 1 or less then the order status will be PENDING.
        // If the difference in hours is 2 or greater then 1 then the order status will be PROCESSING.
        // And, if the difference in hours is 3 or greater then the order status will be DELIVERED.

        val diffInMilliSeconds: Long = System.currentTimeMillis() - soldProductDetails.order_date
        val diffInHours: Long = TimeUnit.MILLISECONDS.toHours(diffInMilliSeconds)
        Log.e("Difference in Hours", "$diffInHours")

             mOrderStatus = resources.getString(R.string.order_status_delivered)
             binding.tvOrderStatus.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorOrderStatusDelivered
                    )
                )
                binding.tvOrderStatus.text = mOrderStatus



        // END

        binding.rvMyOrderItemsList.layoutManager = LinearLayoutManager(this)
        binding.rvMyOrderItemsList.setHasFixedSize(true)

        val cartListAdapter =
            CartItemsListAdapter(this, soldProductDetails.items, false)
        binding.rvMyOrderItemsList.adapter = cartListAdapter


        if (soldProductDetails.address.type == ""){
            binding.tvSelectedAddress.text = "Customer to Pickup"
            binding.llCheckoutAddressDetails.visibility = View.GONE
        }
        else{
            binding.tvCheckoutAddressType.text = soldProductDetails.address.type
            binding.tvCheckoutFullName.text = soldProductDetails.address.name
            binding.tvCheckoutAddress.text = soldProductDetails.address.address
            binding.tvCheckoutAdditionalNote.text = soldProductDetails.address.additionalNote
            binding.tvCheckoutOtherDetails.text = soldProductDetails.address.otherDetails
            binding.tvCheckoutMobileNumber.text = soldProductDetails.address.mobileNumber
        }



        binding.tvMyOrderDetailsFullName.text = soldProductDetails.userName

        binding.tvMyOrderDetailsMobileNumber.text = soldProductDetails.userMobile


        binding.tvOrderDetailsSubTotal.text = soldProductDetails.sub_total_amount
        binding.tvOrderDetailsShippingCharge.text = soldProductDetails.shipping_charge
        binding.tvOrderDetailsTotalAmount.text = soldProductDetails.total_amount
    }
    // END

}
// END