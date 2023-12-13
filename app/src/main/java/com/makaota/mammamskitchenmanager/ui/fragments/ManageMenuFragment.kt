package com.makaota.mammamskitchenmanager.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.makaota.mammamskitchenmanager.R
import com.makaota.mammamskitchenmanager.databinding.FragmentManageMenuBinding
import com.makaota.mammamskitchenmanager.firestore.FirestoreClass
import com.makaota.mammamskitchenmanager.models.Product
import com.makaota.mammamskitchenmanager.ui.activities.AddMenuActivity
import com.makaota.mammamskitchenmanager.ui.adapters.MyProductListAdapter
import com.makaota.mammamskitchenmanager.utils.Constants
import com.shashank.sony.fancytoastlib.FancyToast

class ManageMenuFragment : BaseFragment() {

    private var _binding: FragmentManageMenuBinding? = null
    private lateinit var menuSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mProductsList: ArrayList<Product>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_menu_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_add_menu -> {
                startActivity(Intent(activity, AddMenuActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentManageMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root
        menuSwipeRefreshLayout = root.findViewById(R.id.menu_swipe_refresh_layout)
        refreshPage()
        return root
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFireStore()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun refreshPage() {

        menuSwipeRefreshLayout.setOnRefreshListener {
            getProductListFromFireStore() // Reload Menu Items


            _binding!!.menuSwipeRefreshLayout.isRefreshing = false
        }
    }

    /**
     * A function to get the successful product list from cloud firestore.
     *
     * @param productsList Will receive the product list from cloud firestore.
     */
    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {


        // Hide Progress dialog.
        hideProgressDialog()

        val scambaneList = ArrayList<Product>()
        val chipsList = ArrayList<Product>()
        val russianList = ArrayList<Product>()
        val additionalMealList = ArrayList<Product>()
        val drinksList = ArrayList<Product>()


        for (product in productsList) {

            //Scambane List
            if (product.category == Constants.SCAMBANE) {
                scambaneList.add(product)
            }

            //Chips List
            if (product.category == Constants.CHIPS){
                chipsList.add(product)
            }

            //Russian List
            if (product.category == Constants.RUSSIAN){
                russianList.add(product)
            }

            //Additional List
            if (product.category == Constants.ADDITIONAL_MEALS){
                additionalMealList.add(product)
            }

            //Drinks List
            if (product.category == Constants.DRINKS){
                drinksList.add(product)
            }

        }

        //Scambane List RecycleView
        if (scambaneList.size > 0) {
            binding.rvMyScambaneItems.visibility = View.VISIBLE
            binding.tvNoProductsFound.visibility = View.GONE

            binding.rvMyScambaneItems.layoutManager = LinearLayoutManager(activity)
            binding.rvMyScambaneItems.setHasFixedSize(true)

            val adapterProducts = MyProductListAdapter(requireActivity(), scambaneList, this@ManageMenuFragment)
            binding.rvMyScambaneItems.adapter = adapterProducts
        } else {
            binding.rvMyScambaneItems.visibility = View.GONE
            binding.tvNoProductsFound.visibility = View.VISIBLE
        }

        //Chips List RecycleView
        if (chipsList.size > 0) {
            binding.rvMyChipsItems.visibility = View.VISIBLE
            binding.tvNoProductsFound.visibility = View.GONE

            binding.rvMyChipsItems.layoutManager = LinearLayoutManager(activity)
            binding.rvMyChipsItems.setHasFixedSize(true)

            val adapterProducts = MyProductListAdapter(requireActivity(), chipsList, this@ManageMenuFragment)
            binding.rvMyChipsItems.adapter = adapterProducts
        } else {
            binding.rvMyChipsItems.visibility = View.GONE
            binding.tvNoProductsFound.visibility = View.VISIBLE
        }

        //Russian List RecycleView
        if (russianList.size > 0) {
            binding.rvMyRussianItems.visibility = View.VISIBLE
            binding.tvNoProductsFound.visibility = View.GONE

            binding.rvMyRussianItems.layoutManager = LinearLayoutManager(activity)
            binding.rvMyRussianItems.setHasFixedSize(true)

            val adapterProducts = MyProductListAdapter(requireActivity(), russianList, this@ManageMenuFragment)
            binding.rvMyRussianItems.adapter = adapterProducts
        } else {
            binding.rvMyRussianItems.visibility = View.GONE
            binding.tvNoProductsFound.visibility = View.VISIBLE
        }

        //Additional Meals List RecycleView
        if (additionalMealList.size > 0) {
            binding.rvMyAdditionalMealsItems.visibility = View.VISIBLE
            binding.tvNoProductsFound.visibility = View.GONE

            binding.rvMyAdditionalMealsItems.layoutManager = LinearLayoutManager(activity)
            binding.rvMyAdditionalMealsItems.setHasFixedSize(true)

            val adapterProducts = MyProductListAdapter(requireActivity(), additionalMealList, this@ManageMenuFragment)
            binding.rvMyAdditionalMealsItems.adapter = adapterProducts
        } else {
            binding.rvMyAdditionalMealsItems.visibility = View.GONE
            binding.tvNoProductsFound.visibility = View.VISIBLE
        }

        //Drinks List RecycleView
        if (drinksList.size > 0) {
            binding.rvMyDrinksItems.visibility = View.VISIBLE
            binding.tvNoProductsFound.visibility = View.GONE

            binding.rvMyDrinksItems.layoutManager = LinearLayoutManager(activity)
            binding.rvMyDrinksItems.setHasFixedSize(true)

            val adapterProducts = MyProductListAdapter(requireActivity(), drinksList, this@ManageMenuFragment)
            binding.rvMyDrinksItems.adapter = adapterProducts
        } else {
            binding.rvMyDrinksItems.visibility = View.GONE
            binding.tvNoProductsFound.visibility = View.VISIBLE
        }

    }

//    private fun getMenuByCategory(productsList: ArrayList<Product>){
//
//        // Hide Progress dialog.
//        hideProgressDialog()
//
//        mProductsList = productsList
//
//        if (productsList.size > 0) {
//            binding.rvMyProductItems.visibility = View.VISIBLE
//            binding.tvNoProductsFound.visibility = View.GONE
//
//            binding.rvMyProductItems.layoutManager = LinearLayoutManager(activity)
//            binding.rvMyProductItems.setHasFixedSize(true)
//
//            val adapterProducts = MyProductListAdapter(requireActivity(), productsList, this@ManageMenuFragment)
//            binding.rvMyProductItems.adapter = adapterProducts
//        } else {
//            binding.rvMyProductItems.visibility = View.GONE
//            binding.tvNoProductsFound.visibility = View.VISIBLE
//        }
//    }

    private fun getProductListFromFireStore() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class.
        FirestoreClass().getProductsList(this@ManageMenuFragment)
    }

    fun deleteProduct(productID: String){

        //Toast.makeText(requireActivity(),"You can now delete the product $productID",Toast.LENGTH_SHORT).show()
        showAlertDialogToDeleteProduct(productID)
    }

    // Create a function to notify the success result of product deleted from cloud firestore.
    // START
    /**
     * A function to notify the success result of product deleted from cloud firestore.
     */
    fun productDeleteSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            requireActivity(),
            resources.getString(R.string.product_delete_success_message),
            Toast.LENGTH_SHORT
        ).show()

        // Get the latest products list from cloud firestore.
        getProductListFromFireStore()

    }
    // END

    // Create a function to show the alert dialog for the confirmation of delete product from cloud firestore.
    // START
    /**
     * A function to show the alert dialog for the confirmation of delete product from cloud firestore.
     */
    private fun showAlertDialogToDeleteProduct(productID: String) {

        val builder = AlertDialog.Builder(requireActivity())
        //set title for alert dialog
        builder.setTitle(resources.getString(R.string.delete_dialog_title))
        //set message for alert dialog
        builder.setMessage(resources.getString(R.string.delete_dialog_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->

            // Call the function to delete the product from cloud firestore.
            // START
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            // Call the function of Firestore class.
            FirestoreClass().deleteProduct(this@ManageMenuFragment, productID)
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
}