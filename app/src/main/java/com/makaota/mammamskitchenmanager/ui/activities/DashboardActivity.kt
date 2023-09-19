package com.makaota.mammamskitchenmanager.ui.activities

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.makaota.mammamskitchenmanager.R
import com.makaota.mammamskitchenmanager.databinding.ActivityDashboardBinding

class DashboardActivity  : BaseActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Update the background color of the action bar as per our design requirement.
        supportActionBar!!.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this@DashboardActivity,
                R.drawable.app_gradient_color_background
            )
        )
        // END

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_dashboard)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_manage_menu, R.id.navigation_manage_orders, R.id.navigation_sold_products
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

     //   navView.itemIconTintList = null
    }

    override fun onBackPressed() {
        doubleBackToExit()
    }
}