package com.example.promoadmin.feature.store.actions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.promoadmin.databinding.FragmentStoreActionsBinding
import com.example.promoadmin.feature.store.StoresViewModel
import com.example.promoadmin.feature.store.actions.model.StoreActions
import com.example.promoadmin.util.loadImageWithGlide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreActionsFragment : Fragment() {

    private var _binding: FragmentStoreActionsBinding? = null
    private val args by navArgs<StoreActionsFragmentArgs>()
    private val storesViewModel: StoresViewModel by viewModels()
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var storesAdapter: StoreActionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoreActionsBinding.inflate(inflater, container, false)
        storesViewModel.fetchStoreDetails(args.shopObject.id.toString())

        binding.swipeLayout.setOnRefreshListener {
            refreshData()
        }


        return  binding.root
    }

    override fun onResume() {
        super.onResume()

        storesViewModel.shop.observe(viewLifecycleOwner){ shop ->
            if(shop != null) {
                loadImageWithGlide(binding.storeImage, shop.image)
                binding.storeLocation.text = shop.locationCode
                binding.storeName.text = shop.name

                recyclerView = binding.recyclerView
                recyclerView.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

                storesAdapter =
                    StoreActionsAdapter(StoreActions.values().toList(), ::handleOfferClick)
                recyclerView.adapter = storesAdapter
            }
        }
    }

    private fun refreshData() {
        storesViewModel.fetchStoreDetails(args.shopObject.id.toString())
        binding.swipeLayout.isRefreshing = false
    }


    private fun handleOfferClick(actions: StoreActions) {
//        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToStoreActivity(shop))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}