package com.makaota.mammamskitchenmanager.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makaota.mammamskitchenmanager.databinding.ItemListLayoutBinding
import com.makaota.mammamskitchenmanager.models.CartItem
import com.makaota.mammamskitchenmanager.models.SoldProduct
import com.makaota.mammamskitchenmanager.ui.activities.SoldProductsDetailsActivity
import com.makaota.mammamskitchenmanager.utils.Constants
import com.makaota.mammamskitchenmanager.utils.GlideLoader

// START
/**
 * A adapter class for sold products list items.
 */
open class SoldProductsListAdapter(
    private val context: Context,
    private var list: ArrayList<SoldProduct>
) : RecyclerView.Adapter<SoldProductsListAdapter.MyViewHolder>() {

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = ItemListLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding)
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        with(holder){
            with(model){

                GlideLoader(context).loadProductPicture(model.image, binding.ivItemImage)

                binding.tvItemName.text = model.title
                binding.tvItemPrice.text = "R${model.total_amount}"
                binding.ibDeleteProduct.visibility = View.GONE

                holder.itemView.setOnClickListener {
                    // Launch Product details screen.
                    val intent = Intent(context, SoldProductsDetailsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS, model)
                    context.startActivity(intent)
                }

            }
        }


    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    inner class MyViewHolder(val binding: ItemListLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}
// END