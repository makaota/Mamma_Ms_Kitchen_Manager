package com.makaota.mammamskitchenmanager.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.makaota.mammamskitchenmanager.R
import com.makaota.mammamskitchenmanager.databinding.ActivityMyOrderDetailsBinding
import com.makaota.mammamskitchenmanager.firestore.FirestoreClass
import com.makaota.mammamskitchenmanager.models.NotificationData
import com.makaota.mammamskitchenmanager.models.Notifications
import com.makaota.mammamskitchenmanager.models.Order
import com.makaota.mammamskitchenmanager.models.PushNotification
import com.makaota.mammamskitchenmanager.models.User
import com.makaota.mammamskitchenmanager.ui.adapters.CartItemsListAdapter
import com.makaota.mammamskitchenmanager.utils.Constants
import com.makaota.mammamskitchenmanager.utils.RetrofitInstance
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

const val TOPIC = "myOrders"
const val TAG = "MyOrderDetailsActivity"

class MyOrderDetailsActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityMyOrderDetailsBinding
    lateinit var myOrderDetails: Order
    private var userToken = ""

    lateinit var mOrderStatus: String
    lateinit var mOrderNumber: String

    lateinit var title: String
    lateinit var message: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        // Get the order details through intent.
        // START
        myOrderDetails = Order()
        if (intent.hasExtra(Constants.EXTRA_MY_ORDER_DETAILS)) {
            myOrderDetails = intent.getParcelableExtra<Order>(Constants.EXTRA_MY_ORDER_DETAILS)!!
        }
        // END

        mOrderStatus = myOrderDetails.orderStatus
        mOrderNumber = myOrderDetails.orderNumber

        if (mOrderStatus == resources.getString(R.string.order_status_pending)) {
            binding.btnNotifyUserOrderReceived.visibility = View.VISIBLE


        }

        if (mOrderStatus == resources.getString(R.string.order_status_in_process)) {
            binding.btnNotifyUserOrderReceived.visibility = View.GONE
            binding.btnSendOrderNumber.visibility = View.VISIBLE
            binding.tilOrderNumber.visibility = View.VISIBLE
        }

        if (mOrderStatus == resources.getString(R.string.order_status_preparing)) {
            binding.btnReadyForCollection.visibility = View.VISIBLE

            binding.llOrderNumber.visibility = View.VISIBLE
            binding.tvOrderNumber.text = mOrderNumber

        }

        if (mOrderStatus == resources.getString(R.string.order_status_ready_for_collection)) {
            binding.btnDelivered.visibility = View.VISIBLE
            binding.llOrderNumber.visibility = View.VISIBLE
            binding.tvOrderNumber.text = mOrderNumber

        }

        if (mOrderStatus == resources.getString(R.string.order_status_delivered)) {
            binding.llOrderNumber.visibility = View.VISIBLE
            binding.tvOrderNumber.text = mOrderNumber

        }



        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        setupUI(myOrderDetails)

        binding.btnNotifyUserOrderReceived.setOnClickListener(this)
        binding.btnSendOrderNumber.setOnClickListener(this)
        binding.btnReadyForCollection.setOnClickListener(this)
        binding.btnDelivered.setOnClickListener(this)
        binding.btnDeleteOrder.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_notify_user_order_received -> {


                showProgressDialog(resources.getString(R.string.please_wait))

                val db = Firebase.firestore
                val batch = db.batch()
                // change the value of mOrderStatus
                mOrderStatus = resources.getString(R.string.order_status_in_process)


                val documentRef = db.collection(Constants.ORDERS)
                    .document(myOrderDetails.id)
                // Update the value of the order status
                batch.update(documentRef, Constants.ORDER_STATUS, mOrderStatus)
                batch.update(documentRef, Constants.USER_MANAGER_ID, FirestoreClass().getCurrentUserId())
                Log.i(TAG, "orders status is = $mOrderStatus")
                Log.i(TAG, "document ID is = ${myOrderDetails.id}")
                batch.commit()
                    .addOnSuccessListener {
                        // Update successful
                        //hideProgressDialog()

                    }
                    .addOnFailureListener { e ->
                        // Handle error
                        //hideProgressDialog()
                    }
                sendOrderNotificationToUser()
                hideProgressDialog()
                onBackPressed()
            }

            R.id.btn_send_order_number -> {


                if (validateOrderNumberDetails()) {

                    showProgressDialog(resources.getString(R.string.please_wait))
                    val db = Firebase.firestore
                    val batch = db.batch()
                    // change the value of mOrderStatus
                    mOrderStatus = resources.getString(R.string.order_status_preparing)
                    mOrderNumber = binding.etOrderNumber.text.toString()

                    binding.llOrderNumber.visibility = View.VISIBLE
                    binding.tvOrderNumber.text = mOrderNumber

                    val documentRef = db.collection(Constants.ORDERS)
                        .document(myOrderDetails.id)
                    // Update the value of the order status
                    batch.update(documentRef, Constants.ORDER_STATUS, mOrderStatus)
                    batch.update(documentRef, "orderNumber", mOrderNumber)
                    Log.i(TAG, "orders status is = $mOrderStatus")
                    Log.i(TAG, "document id is = ${myOrderDetails.id}")
                    batch.commit()
                        .addOnSuccessListener {
                            // Update successful

                            //hideProgressDialog()
                        }
                        .addOnFailureListener { e ->
                            // Handle error
                            // hideProgressDialog()
                        }
                    sendOrderNotificationToUser()
                    hideProgressDialog()
                    onBackPressed()

                }
            }

            R.id.btn_ready_for_collection -> {

                showProgressDialog(resources.getString(R.string.please_wait))

                val db = Firebase.firestore
                val batch = db.batch()
                // change the value of mOrderStatus
                mOrderStatus = resources.getString(R.string.order_status_ready_for_collection)

                val documentRef = db.collection(Constants.ORDERS)
                    .document(myOrderDetails.id)
                // Update the value of the order status
                batch.update(documentRef, Constants.ORDER_STATUS, mOrderStatus)
                Log.i(TAG, "orders status is = $mOrderStatus")
                Log.i(TAG, "document ID is = ${myOrderDetails.id}")
                batch.commit()
                    .addOnSuccessListener {
                        // Update successful
                        //hideProgressDialog()

                    }
                    .addOnFailureListener { e ->
                        // Handle error
                        //hideProgressDialog()
                    }

                sendOrderNotificationToUser()
                hideProgressDialog()
                onBackPressed()
            }

            R.id.btn_delivered -> {
                showProgressDialog(resources.getString(R.string.please_wait))

                val db = Firebase.firestore
                val batch = db.batch()
                // change the value of mOrderStatus
                mOrderStatus = resources.getString(R.string.order_status_delivered)

                val documentRef = db.collection(Constants.ORDERS)
                    .document(myOrderDetails.id)
                // Update the value of the order status
                batch.update(documentRef, Constants.ORDER_STATUS, mOrderStatus)
                Log.i(TAG, "orders status is = $mOrderStatus")
                Log.i(TAG, "document ID is = ${myOrderDetails.id}")
                batch.commit()
                    .addOnSuccessListener {
                        // Update successful
                        //hideProgressDialog()
                        // Add Sold Product to firestore
                        FirestoreClass().addSoldProducts(this,myOrderDetails)
                    }
                    .addOnFailureListener { e ->
                        // Handle error
                        //hideProgressDialog()
                    }

                sendOrderNotificationToUser()

                hideProgressDialog()
                onBackPressed()
            }

            R.id.btn_delete_order ->{

                deleteDeliveredOrder(myOrderDetails.id)
            }
        }
    }

    private fun deleteDeliveredOrder(orderId: String) {

        showAlertDialogToDeleteDeliveredOrder(orderId)
    }

    private fun showAlertDialogToDeleteDeliveredOrder(orderId: String) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle(resources.getString(R.string.delete_dialog_title))
        //set message for alert dialog
        builder.setMessage(resources.getString(R.string.delete_order_dialog_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->

            // Call the function to delete the product from cloud firestore.
            // START
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            // Call the function of Firestore class.
            FirestoreClass().deleteDeliveredOrder(this, orderId)
            // END

            dialogInterface.dismiss()
        }

        //performing negative action
        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->

            dialogInterface.dismiss()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
    // END

    // Create a function to notify the success result of product deleted from cloud firestore.
    // START
    /**
     * A function to notify the success result of product deleted from cloud firestore.
     */
    fun orderDeleteSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this,
            resources.getString(R.string.order_delete_success_message),
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }
    // END


    // Create a function to validate the order number details.
    // START
    /**
     * A function to validate the order number entries of a user.
     */
    private fun validateOrderNumberDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etOrderNumber.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_order_number), true)
                false
            }
            else -> {
                showErrorSnackBar("Your details are valid.", false)
                true
            }
        }
    }


    private fun sendOrderNotificationToUser() {
        val userCollection = FirebaseFirestore.getInstance().collection(Constants.USER)

        Log.i(TAG, "user id = ${myOrderDetails.user_id}")
        userCollection.document(myOrderDetails.user_id).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(User::class.java)
                    // Extract user information here
                    userToken = user?.userToken.toString()



                    when {
                        mOrderStatus == resources.getString(R.string.order_status_in_process) -> {

                            title = myOrderDetails.title
                            message = "Your Order has been received Please Confirm the order to " +
                                    "Continue"

                            val notifications = Notifications(
                                title = title,
                                orderDateTime = myOrderDetails.order_datetime,
                                orderStatus = mOrderStatus,
                                orderMessage = message,
                                orderConfirmed = myOrderDetails.order_confirmation,
                                orderNumber = mOrderNumber,
                                user_id = myOrderDetails.user_id
                            )

                            FirestoreClass().uploadNotificationsDetails(this, notifications)

                        }

                        mOrderStatus == resources.getString(R.string.order_status_preparing) -> {

                            title = myOrderDetails.title
                            message = "Your Order is being prepared check the order number"

                            val notifications = Notifications(
                                title = title,
                                orderDateTime = myOrderDetails.order_datetime,
                                orderStatus = mOrderStatus,
                                orderMessage = message,
                                orderConfirmed = myOrderDetails.order_confirmation,
                                orderNumber = mOrderNumber,
                                user_id = myOrderDetails.user_id
                            )

                            FirestoreClass().uploadNotificationsDetails(this, notifications)

                        }

                        mOrderStatus == resources.getString(R.string.order_status_ready_for_collection) -> {

                            title = myOrderDetails.title
                            message = "Your Order is ready for collection"

                            val notifications = Notifications(
                                title = title,
                                orderDateTime = myOrderDetails.order_datetime,
                                orderStatus = mOrderStatus,
                                orderMessage = message,
                                orderConfirmed = myOrderDetails.order_confirmation,
                                orderNumber = mOrderNumber,
                                user_id = myOrderDetails.user_id
                            )

                            FirestoreClass().uploadNotificationsDetails(this, notifications)

                        }

                        mOrderStatus == resources.getString(R.string.order_status_delivered) -> {

                            title = myOrderDetails.title
                            message = "Your Order is delivered thank you very much for using this app to make your order "

                            val notifications = Notifications(
                                title = title,
                                orderDateTime = myOrderDetails.order_datetime,
                                orderStatus = mOrderStatus,
                                orderMessage = message,
                                orderConfirmed = myOrderDetails.order_confirmation,
                                orderNumber = mOrderNumber,
                                user_id = myOrderDetails.user_id
                            )

                            FirestoreClass().uploadNotificationsDetails(this, notifications)

                        }
                    }
                    // END


                    PushNotification(NotificationData(title, message), userToken).also {
                        sendOrderNotification(it)
                        Log.i(TAG, "${title}, $message user Token sent notification = $userToken")
                    }

                    Log.i(TAG, "user Token = $userToken")
                } else {
                    // User document does not exist
                }
            }
            .addOnFailureListener { exception ->
                // Error occurred while fetching user data
            }
    }

    fun notificationsUploadSuccess(){

        //  hideProgressDialog()

        FancyToast.makeText(
            this,
            "Notifications Info Uploaded successfully.",
            FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true
        )
            .show()
    }

    private fun sendOrderNotification(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.postNotification(notification)
                if (response.isSuccessful) {
                    Log.i(TAG, "Response: ${Gson().toJson(response)}")
                } else {
                    Log.i(TAG, "not successful ${response.errorBody().toString()}")

                }

            } catch (e: Exception) {
                Log.i(TAG, "Exception found $e")
            }

        }


    // Create a function to setup UI.
    // START
    /**
     * A function to setup UI.
     *
     * @param orderDetails Order details received through intent.
     */
    private fun setupUI(orderDetails: Order) {

        binding.tvOrderDetailsId.text = orderDetails.title

        // Set the Date in the UI.
        // START
        // Date Format in which the date will be displayed in the UI.
        val dateFormat = "dd MMM yyyy HH:mm"
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = orderDetails.order_datetime

        val orderDateTime = formatter.format(calendar.time)
        binding.tvOrderDetailsDate.text = orderDateTime
        // END

        // Set the order status based on the time for now.
        // START
        // Get the difference between the order date time and current date time in hours.
        // If the difference in hours is 1 or less then the order status will be PENDING.
        // If the difference in hours is 2 or greater then 1 then the order status will be PROCESSING.
        // And, if the difference in hours is 3 or greater then the order status will be DELIVERED.

        val diffInMilliSeconds: Long = System.currentTimeMillis() - orderDetails.order_datetime
        val diffInHours: Long = TimeUnit.MILLISECONDS.toHours(diffInMilliSeconds)
        Log.e("Difference in Hours", "$diffInHours")

        when {
            mOrderStatus == resources.getString(R.string.order_status_pending) -> {
                binding.tvOrderStatus.setTextColor(
                    ContextCompat.getColor(
                        this@MyOrderDetailsActivity,
                        R.color.colorAccent
                    )
                )
                binding.tvOrderStatus.text = resources.getString(R.string.order_status_pending)
            }


            mOrderStatus == resources.getString(R.string.order_status_in_process) -> {
                binding.tvOrderStatus.setTextColor(
                    ContextCompat.getColor(
                        this@MyOrderDetailsActivity,
                        R.color.colorOrderStatusInProcess
                    )
                )
                binding.tvOrderStatus.text = resources.getString(R.string.order_status_in_process)
            }

            mOrderStatus == resources.getString(R.string.order_status_preparing) -> {
                binding.tvOrderStatus.setTextColor(
                    ContextCompat.getColor(
                        this@MyOrderDetailsActivity,
                        R.color.colorOrderStatusPreparing
                    )
                )
                binding.tvOrderStatus.text = resources.getString(R.string.order_status_preparing)
            }

            mOrderStatus == resources.getString(R.string.order_status_ready_for_collection) -> {
                binding.tvOrderStatus.setTextColor(
                    ContextCompat.getColor(
                        this@MyOrderDetailsActivity,
                        R.color.colorOrderStatusReadyForCollection
                    )
                )
                binding.tvOrderStatus.text =
                    resources.getString(R.string.order_status_ready_for_collection)
            }

            mOrderStatus == resources.getString(R.string.order_status_delivered) -> {
                binding.tvOrderStatus.setTextColor(
                    ContextCompat.getColor(
                        this@MyOrderDetailsActivity,
                        R.color.colorOrderStatusDelivered
                    )
                )
                binding.tvOrderStatus.text = resources.getString(R.string.order_status_delivered)


            }
        }
        // END

        binding.rvMyOrderItemsList.layoutManager = LinearLayoutManager(this@MyOrderDetailsActivity)
        binding.rvMyOrderItemsList.setHasFixedSize(true)

        val cartListAdapter =
            CartItemsListAdapter(this@MyOrderDetailsActivity, orderDetails.items, false)
        binding.rvMyOrderItemsList.adapter = cartListAdapter


        if (myOrderDetails.address.type == ""){
            binding.tvSelectedAddress.text = "Customer to Pickup"
            binding.llCheckoutAddressDetails.visibility = View.GONE
        }
        else{
            binding.tvCheckoutAddressType.text = myOrderDetails.address.type
            binding.tvCheckoutFullName.text = myOrderDetails.address.name
            binding.tvCheckoutAddress.text = myOrderDetails.address.address
            binding.tvCheckoutAdditionalNote.text = myOrderDetails.address.additionalNote
            binding.tvCheckoutOtherDetails.text = myOrderDetails.address.otherDetails
            binding.tvCheckoutMobileNumber.text = myOrderDetails.address.mobileNumber
        }



        binding.tvMyOrderDetailsFullName.text = myOrderDetails.userName

        if (orderDetails.order_confirmation == ""){
            binding.tvOrderConfirmationText.text = "No"

            binding.etOrderNumber.isEnabled = false
            binding.btnSendOrderNumber.isEnabled  = false

        }else{
            binding.tvOrderConfirmationText.text = orderDetails.order_confirmation
        }

        binding.tvMyOrderDetailsMobileNumber.text = myOrderDetails.userMobile

        binding.tvOrderDetailsSubTotal.text = orderDetails.sub_total_amount
        binding.tvOrderDetailsShippingCharge.text = orderDetails.shipping_charge
        binding.tvOrderDetailsTotalAmount.text = orderDetails.total_amount
    }
    // END

    fun addSoldProductsSuccess(){

       // hideProgressDialog()

        FancyToast.makeText(this,
            "Sold Products Added Successfully",
            FancyToast.LENGTH_SHORT,
            FancyToast.SUCCESS,
            true).show()
    }


    // Create a function to setup action bar.
    // START
    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarMyOrderDetailsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarMyOrderDetailsActivity.setNavigationOnClickListener { onBackPressed() }
    }


}