package com.makaota.mammamskitchenmanager.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// START
/**
 * A data model class for User with required fields.
 */
// START
@Parcelize
data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val image: String = "",
    val mobile: String = "",
    var userToken: String = "",
    val profileCompleted: Int = 0) : Parcelable