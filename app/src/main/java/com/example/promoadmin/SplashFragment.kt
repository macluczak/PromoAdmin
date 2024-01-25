package com.example.promoadmin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.api.user.model.User
import com.example.promoadmin.databinding.FragmentSplashBinding

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSplashBinding.inflate(inflater, container, false)

        if (splashViewModel.isUserLoggedIn()) {
            lifecycleScope.launch {
                val user = splashViewModel.getUser()
                moveToHomeDestination(user)
                activity?.finish()
            }
        } else
            moveToRegisterDestination()


        return binding.root
    }

    private fun moveToRegisterDestination() =
        findNavController().navigate(R.id.action_splashFragment_to_authActivity)

    private fun moveToHomeDestination(user: User) =
        findNavController().navigate(
            SplashFragmentDirections.actionSplashFragmentToHomeActivity(user)
        )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}