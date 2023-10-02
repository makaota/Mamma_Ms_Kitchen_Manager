package com.makaota.mammamskitchenmanager.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.makaota.mammamskitchenmanager.models.Order
import com.makaota.mammamskitchenmanager.models.Product
import com.makaota.mammamskitchenmanager.models.SoldProduct
import com.makaota.mammamskitchenmanager.models.UserManager
import com.makaota.mammamskitchenmanager.ui.activities.AddMenuActivity
import com.makaota.mammamskitchenmanager.ui.activities.LoginActivity
import com.makaota.mammamskitchenmanager.ui.activities.MyOrderDetailsActivity
import com.makaota.mammamskitchenmanager.ui.activities.ProductDetailsActivity
import com.makaota.mammamskitchenmanager.ui.activities.RegisterActivity
import com.makaota.mammamskitchenmanager.ui.activities.SettingsActivity
import com.makaota.mammamskitchenmanager.ui.activities.UserProfileActivity
import com.makaota.mammamskitchenmanager.ui.fragments.ManageMenuFragment
import com.makaota.mammamskitchenmanager.ui.fragments.ManageOrdersFragment
import com.makaota.mammamskitchenmanager.ui.fragments.SoldProductsFragment
import com.makaota.mammamskitchenmanager.utils.Constants

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()
    private lateinit var documentId : String
    private lateinit var generatedId: String

    // START
    /**
     * A function to make an entry of the registered user in the FireStore database.
     */
    fun registerUser(activity: RegisterActivity, userInfo: UserManager) {

        // The "users" is collection name. If the collection is already created then it will not create the same one again.
        mFirestore.collection(Constants.USER_MANAGER)
            // Document ID for users fields. Here the document it is the User ID.
            .document(userInfo.id)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge later on instead of replacing the fields.
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    /**
     * A function to get the user id of current logged user.
     */
    fun getCurrentUserId(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }


    /**
     * A function to get the logged user details from from FireStore Database.
     */
    fun getUserDetails(activity: Activity) {

        // Here we pass the collection name from which we wants the data.
        mFirestore.collection(Constants.USER_MANAGER)
            // The document id to get the Fields of user.
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->


                Log.i(activity.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object.
                val userManager = document.toObject(UserManager::class.java)!!

                if (userManager != null && userManager is UserManager){

                    // Create an instance of the Android SharedPreferences.
                    // START
                    val sharedPreferences =
                        activity.getSharedPreferences(
                            Constants.USER_MANAGER_PREFERENCES,
                            Context.MODE_PRIVATE
                        )

                    // Create an instance of the editor which is help us to edit the SharedPreference.
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString(
                        Constants.LOGGED_IN_USERNAME,
                        "${userManager.firstName} ${userManager.lastName}"
                    )
                    editor.apply()
                    // END

                    // START
                    when (activity) {
                        is LoginActivity -> {
                            // Call a function of base activity for transferring the result to it.
                            activity.userLoggedInSuccess(userManager)
                        }
                        is SettingsActivity -> {
                            activity.userDetailsSuccess(userManager)
                        }
                    }
                    // END
                }else{
                    Log.d("TAG","Document object does not match the expected UserManager class")
                }

            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }

                    is SettingsActivity -> {
                        activity.hideProgressDialog()
                    }


                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.", e
                )
            }
    }

    // Create a function to update user details in the database.
    // START
    /**
     * A function to update the user profile data into the database.
     *
     * @param activity The activity is used for identifying the Base activity to which the result is passed.
     * @param userHashMap HashMap of fields which are to be updated.
     */
    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {
        // Collection Name
        mFirestore.collection(Constants.USER_MANAGER)
            // Document ID against which the data to be updated. Here the document id is the current logged in user id.
            .document(getCurrentUserId())
            // A HashMap of fields which are to be updated.
            .update(userHashMap)
            .addOnSuccessListener {

                // Notify the success result to the base activity.
                // START
                // Notify the success result.
                when (activity) {
                    is UserProfileActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.userProfileUpdateSuccess()
                    }
                }
                // END
            }
            .addOnFailureListener { e ->

                when (activity) {
                    is UserProfileActivity -> {
                        // Hide the progress dialog if there is any error. And print the error in log.
                        activity.hideProgressDialog()
                    }

                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }
    // END

    // Create a function to update user details in the database.
    // START
    /**
     * A function to update the user profile data into the database.
     *
     * @param activity The activity is used for identifying the Base activity to which the result is passed.
     * @param userHashMap HashMap of fields which are to be updated.
     */
    fun updateProductDetails(activity: Activity, userHashMap: HashMap<String, Any>, product: Product) {
        // Collection Name
        mFirestore.collection(Constants.PRODUCTS)
            // Document ID against which the data to be updated. Here the document id is the product_id.
            .document(product.product_id)
            // A HashMap of fields which are to be updated.
            .update(userHashMap)
            .addOnSuccessListener {

                // Notify the success result to the base activity.
                // START
                // Notify the success result.
                when (activity) {
                    is AddMenuActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.productDetailsUpdateSuccess()
                    }
                }
                // END
            }
            .addOnFailureListener { e ->

                when (activity) {
                    is AddMenuActivity -> {
                        // Hide the progress dialog if there is any error. And print the error in log.
                        activity.hideProgressDialog()
                    }

                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }
    // END

    // Create a function to upload the image to the Cloud Storage.
    // START
    // A function to upload the image to the cloud storage.
    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType: String) {

        //getting the storage reference
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(
                activity,
                imageFileURI
            )
        )

        //adding the file to reference
        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener { taskSnapshot ->
                // The image upload is success
                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                // Get the downloadable url from the task snapshot
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image URL", uri.toString())

                        // Pass the success result to base class.
                        // START
                        // Here call a function of base activity for transferring the result to it.
                        when (activity) {
                            is AddMenuActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                        }
                        // END
                    }
            }
            .addOnFailureListener { exception ->

                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is AddMenuActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }
    // END


    /**
     * A function to make an entry of the user's product in the cloud firestore database.
     */
    fun uploadProductDetails(activity: AddMenuActivity, productInfo: Product) {

        mFirestore.collection(Constants.PRODUCTS)
            //.document()
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .add(productInfo)
            .addOnSuccessListener {
                generatedId = it.id
                productInfo.product_id = generatedId
                Log.i("ID",productInfo.product_id)

                // Here call a function of base activity for transferring the result to it.
                activity.productUploadSuccess()
            }
            .addOnFailureListener { e ->

                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading the product details.",
                    e
                )
            }
    }


    /**
     * A function to get the products list from cloud firestore.
     *
     * @param fragment The fragment is passed as parameter as the function is called from fragment and need to the success result.
     */
    fun getProductsList(fragment: Fragment) {
        // The collection name for PRODUCTS
        mFirestore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserId())
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of boards in the form of documents.
                Log.e("Products List", document.documents.toString())

                // Here we have created a new instance for Products ArrayList.
                val productsList: ArrayList<Product> = ArrayList()

                // A for loop as per the list of documents to convert them into Products ArrayList.
                for (i in document.documents) {

                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    productsList.add(product)
                }

                when (fragment) {
                    is ManageMenuFragment -> {
                        fragment.successProductsListFromFireStore(productsList)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error based on the base class instance.
                when (fragment) {
                    is ManageMenuFragment -> {
                        fragment.hideProgressDialog()
                    }
                }

                Log.e("Get Product List", "Error while getting product list.", e)
            }
    }

    // Create a function to delete the product from the cloud firestore.
    /**
     * A function to delete the product from the cloud firestore.
     */
    fun deleteProduct(fragment: ManageMenuFragment, productId: String) {

        mFirestore.collection(Constants.PRODUCTS)
            .document(productId)
            .delete()
            .addOnSuccessListener {

                // Notify the success result to the base class.
                // START
                // Notify the success result to the base class.
                fragment.productDeleteSuccess()
                // END
            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is an error.
                fragment.hideProgressDialog()

                Log.e(
                    fragment.requireActivity().javaClass.simpleName,
                    "Error while deleting the product.",
                    e
                )
            }
    }
    // END

    // Create a function to get the product details based on the product id.
    // START
    /**
     * A function to get the product details based on the product id.
     */
    fun getProductDetails(activity: ProductDetailsActivity, productId: String) {

        // The collection name for PRODUCTS
        mFirestore.collection(Constants.PRODUCTS)
            .document(productId)
            .get() // Will get the document snapshots.
            .addOnSuccessListener { document ->

                // Here we get the product details in the form of document.
                Log.e(activity.javaClass.simpleName, document.toString())

                // Convert the snapshot to the object of Product data model class.
                val product = document.toObject(Product::class.java)!!

                // Notify the success result.
                // START
                activity.productDetailsSuccess(product)
                // END
            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is an error.
                activity.hideProgressDialog()

                Log.e(activity.javaClass.simpleName, "Error while getting the product details.", e)
            }
    }
    // END

    // Create a function to get the list of orders from cloud firestore.
    // START
    /**
     * A function to get the list of orders from cloud firestore.
     */
    fun getMyOrdersList(fragment: ManageOrdersFragment) {
        mFirestore.collection(Constants.ORDERS)
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->
                Log.e(fragment.javaClass.simpleName, document.documents.toString())
                val list: ArrayList<Order> = ArrayList()

                for (i in document.documents) {

                    val orderItem = i.toObject(Order::class.java)!!
                    orderItem.id = i.id

                    list.add(orderItem)
                }

                // Notify the success result to base class.
                // START
                fragment.populateOrdersListInUI(list)
                Log.i("TAG","Code good "+getCurrentUserId())
                // END
            }
            .addOnFailureListener { e ->
                // Here call a function of base activity for transferring the result to it.

                fragment.hideProgressDialog()

                Log.e(fragment.javaClass.simpleName, "Error while getting the orders list.", e)
            }
    }
    // END

    fun addSoldProducts(activity: MyOrderDetailsActivity, orderDetails: Order){


        // Collection name address.
        mFirestore.collection(Constants.SOLD_PRODUCTS)
            .document()
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(orderDetails, SetOptions.merge())
            .addOnSuccessListener {

                // Notify the success result to the base class.
                // START
                // Here call a function of base activity for transferring the result to it.
                activity.addSoldProductsSuccess()
                // END
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while adding the sold products.",
                    e
                )
            }

    }

    // Create a function to get the list of sold products.
    // START
    /**
     * A function to get the list of sold products from the cloud firestore.
     *
     *  @param fragment Base class
     */
    fun getSoldProductsList(fragment: SoldProductsFragment) {
        // The collection name for SOLD PRODUCTS
        mFirestore.collection(Constants.SOLD_PRODUCTS)
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->
                // Here we get the list of sold products in the form of documents.
                Log.e(fragment.javaClass.simpleName, document.documents.toString())

                // Here we have created a new instance for Sold Products ArrayList.
                val list: ArrayList<SoldProduct> = ArrayList()

                // A for loop as per the list of documents to convert them into Sold Products ArrayList.
                for (i in document.documents) {

                    val soldProduct = i.toObject(SoldProduct::class.java)!!
                    soldProduct.id = i.id

                    list.add(soldProduct)
                }

                // Notify the success result to the base class.
                // START
                fragment.successSoldProductsList(list)
                // END
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error.
                fragment.hideProgressDialog()

                Log.e(
                    fragment.javaClass.simpleName,
                    "Error while getting the list of sold products.",
                    e
                )
            }
    }
    // END

}