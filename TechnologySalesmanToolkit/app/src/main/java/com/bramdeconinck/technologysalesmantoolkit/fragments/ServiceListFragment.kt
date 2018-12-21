package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.EditText
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.adapters.ServiceAdapter
import com.bramdeconinck.technologysalesmantoolkit.databinding.FragmentServiceListBinding
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IToastMaker
import com.bramdeconinck.technologysalesmantoolkit.models.Category
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.makeToast
import com.bramdeconinck.technologysalesmantoolkit.viewmodels.ServiceViewModel
import kotlinx.android.synthetic.main.fragment_service_list.view.*

class ServiceListFragment : Fragment(), IToastMaker {

    private lateinit var serviceViewModel: ServiceViewModel
    private lateinit var binding: FragmentServiceListBinding
    private lateinit var serviceAdapter: ServiceAdapter

    // Variable to check whether the app is running on a tablet or not
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_service_list, container, false)

        serviceViewModel = ViewModelProviders.of(activity!!).get(ServiceViewModel::class.java)

        val rootView = binding.root
        binding.serviceViewModel = serviceViewModel
        binding.setLifecycleOwner(activity)

        val services = serviceViewModel.services

        // If the service detail container is not null,
        // then the app is running on a tablet
        if (rootView.service_detail_container != null) twoPane = true

        serviceAdapter = ServiceAdapter(this, services, twoPane)

        rootView.service_list.adapter = serviceAdapter

        services.observe(this, Observer { serviceAdapter.notifyDataSetChanged() })

        serviceViewModel.servicesErrorOccurred.observe(this, Observer { showToast(R.string.fetching_data_error) })

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()

        serviceViewModel.clearFilters()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_search_bar, menu)
        inflater?.inflate(R.menu.menu_search_filters, menu)

        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        val searchViewText = searchView.findViewById<EditText>(android.support.v7.appcompat.R.id.search_src_text)

        searchViewText.hint = getString(R.string.search_hint)
        searchViewText.setHintTextColor(Color.WHITE)
        searchViewText.setTextColor(Color.WHITE)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean { return false }
            override fun onQueryTextChange(newText: String): Boolean {
                serviceViewModel.applySearchQuery(newText)
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter_everything -> {
                serviceViewModel.applyCategoryQuery(null)
                return true
            }
            R.id.action_filter_windows -> {
                serviceViewModel.applyCategoryQuery(Category.Windows)
                return true
            }
            R.id.action_filter_android -> {
                serviceViewModel.applyCategoryQuery(Category.Android)
                return true
            }
            R.id.action_filter_apple -> {
                serviceViewModel.applyCategoryQuery(Category.Apple)
                return true
            }
            R.id.action_filter_other -> {
                serviceViewModel.applyCategoryQuery(Category.Andere)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showToast(message: Int) { makeToast(context!!, message) }

}
