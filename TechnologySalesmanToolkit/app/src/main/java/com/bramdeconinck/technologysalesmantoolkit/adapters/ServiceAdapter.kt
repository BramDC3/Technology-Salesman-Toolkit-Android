package com.bramdeconinck.technologysalesmantoolkit.adapters

import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.activities.MainActivity
import com.bramdeconinck.technologysalesmantoolkit.fragments.ServiceDetailFragment
import com.bramdeconinck.technologysalesmantoolkit.fragments.ServiceListFragment
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.service_list_content.view.*

class ServiceAdapter(
        private val fragment: ServiceListFragment,
        private val services: MutableLiveData<List<Service>>,
        private val twoPane: Boolean) :
        RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Service
            if (twoPane) {
                val detailFragment = ServiceDetailFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ServiceDetailFragment.ARG_ITEM_ID, item)
                    }
                }
                fragment.activity!!.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.service_detail_container, detailFragment)
                        .commit()
            } else {
                val arguments = Bundle().apply {
                    putParcelable(ServiceDetailFragment.ARG_ITEM_ID, item)
                }
                fragment.findNavController().navigate(R.id.toServiceDetail, arguments)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.service_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = services.value!![position]
        Glide.with(fragment).load(item.image).into(holder.afbeeldingView)
        holder.naamView.text = item.name
        holder.beschrijvingView.text = item.description
        holder.categorieView.text = item.category.toString()
        if (item.price != 0.0) holder.prijsView.text = String.format("€ %.2f", item.price)

        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = services.value!!.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val afbeeldingView = view.iv_afbeelding
        val naamView = view.tv_naam
        val beschrijvingView = view.tv_beschrijving
        val categorieView = view.tv_categorie
        val prijsView = view.tv_prijs
    }
}