package com.example.promoadmin.feature.authentication.register
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.promoadmin.R
import com.example.promoadmin.databinding.FragmentLoginBinding
import com.example.promoadmin.databinding.FragmentRegisterBinding
import com.example.promoadmin.feature.authentication.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val authViewModel: AuthViewModel by viewModels({ requireActivity() })

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.registerSignInButton.setOnClickListener {
            moveToRegisterDestination()
        }
            binding.registerSignUpButton.setOnClickListener {
                val email = binding.editTextEmail.text.toString()
                val password = binding.editTextPassword.text.toString()
                authViewModel.tryRegister(email, password)
            }

            authViewModel.emailError.observe(viewLifecycleOwner) { showError(binding.editTextEmail, it) }
            authViewModel.passwordError.observe(viewLifecycleOwner) { showError(binding.editTextPassword, it) }

            lifecycleScope.launchWhenStarted {
                authViewModel.serverError.collect { showError(binding.editTextEmail, it) }
            }

            return binding.root
        }

    private fun showError(editText: EditText, error: String?) {
        editText.error = error
    }
    private fun moveToRegisterDestination() =
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

    override fun onDestroy() {
        super.onDestroy()
        authViewModel.clearAllErrors()
    }

}