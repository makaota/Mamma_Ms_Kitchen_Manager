package com.makaota.mammamskitchenmanager.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.makaota.mammamskitchenmanager.R
import com.makaota.mammamskitchenmanager.databinding.FragmentManageOrdersBinding
import com.makaota.mammamskitchenmanager.firestore.FirestoreClass
import com.makaota.mammamskitchenmanager.models.Order
import com.makaota.mammamskitchenmanager.ui.activities.SettingsActivity
import com.makaota.mammamskitchenmanager.ui.adapters.MyOrdersListAdapter
import com.shashank.sony.fancytoastlib.FancyToast

class ManageOrdersFragment : BaseFragment() {

    private var _binding: FragmentManageOrdersBinding? = null
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_settings -> {
                startActivity(Intent(activity, SettingsActivity::class.java))
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

        _binding = FragmentManageOrdersBinding.inflate(inflater, container, false)
        val root: View = binding.root
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh_layout)
        refreshPage()

        return root
    }

    override fun onResume() {
        super.onResume()
        getMyOrdersList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun refreshPage(){

        swipeRefreshLayout.setOnRefreshListener {

            getMyOrdersList() //Reload order List Items

            FancyToast.makeText(requireContext(),
                "Orders Refreshed",
                FancyToast.LENGTH_SHORT,
                FancyToast.SUCCESS,
                true).show()

            _binding!!.swipeRefreshLayout.isRefreshing = false

        }
    }


    // Create a function to get the success result of the my order list from cloud firestore.
    // START
    /**
     * A function to get the success result of the my order list from cloud firestore.
     *
     * @param ordersList List of my orders.
     */
    fun populateOrdersListInUI(ordersList: ArrayList<Order>) {

        // Hide the progress dialog.
        hideProgressDialog()

        // Populate the orders list in the UI.
        // START
        if (ordersList.size > 0) {

            _binding!!.rvMyOrderItems.visibility = View.VISIBLE
            _binding!!.tvNoOrdersFound.visibility = View.GONE

            _binding!!.rvMyOrderItems.layoutManager = LinearLayoutManager(activity)
            _binding!!.rvMyOrderItems.setHasFixedSize(true)

            val myOrdersAdapter = MyOrdersListAdapter(requireActivity(), ordersList)
            _binding!!.rvMyOrderItems.adapter = myOrdersAdapter

        } else {
            _binding!!.rvMyOrderItems.visibility = View.GONE
            _binding!!.tvNoOrdersFound.visibility = View.VISIBLE
        }
        // END
    }
    // END

    // Create a function to call the firestore class function to get the list of my orders.
    // START
    /**
     * A function to get the list of my orders.
     */
    private fun getMyOrdersList() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getMyOrdersList(this@ManageOrdersFragment)
    }

}