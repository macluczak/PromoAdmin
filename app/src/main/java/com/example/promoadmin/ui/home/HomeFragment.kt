package com.example.promoadmin.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.api.shop.model.Shop
import com.example.promoadmin.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var storesAdapter: StoresListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val storesViewModel: HomeViewModel by viewModels({ requireActivity() })

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        storesViewModel.shops.observe(viewLifecycleOwner) {

            recyclerView = binding.recyclerView
            recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            storesAdapter = StoresListAdapter(it, ::handleOfferClick)
            recyclerView.adapter = storesAdapter
        }

        return root
    }

    private fun handleOfferClick(shop: Shop) {
//        findNavController().navigate(
//        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}