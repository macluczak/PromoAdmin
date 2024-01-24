package com.example.promoadmin.feature.authentication.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.promoadmin.R
import com.example.promoadmin.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.registerSignUpButton.setOnClickListener{
            moveToRegisterDestination()
        }
        return binding.root
    }

    private fun moveToRegisterDestination() =
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)


}