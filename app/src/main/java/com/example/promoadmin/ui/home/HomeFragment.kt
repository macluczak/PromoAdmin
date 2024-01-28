package com.example.promoadmin.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.api.shop.model.Shop
import com.example.promoadmin.R
import com.example.promoadmin.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var storesAdapter: StoresListAdapter

    val storesViewModel: HomeViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        lifecycleScope.launch {
            storesViewModel.shops.observe(viewLifecycleOwner) { shops ->

                recyclerView = binding.recyclerView
                recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

                storesAdapter = StoresListAdapter(shops.sortedBy { it.name }, ::handleOfferClick)
                recyclerView.adapter = storesAdapter
            }
        }

        return  binding.root
    }

    override fun onResume() {
        super.onResume()
        storesViewModel.fetchStoresForUser()
    }
    private fun handleOfferClick(shop: Shop) {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToStoreActivity(shop))
    }

}