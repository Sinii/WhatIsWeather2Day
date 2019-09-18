package com.example.utils

import android.widget.SeekBar
import androidx.databinding.BindingAdapter

@BindingAdapter("addSeekListener")
fun addSeekListener(view: SeekBar, listener: SeekBar.OnSeekBarChangeListener?) {
    listener?.let {
        view.setOnSeekBarChangeListener(it)
    }
}