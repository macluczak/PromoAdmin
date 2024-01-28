package com.example.promoadmin.feature.product.details

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
import com.example.promoadmin.feature.store.details.StoreDetailsFragment
import com.example.promoadmin.util.loadEditImageWithGlide
import com.example.promoadmin.util.priceToText
import com.example.promoadmin.util.toEditable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.IOException

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private val args by navArgs<ProductDetailsFragmentArgs>()
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

        val product = args.productObject

        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        binding.productBackButton.setOnClickListener { backToProductsList() }

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

        selectedCategory = product.category

        binding.categoryAutoCompleteTextView.setText(product.category, false)
        loadEditImageWithGlide(binding.productImage, product.image)
        binding.productMaker.text = product.maker.toEditable()
        binding.productDescription.text = product.description.toEditable()
        binding.productName.text = product.name.toEditable()
        binding.productPrice.text = product.price.priceToText().toEditable()
        binding.productDiscount.text = product.discountPrice?.priceToText()?.toEditable()

        binding.productImage.setOnClickListener { openImagePicker() }
        binding.editSave.setOnClickListener {
            if (isFormValid()) {
                lifecycleScope.launch {
                    try {
                        binding.editLoad.visibility = View.VISIBLE
                        submitModifiedProduct(product)

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


    private suspend fun submitModifiedProduct(product: Product) {
        val body = createModifiedProduct(product)
        productViewModel.editProduct(body)
    }

    private suspend fun createModifiedProduct(product: Product): ProductRequest {
        return ProductRequest(
            id = product.id,
            name = binding.productName.text.toString(),
            description = binding.productDescription.text.toString(),
            price = binding.productPrice.text.toString().toDouble(),
            discountPrice = binding.productDiscount.text.toString().toDouble(),
            maker = binding.productMaker.text.toString(),
            image = getImageUrl(product),
            amount = product.amount,
            category = selectedCategory!!,
            shop = args.shopObject

        )
    }

    private suspend fun getImageUrl(product: Product) = if (selectedImageUri != null) {
        productViewModel.uploadImageToFirebase(selectedImageUri).toString()
    } else {
        product.image
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
            ProductDetailsFragmentDirections.actionProductDetailsFragmentToProductListFragment(
                args.shopObject
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}