package com.example.promoadmin.feature.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.promoadmin.R
import com.example.promoadmin.databinding.FragmentProfileBinding
import com.example.promoadmin.feature.home.HomeViewModel
import com.example.promoadmin.util.toSimpleDate
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val homeViewModel: HomeViewModel by viewModels({ requireActivity() })
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        homeViewModel.fetchUserData()

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            binding.profileEmail.text = user.email
            binding.profileRole.text = user.role.uppercase()
            binding.profileCreatedAt.text = user.createdAt.toSimpleDate()

            binding.submitNewPassword.setOnClickListener {
                val newPassword = binding.profilePasswordNew.text.toString()
                val oldPassword = binding.profilePasswordOld.text.toString()
                homeViewModel.changePassword(newPassword, oldPassword)
            }
        }

        lifecycleScope.launch {
            homeViewModel.changePasswordResponse.collect { response ->
                when (response) {
                    400 -> binding.profilePasswordOld.error = "Invalid Input"
                    401 -> binding.profilePasswordOld.error = "Unauthorised"
                    200 -> {
                        binding.profilePasswordNew.text.clear()
                        binding.profilePasswordOld.text.clear()
                        Toast.makeText(requireActivity(), "success", Toast.LENGTH_SHORT).show()
                    }
                    else -> binding.profilePasswordOld.error = "Failed"
                }
            }
        }

        binding.profileLogOut.setOnClickListener {
            homeViewModel.logoutUser()
            moveToAuthDestination()
            activity?.finish()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun moveToAuthDestination() =
        findNavController().navigate(R.id.action_profileFragment_to_authActivity)
}