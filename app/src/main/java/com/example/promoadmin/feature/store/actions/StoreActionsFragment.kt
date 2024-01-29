package com.example.promoadmin.feature.store.actions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.api.shop.model.Shop
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
    lateinit var shopObject: Shop
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var storesAdapter: StoreActionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoreActionsBinding.inflate(inflater, container, false)

        storesViewModel.shop.observe(viewLifecycleOwner){ shop ->
            if(shop != null) {
                shopObject = shop
                loadImageWithGlide(binding.storeImage, shop.image)
                binding.storeLocation.text = shop.locationCode
                binding.storeName.text = shop.name

                recyclerView = binding.recyclerView
                recyclerView.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

                storesAdapter =
                    StoreActionsAdapter(StoreActions.values().toList(), ::handleActionClick)
                recyclerView.adapter = storesAdapter
            }
        }

        binding.swipeLayout.setOnRefreshListener {
            refreshData()
        }
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshData()
    }

    private fun refreshData() {
        storesViewModel.fetchStoreDetails(args.shopObject.id.toString())
        binding.swipeLayout.isRefreshing = false
    }

    private fun handleActionClick(actions: StoreActions) = when(actions){
        StoreActions.EditStore -> moveToStoreDetails()
        StoreActions.EditProduct -> moveToProductList()
        StoreActions.AddProduct -> moveToProductCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun moveToStoreDetails()=
        findNavController().navigate(StoreActionsFragmentDirections.actionStoreActionsFragmentToStoreDetailsFragment(shopObject))

    private fun moveToProductList()=
        findNavController().navigate(StoreActionsFragmentDirections.actionStoreActionsFragmentToProductListFragment(shopObject))

    private fun moveToProductCreate()=
        findNavController().navigate(StoreActionsFragmentDirections.actionStoreActionsFragmentToProductCreateFragment(shopObject))

}