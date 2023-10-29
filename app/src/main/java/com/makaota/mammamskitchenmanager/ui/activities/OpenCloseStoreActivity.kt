package com.makaota.mammamskitchenmanager.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.makaota.mammamskitchenmanager.R
import com.makaota.mammamskitchenmanager.databinding.ActivityOpenCloseStoreBinding

class OpenCloseStoreActivity : AppCompatActivity() {

    lateinit var binding: ActivityOpenCloseStoreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenCloseStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarOpenCloseStoreActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarOpenCloseStoreActivity.setNavigationOnClickListener { onBackPressed() }
    }
}