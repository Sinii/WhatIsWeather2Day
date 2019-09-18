package com.example.interfaces

import android.widget.SeekBar

abstract class SimpleSeekBarChangeListener : SeekBar.OnSeekBarChangeListener {
    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}