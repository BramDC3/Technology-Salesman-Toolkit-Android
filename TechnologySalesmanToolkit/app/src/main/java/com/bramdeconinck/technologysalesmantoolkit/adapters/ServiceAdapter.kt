package com.bramdeconinck.technologysalesmantoolkit.adapters

import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.fragments.ServiceDetailFragment
import com.bramdeconinck.technologysalesmantoolkit.fragments.ServiceListFragment
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.bramdeconinck.technologysalesmantoolkit.utils.SERVICE_ITEM
import com.bramdeconinck.technologysalesmantoolkit.utils.StringUtils.formatPrice
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
                        putParcelable(SERVICE_ITEM, item)
                    }
                }
                fragment.activity!!.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fl_service_list_detail_container, detailFragment)
                        .commit()
            } else {
                val arguments = Bundle().apply {
                    putParcelable(SERVICE_ITEM, item)
                }
                fragment.findNavController().navigate(R.id.toServiceDetail, arguments)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.service_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = services.value!![position]
        Glide.with(fragment).load(item.image).into(holder.imageView)
        holder.nameView.text = item.name
        holder.descriptionView.text = item.description
        holder.categoryView.text = item.category.toString()
        if (item.price != 0.0) holder.priceView.text = formatPrice(item.price)

        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = services.value!!.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.iv_service_logo
        val nameView: TextView = view.tv_service_name
        val descriptionView: TextView = view.tv_service_description
        val categoryView: TextView = view.tv_service_category
        val priceView: TextView = view.tv_service_price
    }
}