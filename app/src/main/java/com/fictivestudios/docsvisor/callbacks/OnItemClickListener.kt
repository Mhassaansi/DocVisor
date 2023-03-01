package com.fictivestudios.docsvisor.callbacks

import android.view.View

interface OnItemClickListener {
    fun onItemClick(
        position: Int,
        `object`: Any?,
        view: View?,
        adapterName: String?
    )
}