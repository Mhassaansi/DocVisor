package com.fictivestudios.docsvisor.callbacks

import com.fictivestudios.docsvisor.model.SpinnerModel

interface OnSpinnerOKPressedListener {
    fun onSingleSelection(data: SpinnerModel, selectedPosition: Int)
}