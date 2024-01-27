package com.example.promoadmin.feature.store.details

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.api.shop.model.Shop
import com.example.api.shop.model.ShopRequest
import com.example.promoadmin.R
import com.example.promoadmin.databinding.FragmentStoreActionsBinding
import com.example.promoadmin.databinding.FragmentStoreDetailsBinding
import com.example.promoadmin.feature.store.StoresViewModel
import com.example.promoadmin.feature.store.actions.StoreActionsAdapter
import com.example.promoadmin.feature.store.actions.StoreActionsFragmentArgs
import com.example.promoadmin.feature.store.actions.StoreActionsFragmentDirections
import com.example.promoadmin.feature.store.actions.model.StoreActions
import com.example.promoadmin.util.loadEditImageWithGlide
import com.example.promoadmin.util.loadImageWithGlide
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

@AndroidEntryPoint
class StoreDetailsFragment : Fragment() {

    private var _binding: FragmentStoreDetailsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<StoreActionsFragmentArgs>()

    private var selectedImageUri: Uri? = null

    private val storesViewModel: StoresViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoreDetailsBinding.inflate(inflater, container, false)
        storesViewModel.fetchStoreDetails(args.shopObject.id.toString())
        binding.storeBackButton.setOnClickListener { moveToStoreActions(args.shopObject) }
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editLl.visibility = View.INVISIBLE
        storesViewModel.shop.observe(viewLifecycleOwner){ shop ->
            if(shop != null) {
                loadEditImageWithGlide(binding.storeImage, shop.image)
                binding.editStoreLocationCode.text = shop.locationCode.toEditable()
                binding.editStoreDescription.text = shop.description.toEditable()
                binding.editStoreName.text = shop.name.toEditable()
                binding.editStoreImage.setOnClickListener {openImagePicker()  }
                binding.editLl.visibility = View.VISIBLE
                binding.editSave.setOnClickListener {
                    lifecycleScope.launch {
                        try {
                            binding.editLoad.visibility = View.VISIBLE
                            submitModifiedShop(shop)

                        }catch (e: IOException){
                            Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show()
                        }
                        finally {
                            binding.editLoad.visibility = View.INVISIBLE
                            Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show()
                            moveToStoreActions(shop)
                        }
                    }

                }
            }
        }
    }

    private suspend fun submitModifiedShop(shop: Shop){
        val body = createModifiedShop(shop)
        storesViewModel.editShop(body)
    }

    private suspend fun createModifiedShop(originalShop: Shop): ShopRequest {
        return ShopRequest(
            name = binding.editStoreName.text.toString(),
            locationCode = binding.editStoreLocationCode.text.toString(),
            description = binding.editStoreDescription.text.toString(),
            image = getImageUrl(originalShop)
        )
    }

    private suspend fun getImageUrl(originalShop: Shop) = if (selectedImageUri != null) {
        storesViewModel.uploadImageToFirebase(selectedImageUri).toString()
    } else {
        originalShop.image
    }

    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            binding.storeImage.setImageURI(data.data)
        }
    }

    private fun moveToStoreActions(shop: Shop){
        findNavController().navigate(StoreDetailsFragmentDirections.actionStoreDetailsFragmentToStoreActionsFragment(shop))

    }
    companion object{
        private val PICK_IMAGE_REQUEST = 1
    }
}
