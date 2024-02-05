package com.makaota.mammamskitchenmanager.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.makaota.mammamskitchenmanager.R
import com.makaota.mammamskitchenmanager.firestore.FirestoreClass

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else
        {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        val splashAnimation = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.anim_splash)
        val splashText = findViewById<ImageView>(R.id.splash_text)
        splashText.animation = splashAnimation

        splashAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // "Add the code that you want to execute when animation starts")
            }

            override fun onAnimationEnd(animation: Animation?) {
                // "Add the code that you want to execute when animation ends")

                Handler(Looper.getMainLooper()).postDelayed({
                    try {

                        showProgressDialog(resources.getString(R.string.please_wait))
                        FirestoreClass().getUserDetails(this@SplashActivity)

                    } catch (e: IllegalArgumentException) {

                        hideProgressDialog()
                        Log.i(TAG, "error message $e.message")
                        val intent = Intent(this@SplashActivity, LoginActivity::class.java)

                        // Adding the FLAG_ACTIVITY_CLEAR_TASK flag to clear the task
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK


                        startActivity(intent)
                    }
                }, 1000)
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // "Add the code that you want to execute when animation repeats")
            }
        })

    }

    fun userLoggedInSuccess() {

        hideProgressDialog()

        val intent = Intent(this@SplashActivity, DashboardActivity::class.java)

        // Adding the FLAG_ACTIVITY_CLEAR_TASK flag to clear the task
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK


        startActivity(intent)
        finish()
        // END
    }
}