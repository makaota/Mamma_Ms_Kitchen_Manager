package com.makaota.mammamskitchenmanager.interfaces

import com.makaota.mammamskitchenmanager.models.PushNotification
import com.makaota.mammamskitchenmanager.utils.Constants
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI {

    @Headers("Authorization: key=${Constants.SERVER_KEY}")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}