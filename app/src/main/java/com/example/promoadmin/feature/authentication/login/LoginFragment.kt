package com.example.promoadmin.feature.authentication.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.api.user.model.User

import com.example.promoadmin.R
import com.example.promoadmin.databinding.FragmentLoginBinding
import com.example.promoadmin.feature.authentication.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val authViewModel: AuthViewModel by viewModels({ requireActivity() })

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.logSignUpButton.setOnClickListener {
            moveToRegisterDestination()
        }
        binding.logSignInButton.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            authViewModel.tryLogin(email, password, ::loadingStart, ::loadingEnd)
        }

        authViewModel.emailError.observe(viewLifecycleOwner) {
            showError(
                binding.editTextEmail,
                it
            )
        }
        authViewModel.passwordError.observe(viewLifecycleOwner) {
            showError(
                binding.editTextPassword,
                it
            )
        }
        authViewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                moveToHomeDestination(it)
                activity?.finish()
            }

        }

        lifecycleScope.launchWhenStarted {
            authViewModel.serverError.collect { showError(binding.editTextEmail, it) }
        }

        return binding.root
    }

    private fun showError(editText: EditText, error: String?) {
        editText.error = error
    }

    private fun moveToRegisterDestination() =
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)

    private fun moveToHomeDestination(user: User) =
        findNavController().navigate(
            LoginFragmentDirections.actionRegisterFragmentToHomeActivity(user)
        )

    private fun loadingStart() {
        binding.loadingLogin.visibility = View.VISIBLE
    }

    private fun loadingEnd() {
        binding.loadingLogin.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        authViewModel.clearAllErrors()
    }


}