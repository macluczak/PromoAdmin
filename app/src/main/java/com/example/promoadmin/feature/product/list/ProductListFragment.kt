package com.example.promoadmin.feature.product.list
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
import com.example.api.product.model.Product
import com.example.promoadmin.databinding.FragmentProductListBinding
import com.example.promoadmin.feature.store.StoresViewModel
import com.example.promoadmin.feature.store.actions.StoreActionsFragmentArgs
import com.example.promoshow.feature.stores.details.ProductListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<StoreActionsFragmentArgs>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var productListAdapter: ProductListAdapter

    private val storesViewModel: StoresViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        binding.storeBackButton.setOnClickListener { backToStoreActions() }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        storesViewModel.fetchStoreDetails(args.shopObject.id.toString())

        storesViewModel.shop.observe(viewLifecycleOwner) { shop ->

            recyclerView = binding.recyclerView
            recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            productListAdapter = ProductListAdapter(shop.products.sortedBy { it.name }, ::handleOfferClick)
            recyclerView.adapter = productListAdapter

        }
    }

    private fun backToStoreActions() =
        findNavController().navigate(
            ProductListFragmentDirections.actionProductListFragmentToStoreActionsFragment(
                args.shopObject
            )
        )

    private fun handleOfferClick(product: Product) {
        findNavController().navigate(
            ProductListFragmentDirections.actionProductListFragmentToProductDetailsFragment(product, args.shopObject)
        )
    }
}
