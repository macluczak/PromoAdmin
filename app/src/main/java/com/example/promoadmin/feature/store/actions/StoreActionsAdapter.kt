package com.example.promoadmin.feature.store.actions
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.promoadmin.R
import com.example.promoadmin.feature.store.actions.model.StoreActions

class StoreActionsAdapter(private val actions: List<StoreActions>, private val offerClickListener: (StoreActions) -> Unit) :
    RecyclerView.Adapter<StoreActionsAdapter.StoresViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoresViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_store_action, parent, false)
        return StoresViewHolder(view, offerClickListener)
    }

    override fun onBindViewHolder(holder: StoresViewHolder, position: Int) {
        holder.bind(actions[position])
    }

    override fun getItemCount(): Int {
        return actions.size
    }

    class StoresViewHolder(itemView: View,  private val offerClickListener: (StoreActions) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.storeActionName)
        private val imageView: ImageView = itemView.findViewById(R.id.storeActionImage)
        private val description: TextView = itemView.findViewById(R.id.storeActionDescription)
        private val cv: CardView = itemView.findViewById(R.id.action_cv)

        fun bind(action: StoreActions) {
            name.text = action.action
            imageView.setImageResource(action.icon)
            description.text = action.description
            cv.setOnClickListener { offerClickListener(action) }
        }
    }
}