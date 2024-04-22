package com.example.promoadmin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.api.user.model.User
import com.example.promoadmin.databinding.FragmentSplashBinding

import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (splashViewModel.isUserLoggedIn()) {
            splashViewModel.getUser(::moveToHomeDestination, ::moveToRegisterDestination)
        } else {
            moveToRegisterDestination()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun moveToRegisterDestination() = if(isAdded) {
        findNavController().navigate(R.id.action_splashFragment_to_authActivity)
        activity?.finish()
    } else null

    private fun moveToHomeDestination(user: User) = if (isAdded) {
        findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeActivity(user))
        activity?.finish()
    }
    else null

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}