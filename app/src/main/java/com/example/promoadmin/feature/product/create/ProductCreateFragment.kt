package com.example.promoadmin.feature.product.create

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.api.product.model.Category
import com.example.api.product.model.Product
import com.example.api.product.model.ProductRequest
import com.example.promoadmin.R
import com.example.promoadmin.databinding.FragmentProductDetailsBinding
import com.example.promoadmin.feature.product.ProductViewModel
import com.example.promoadmin.feature.product.details.ProductDetailsFragmentArgs
import com.example.promoadmin.feature.product.details.ProductDetailsFragmentDirections
import com.example.promoadmin.feature.store.details.StoreDetailsFragment
import com.example.promoadmin.util.loadEditImageWithGlide
import com.example.promoadmin.util.priceToText
import com.example.promoadmin.util.toDoubleSafe
import com.example.promoadmin.util.toEditable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.IOException

@AndroidEntryPoint
class ProductCreateFragment : Fragment() {

    private val args by navArgs<ProductCreateFragmentArgs>()
    private var _binding: FragmentProductDetailsBinding? = null

    private val productViewModel: ProductViewModel by viewModels({ requireActivity() })

    private var selectedImageUri: Uri? = null

    private var selectedCategory: String? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        binding.productBackButton.setOnClickListener { backToProductsList() }

        binding.headerTitle.text = "Add Product"
        binding.editDelete.visibility = View.INVISIBLE

        val categoryList = Category.values().map { it.name }

        val arrayAdapter = ArrayAdapter(
            requireActivity(),
            R.layout.list_item,
            categoryList
        )
        binding.categoryAutoCompleteTextView.setAdapter(arrayAdapter)

        binding.categoryAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            selectedCategory = Category.values()[position].name
        }

        binding.productImage.setOnClickListener { openImagePicker() }
        binding.editSave.setOnClickListener {
            if (isFormValid()) {
                lifecycleScope.launch {
                    try {
                        binding.editLoad.visibility = View.VISIBLE
                        submitNewProduct()

                    } catch (_: IOException) {
                    } finally {
                        binding.editLoad.visibility = View.INVISIBLE
                        Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show()
                        backToProductsList()
                    }
                }
            }

        }
        return binding.root
    }


    private suspend fun submitNewProduct() {
        val body = createNewProduct()
        productViewModel.addProduct(body)
    }

    private suspend fun createNewProduct(): ProductRequest {
        return ProductRequest(
            name = binding.productName.text.toString(),
            description = binding.productDescription.text.toString(),
            price = binding.productPrice.text.toString().toDoubleSafe(),
            discountPrice = binding.productDiscount.text.toString().toDoubleSafe(),
            maker = binding.productMaker.text.toString(),
            image = getImageUrl(),
            amount = 1,
            category = selectedCategory!!,
            shop = args.shopObject
        )
    }

    private suspend fun getImageUrl() = if (selectedImageUri != null) {
        productViewModel.uploadImageToFirebase(selectedImageUri).toString()
    } else {
       ""
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, StoreDetailsFragment.PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == StoreDetailsFragment.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            binding.productImage.setImageURI(data.data)
        }
    }

    private fun isFormValid(): Boolean {
        var isValid = true

        isValid = isValidField(binding.productName, "Please enter product name") && isValid
        isValid = isValidField(binding.productMaker, "Please enter brand") && isValid
        isValid = isValidField(binding.productDescription, "Please enter description") && isValid
        isValid = isValidField(binding.productPrice, "Please enter price") && isValid
        isValid = isValidField(binding.productDiscount, "Please enter discount") && isValid
        isValid = isValidField(binding.categoryAutoCompleteTextView, "Please enter discount") && isValid

        return isValid
    }

    private fun isValidField(editText: EditText, errorMessage: String): Boolean {
        val fieldValue = editText.text.toString().trim()
        return if (fieldValue.isEmpty()) {
            editText.error = errorMessage
            false
        } else {
            true
        }
    }

    private fun backToProductsList() {
        findNavController().navigate(
            ProductCreateFragmentDirections.actionProductCreateFragmentToStoreActionsFragment(
                args.shopObject
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}