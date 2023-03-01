package com.fictivestudios.docsvisor.callbacks

import android.view.View


interface OnSubItemClickListener {
    fun onItemClick(
        parentPosition: Int,
        childPosition: Int,
        `object`: Any?,
        view: View?,
        adapterName: String?
    )
}