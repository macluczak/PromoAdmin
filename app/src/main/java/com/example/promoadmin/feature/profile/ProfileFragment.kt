package com.example.promoadmin.feature.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.promoadmin.R
import com.example.promoadmin.SplashViewModel
import com.example.promoadmin.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val splashViewModel: SplashViewModel by viewModels({requireActivity()})
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.profileLogOut.setOnClickListener{
            splashViewModel.logoutUser()
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