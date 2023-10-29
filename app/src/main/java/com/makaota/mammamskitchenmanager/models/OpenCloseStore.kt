package com.makaota.mammamskitchenmanager.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class OpenCloseStore(
    var isStoreOpen: Boolean = false
): Parcelable