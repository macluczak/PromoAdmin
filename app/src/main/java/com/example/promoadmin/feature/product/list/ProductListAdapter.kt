package com.example.promoshow.feature.stores.details
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.api.product.model.Product
import com.example.promoadmin.R
import com.example.promoadmin.util.loadImageWithGlide
import com.example.promoadmin.util.toPLN
import kotlin.reflect.KFunction1

class ProductListAdapter(private val deals: List<Product>, private val offerClickListener: KFunction1<Product, Unit>) :
    RecyclerView.Adapter<ProductListAdapter.StoresDealsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoresDealsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_store_deals, parent, false)
        return StoresDealsViewHolder(view, offerClickListener)
    }

    override fun onBindViewHolder(holder: StoresDealsViewHolder, position: Int) {
        holder.bind(deals[position])
    }

    override fun getItemCount(): Int {
        return deals.size
    }

    class StoresDealsViewHolder(itemView: View,  private val offerClickListener: KFunction1<Product, Unit>) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.productNameTextView)
        private val maker: TextView = itemView.findViewById(R.id.makerNameTextView)
        private val discount: TextView = itemView.findViewById(R.id.promoPriceTextView)
        private val price: TextView = itemView.findViewById(R.id.regularPriceTextView)
        private val image: ImageView = itemView.findViewById(R.id.productImageView)

        fun bind(product: Product) {
            title.text = product.name
            maker.text = product.maker
            discount.text = product.discountPrice?.toPLN()
            price.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                text = product.price.toPLN()
            }
            loadImageWithGlide(image, product.image)
            itemView.setOnClickListener { offerClickListener(product)}
        }
    }
}