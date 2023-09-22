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
import com.shashank.sony.fancytoastlib.FancyToast

class ManageMenuFragment : BaseFragment() {

    private var _binding: FragmentManageMenuBinding? = null
    private lateinit var menuSwipeRefreshLayout: SwipeRefreshLayout

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
            FancyToast.makeText(
                requireContext(),
                "Menu Refreshed",
                FancyToast.LENGTH_SHORT,
                FancyToast.SUCCESS,
                true
            ).show()

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

        if (productsList.size > 0) {
            binding.rvMyProductItems.visibility = View.VISIBLE
            binding.tvNoProductsFound.visibility = View.GONE

            binding.rvMyProductItems.layoutManager = LinearLayoutManager(activity)
            binding.rvMyProductItems.setHasFixedSize(true)

            val adapterProducts = MyProductListAdapter(requireActivity(), productsList, this@ManageMenuFragment)
            binding.rvMyProductItems.adapter = adapterProducts
        } else {
            binding.rvMyProductItems.visibility = View.GONE
            binding.tvNoProductsFound.visibility = View.VISIBLE
        }
    }

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