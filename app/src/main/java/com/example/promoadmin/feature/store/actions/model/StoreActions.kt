package com.example.promoadmin.feature.store.actions.model

import com.example.promoadmin.R

enum class StoreActions(val action: String, val icon: Int, val description: String) {
    EditStore(
        "Store",
        R.drawable.baseline_storefront_24,
        "Modify Store Information",
    ),
    EditProduct(
        "Products",
        R.drawable.baseline_app_registration_24,
        "Modify Products Information",
    ),
    AddProduct(
        "Add Product",
        R.drawable.outline_add_box_24,
        "Add a New Product",
    ),
}