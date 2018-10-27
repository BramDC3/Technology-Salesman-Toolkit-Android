package com.bramdeconinck.technologysalesmantoolkit.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.fragments.ServiceDetailFragment
import kotlinx.android.synthetic.main.activity_service_detail.*

class ServiceDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(ServiceDetailFragment.ARG_ITEM_ID)

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity using a fragment transaction.
            val fragment = ServiceDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ServiceDetailFragment.ARG_ITEM_ID,
                            intent.getStringExtra(ServiceDetailFragment.ARG_ITEM_ID))
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
