package com.example.promoadmin.feature.admin

import android.app.Activity.RESULT_OK
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
import com.example.api.shop.model.ShopRequest
import com.example.promoadmin.R;

import com.example.promoadmin.databinding.FragmentAdminBinding

import com.example.promoadmin.feature.home.HomeViewModel

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

import java.io.IOException

@AndroidEntryPoint
class AdminFragment : Fragment() {

    private var _binding: FragmentAdminBinding? = null
    private val binding get() = _binding!!

    private var selectedImageUri: Uri? = null

    private val homeViewModel: HomeViewModel by viewModels()

    private var selectedUserId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editLl.visibility = View.VISIBLE
        homeViewModel.fetchAllUsers()

        homeViewModel.users.observe(viewLifecycleOwner) { users ->
            val usersMap: Map<String, String> = users.associateBy({ it.id }, { it.email })

            val arrayAdapter = ArrayAdapter<String>(
                requireActivity(),
                R.layout.list_item,
                usersMap.values.toTypedArray()
            )
            binding.autoCompleteTextView.setAdapter(arrayAdapter)

            binding.autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
                selectedUserId = usersMap.keys.toList()[position]
            }
        }
        binding.editStoreImage.setOnClickListener { openImagePicker() }

        binding.editSave.setOnClickListener {
            lifecycleScope.launch {
                try {
                    binding.editLoad.visibility = View.VISIBLE
                    if (isFormValid()) {
                        selectedUserId?.let { it1 ->
                            homeViewModel.createShop(
                                createShopFromInput(),
                                it1
                            )
                        }
                    }
                } catch (e: IOException) {
                    Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show()
                } finally {
                    binding.editLoad.visibility = View.INVISIBLE
                    clearForm()
                }
            }
        }
    }

    private suspend fun createShopFromInput(): ShopRequest {
        return ShopRequest(
            name = binding.editStoreName.text.toString(),
            locationCode = binding.editStoreLocationCode.text.toString(),
            description = binding.editStoreDescription.text.toString(),
            image = getImageUrl()
        )
    }

    private suspend fun getImageUrl() =
        if (selectedImageUri != null) {
            homeViewModel.uploadImageToFirebase(selectedImageUri).toString()
        } else ""

    private fun isFormValid(): Boolean {
        var isValid = true

        isValid = isValidField(binding.editStoreName, "Please enter store name") && isValid
        isValid =
            isValidField(binding.editStoreLocationCode, "Please enter location code") && isValid
        isValid = isValidField(binding.editStoreDescription, "Please enter description") && isValid
        isValid =
            isValidField(binding.autoCompleteTextView, "Please select assigned user") && isValid

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

    private fun clearForm() {
        binding.apply {
            editStoreName.text.clear()
            editStoreDescription.text.clear()
            editStoreLocationCode.text.clear()
            storeImage.setImageResource(R.drawable.baseline_add_photo_alternate_24)
            selectedImageUri = null
            selectedUserId = null
            autoCompleteTextView.text.clear()
            autoCompleteTextView.clearListSelection()
        }
    }

    override fun onPause() {
        super.onPause()
        binding.editStoreName.error = null
        binding.editStoreLocationCode.error = null
        binding.editStoreDescription.error = null
        binding.autoCompleteTextView.error = null
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 2
    }
}
