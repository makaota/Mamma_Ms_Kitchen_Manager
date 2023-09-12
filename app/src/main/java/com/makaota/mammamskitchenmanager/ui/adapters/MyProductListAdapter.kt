package com.makaota.mammamskitchenmanager.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makaota.mammamskitchenmanager.databinding.ItemListLayoutBinding
import com.makaota.mammamskitchenmanager.models.Product
import com.makaota.mammamskitchenmanager.ui.activities.ProductDetailsActivity
import com.makaota.mammamskitchenmanager.ui.fragments.ManageMenuFragment
import com.makaota.mammamskitchenmanager.utils.Constants
import com.makaota.mammamskitchenmanager.utils.GlideLoader

class MyProductListAdapter(
    private val context: Context,
    private var list: ArrayList<Product>,
    private val fragment: ManageMenuFragment
) : RecyclerView.Adapter<MyProductListAdapter.MyViewHolder>() {

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = ItemListLayoutBinding.inflate(LayoutInflater.from(context),parent, false)
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
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]


        with(holder){
            with(model){
                GlideLoader(context).loadProductPicture(model.image, binding.ivItemImage)
                binding.tvItemName.text = model.title
                binding.tvItemPrice.text = "R${model.price}"

                // Assigning the click event to the delete button.
                // START
                binding.ibDeleteProduct.setOnClickListener {

                    // Now let's call the delete function of the ProductsFragment.
                    // START
                    fragment.deleteProduct(model.product_id)
                    // END
                }
                // END

                holder.itemView.setOnClickListener {
                    // Launch Product details screen.
                    val intent = Intent(context, ProductDetailsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.product_id)
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