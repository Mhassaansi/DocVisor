package com.fictivestudios.docsvisor.callbacks

import com.fictivestudios.docsvisor.adapter.SpinnerDialogSingleSelectAdapter

interface OnSpinnerItemClickListener {
    fun onItemClick(position: Int, anyObject: Any, singleSelectAdapter: SpinnerDialogSingleSelectAdapter)
}