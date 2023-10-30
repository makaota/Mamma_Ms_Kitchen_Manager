package com.makaota.mammamskitchenmanager.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    const val USER_MANAGER_ID = "user_manager_id"
    const val BASE_URL = "https://fcm.googleapis.com"
    const val SERVER_KEY = "AAAA6WuBVzg:APA91bG42BqsBYwZ9UHeROrVNaWFE6A1Bl2b8eMDFpWsE3MI4MaLY1PLbGd7R9YIj6ysR4FoIwMmO8fSXALcKAB78RU4eRVBGGz4zjcr6c8Xm9IVE0wZ0O21KgFE_yk_XALjg5JfQ6db"
    const val CONTENT_TYPE = "application/json"

    // Firebase Constants
    // This is used for the collection in the firestore.
    const val USER_MANAGER: String = "userManager"
    const val USER: String = "user"
    const val PRODUCTS: String = "products"
    const val ORDERS: String = "orders"
    const val SOLD_PRODUCTS: String = "sold_products"
    const val NOTIFICATIONS: String = "notifications"
    const val OPEN_CLOSE_STORE: String = "openCloseStore"

    const val ORDER_STATUS: String = "orderStatus"

    const val CART_ITEMS: String = "cart_items"


    //menu categories
    const val ADDITIONAL_MEALS: String = "ADDITIONAL MEALS"
    const val SCAMBANE: String = "SCAMBANE"
    const val CHIPS: String = "CHIPS"
    const val RUSSIAN: String= "RUSSIAN"
    const val DRINKS: String = "DRINKS"


    const val PLATTERS: String = "platters"
    const val PIZZA: String = "pizza"
    const val PASTA: String = "pasta"
    const val BEEF: String = "beef"
    const val CHICKEN: String = "chicken"
    const val PORK: String = "pork"
    const val FISH: String = "fish"
    const val BURGERS: String = "burgers"
    const val SUBS: String = "subs"
    const val TRAMEZZINI: String = "tramezzini"
    const val TOASTED_SANDWICH: String = "toasted_sandwich"
    const val ROLLS: String = "rolls"
    const val SIDES: String = "sides"
    const val SALADS: String = "salads"


    // Product Constants

    const val PRODUCT_TITLE = "title"
    const val PRODUCT_PRICE = "price"
    const val PRODUCT_DESCRIPTION = "description"
    const val STOCK_QUANTITY: String = "stock_quantity"

    const val USER_MANAGER_PREFERENCES: String = "userManagerPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS: String = "extra_user_details"

    const val READ_STORAGE_PERMISSION_CODE: Int = 2
    const val PICK_IMAGE_REQUEST_CODE = 1
    const val PICK_PRODUCT_IMAGE_REQUEST_CODE = 1
    // Constant variables for Gender
    const val MALE: String = "male"
    const val FEMALE: String = "female"
    // Firebase database field names
    const val MOBILE: String = "mobile"
    const val GENDER: String = "gender"
    const val IMAGE: String = "image"
    const val COMPLETE_PROFILE: String = "profileCompleted"
    const val FIRST_NAME: String = "firstName"
    const val LAST_NAME: String = "lastName"
    const val USER_ID: String = "user_id"
    const val PRODUCT_ID: String = "product_id"
    //
    const val USER_RESTAURANT_MANAGER_PROFILE_IMAGE: String = "User_Restaurant_Manager_Profile_Image"
    const val PRODUCT_IMAGE: String = "Product_Image"

    // Intent extra constants.
    const val EXTRA_PRODUCT_ID: String = "extra_product_id"
    const val EXTRA_PRODUCT_OWNER_ID: String = "extra_product_owner_id"
    const val EXTRA_PRODUCT_DETAILS = "extra_product_details"
    const val EXTRA_PIZZA_PRODUCT_DETAILS: String = "extra_pizza_product_details"
    const val EXTRA_CHICKEN_PRODUCT_DETAILS: String = "extra_chicken_product_details"
    const val EXTRA_BEEF_PRODUCT_DETAILS: String = "extra_beef_product_details"
    const val EXTRA_ADDRESS_DETAILS: String = "AddressDetails"
    const val EXTRA_SELECT_ADDRESS: String = "extra_select_address"
    const val EXTRA_SELECTED_ADDRESS: String = "extra_selected_address"
    const val EXTRA_MY_ORDER_DETAILS: String = "extra_my_order_details"
    const val EXTRA_SOLD_PRODUCT_DETAILS: String = "extra_sold_product_details"


    const val CART_QUANTITY: String = "cart_quantity"



    const val HOME: String = "Home"
    const val OFFICE: String = "Office"
    const val OTHER: String = "Other"


    // START
    /**
     * A function for user profile image selection from phone storage.
     */
    fun showImageChooser(activity: Activity) {
        // An intent for launching the image selection of phone storage.
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        // Launches the image selection of phone storage using the constant code.

        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }
    // END

    // Create a function to get the extension of the selected image file.
    // START
    /**
     * A function to get the image file extension of the selected image.
     *
     * @param activity Activity reference.
     * @param uri Image file uri.
     */
    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        /*
         * MimeTypeMap: Two-way map that maps MIME-types to file extensions and vice versa.
         *
         * getSingleton(): Get the singleton instance of MimeTypeMap.
         *
         * getExtensionFromMimeType: Return the registered extension for the given MIME type.
         *
         * contentResolver.getType: Return the MIME type of the given content URL.
         */
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
    // END

}