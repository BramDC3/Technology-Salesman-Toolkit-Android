package com.bramdeconinck.technologysalesmantoolkit.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.fragments.ServiceDetailFragment
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import kotlinx.android.synthetic.main.activity_service_detail.*

class ServiceDetailActivity : AppCompatActivity() {

    private var service: Service? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_detail)

        setSupportActionBar(toolbar)

        service = intent.getParcelableExtra(ServiceDetailFragment.ARG_ITEM_ID)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = service?.name

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity using a fragment transaction.
            val fragment = ServiceDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ServiceDetailFragment.ARG_ITEM_ID, service)
                }
            }

            supportFragmentManager.beginTransaction()
                    .add(R.id.service_detail_container, fragment)
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    navigateUpTo(Intent(this, ServiceListActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}
