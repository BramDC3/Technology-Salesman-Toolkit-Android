package com.bramdeconinck.technologysalesmantoolkit.adapters

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentStatePagerAdapter
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
import com.bramdeconinck.technologysalesmantoolkit.models.Instruction
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.bramdeconinck.technologysalesmantoolkit.utils.StringUtils.formatPrice
import com.bramdeconinck.technologysalesmantoolkit.viewmodels.ServiceViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.service_list_content.view.*

/**
 * [ServiceAdapter] is a [RecyclerView] Adapter used for the RecyclerView of the ServiceListFragment.
 */
class ServiceAdapter(
        private val fragment: ServiceListFragment,
        private val services: LiveData<List<Service>>,
        private val twoPane: Boolean) :
        RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {

    /**
     * The [onClickListener] is used to perform an action when a [Service] gets clicked.
     */
    private val onClickListener: View.OnClickListener

    /**
     * The [serviceViewModel] contains all the information of [Service] objects and their instructions.
     */
    private val serviceViewModel = ViewModelProviders.of(fragment.activity!!).get(ServiceViewModel::class.java)

    init {
        onClickListener = View.OnClickListener { v ->
            /**
             * Instead of sending the selected [Service] as a parcel,
             * the selected [Service] is stored in the [ServiceViewModel].
             */
            serviceViewModel.selectedService.value = v.tag as Service

            /**
             * Clearing the instructions of the previously clicked [Service].
             */
            serviceViewModel.clearInstructions()

            /**
             * When [twoPane] is true, the user is using a tablet.
             * If the user is using a tablet, the [ServiceDetailFragment] should open in a container of the tablet layout of the [ServiceListFragment].
             * If the user is using a phone, the Navigation Controller should navigate to the [ServiceDetailFragment].
             */
            if (twoPane) {
                fragment.activity!!.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fl_service_list_detail_container, ServiceDetailFragment())
                        .commit()
            } else { fragment.findNavController().navigate(R.id.toServiceDetail) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.service_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = services.value!![position]

        /**
         * [Glide] is used to load the image of the [Service] into the ImageView.
         */
        Glide.with(fragment)
            .load(item.image)
            .apply(RequestOptions()
                .error(R.drawable.logo)
                .placeholder(R.drawable.logo))
            .into(holder.imageView)

        holder.nameView.text = item.name
        holder.descriptionView.text = item.description
        holder.categoryView.text = item.category.toString()

        /**
         * If the [Service] doesn't have a price (price is 0), it shouldn't be shown.
         */
        if (item.price != 0.0) holder.priceView.text = formatPrice(item.price)

        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    /**
     * To preserve the animation of the [RecyclerView] of the [ServiceListFragment],
     * the id of a [Service] item should be truly unique and not the [position] itself.
     * That's why the hashcode is used.
     */
    override fun getItemId(position: Int): Long {
        return services.value!![position].hashCode().toLong()
    }

    /**
     * Function that returns the amount of [Service] objects.
     */
    override fun getItemCount() = services.value!!.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.iv_service_logo
        val nameView: TextView = view.tv_service_name
        val descriptionView: TextView = view.tv_service_description
        val categoryView: TextView = view.tv_service_category
        val priceView: TextView = view.tv_service_price
    }
}