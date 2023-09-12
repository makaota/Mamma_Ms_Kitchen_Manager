package com.makaota.mammamskitchenmanager.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.makaota.mammamskitchenmanager.R
import com.makaota.mammamskitchenmanager.databinding.ItemCartLayoutBinding
import com.makaota.mammamskitchenmanager.models.CartItem
import com.makaota.mammamskitchenmanager.utils.GlideLoader

// Create a adapter class for CartItemsList.
// START
/**
 * A adapter class for dashboard items list.
 */
open class CartItemsListAdapter(
    private val context: Context,
    private var list: ArrayList<CartItem>,
    private val updateCartItems: Boolean
) : RecyclerView.Adapter<CartItemsListAdapter.MyViewHolder>() {

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = ItemCartLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
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

                GlideLoader(context).loadProductPicture(model.image, binding.ivCartItemImage)

                binding.tvCartItemTitle.text = model.title
                binding.tvCartItemPrice.text = "R${model.price}"
                binding.tvCartQuantity.text = model.cart_quantity


                // Show the text Out of Stock when cart quantity is zero.
                // START
                if (model.cart_quantity == "0") {

                    binding.tvCartQuantity.text =
                        context.resources.getString(R.string.lbl_out_of_stock)

                    binding.tvCartQuantity.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorSnackBarError
                        )
                    )
                } else {

                    binding.tvCartQuantity.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorSecondaryText
                        )
                    )
                }
                // END

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
    inner class MyViewHolder(val binding: ItemCartLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}
// END