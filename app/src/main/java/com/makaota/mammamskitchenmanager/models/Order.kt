package com.makaota.mammamskitchenmanager.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.android.parcel.Parcelize

// Create a data model class for Order Items with required fields.
// START
/**
 * A data model class for Order item with required fields.
 */
@Parcelize
data class Order(
    val user_id: String = "",
    val items: ArrayList<CartItem> = ArrayList(),
    val title: String = "",
    val orderStatus: String = "",
    val orderNumber: String = "",
    val userName: String = "",
    val userMobile: String="",
    val image: String = "",
    val sub_total_amount: String = "",
    val shipping_charge: String = "",
    val total_amount: String = "",
    val order_datetime: Long = 0L,
    @DocumentId
    var id: String = ""
) : Parcelable
// END