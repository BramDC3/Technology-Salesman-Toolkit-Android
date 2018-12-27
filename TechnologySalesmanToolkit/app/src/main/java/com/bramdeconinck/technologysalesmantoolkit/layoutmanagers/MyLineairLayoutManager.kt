package com.bramdeconinck.technologysalesmantoolkit.layoutmanagers

import android.content.Context
import android.support.v7.widget.LinearLayoutManager

class MyLineairLayoutManager(context: Context?) : LinearLayoutManager(context) {

    override fun supportsPredictiveItemAnimations(): Boolean { return true }

}