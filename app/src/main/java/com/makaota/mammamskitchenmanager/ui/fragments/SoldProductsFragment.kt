package com.makaota.mammamskitchenmanager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.makaota.mammamskitchenmanager.R
import com.makaota.mammamskitchenmanager.databinding.FragmentSoldProductsBinding
import com.makaota.mammamskitchenmanager.firestore.FirestoreClass
import com.makaota.mammamskitchenmanager.models.SoldProduct
import com.makaota.mammamskitchenmanager.ui.adapters.SoldProductsListAdapter
import com.shashank.sony.fancytoastlib.FancyToast

class SoldProductsFragment : BaseFragment() {

    private var _binding: FragmentSoldProductsBinding? = null
    private lateinit var soldProductsSwipeRefreshLayout: SwipeRefreshLayout

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSoldProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        soldProductsSwipeRefreshLayout = root.findViewById(R.id.sold_products_swipe_refresh_layout)
        refreshPage()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Override the onResume function and call the function to get the list of sold products.
    override fun onResume() {
        super.onResume()

        getSoldProductsList()
    }

    private fun refreshPage(){

        soldProductsSwipeRefreshLayout.setOnRefreshListener {

            getSoldProductsList() //Reload order List Items

            FancyToast.makeText(requireContext(),
                "Orders Refreshed",
                FancyToast.LENGTH_SHORT,
                FancyToast.SUCCESS,
                true).show()

            _binding!!.soldProductsSwipeRefreshLayout.isRefreshing = false

        }
    }

    // Create a function to get the list of sold products.
    // START
    private fun getSoldProductsList() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class.
        FirestoreClass().getSoldProductsList(this@SoldProductsFragment)
    }
    // END

    // Create a function to get the success result list of sold products.
    // START
    /**
     * A function to get the list of sold products.
     */
    fun successSoldProductsList(soldProductsList: ArrayList<SoldProduct>) {

        // Hide Progress dialog.
        hideProgressDialog()

        // Populate the list in the RecyclerView using the adapter class.
        // START
        if (soldProductsList.size > 0) {
            _binding!!.rvSoldProductItems.visibility = View.VISIBLE
            _binding!!.tvNoSoldProductsFound.visibility = View.GONE

            _binding!!.rvSoldProductItems.layoutManager = LinearLayoutManager(activity)
            _binding!!.rvSoldProductItems.setHasFixedSize(true)

            val soldProductsListAdapter =
                SoldProductsListAdapter(requireActivity(), soldProductsList)
            _binding!!.rvSoldProductItems.adapter = soldProductsListAdapter
        } else {
            _binding!!.rvSoldProductItems.visibility = View.GONE
            _binding!!.tvNoSoldProductsFound.visibility = View.VISIBLE
        }
        // END
    }
    // END
}