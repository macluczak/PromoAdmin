package com.example.promoadmin.ui.home
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.api.shop.model.Shop
import com.example.promoadmin.R
import com.example.promoadmin.util.loadImageWithGlide

class StoresListAdapter(private val deals: List<Shop>,  private val offerClickListener: (Shop) -> Unit) :
    RecyclerView.Adapter<StoresListAdapter.StoresViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoresViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stores_list, parent, false)
        return StoresViewHolder(view, offerClickListener)
    }

    override fun onBindViewHolder(holder: StoresViewHolder, position: Int) {
        holder.bind(deals[position])
    }

    override fun getItemCount(): Int {
        return deals.size
    }

    class StoresViewHolder(itemView: View,  private val offerClickListener: (Shop) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.storesTitleTextView)
        private val counter: TextView = itemView.findViewById(R.id.deals_count)
        private val image: ImageView = itemView.findViewById(R.id.image)

        fun bind(shop: Shop) {
            title.text = shop.name
            counter.text = shop.locationCode
            loadImageWithGlide(image, shop.image)
            itemView.setOnClickListener { offerClickListener(shop)}
        }
    }
}