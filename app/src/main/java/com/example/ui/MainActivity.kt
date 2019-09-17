package com.example.ui

import android.os.Bundle
import android.widget.Toast
import com.example.example.R
import com.example.interfaces.ActivityDefaultBehavior
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity(), ActivityDefaultBehavior {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun showError(errorText: String) {
        //todo return universe error showing solution if needed. For now toast is enough
        Toast.makeText(this, errorText, Toast.LENGTH_LONG).show()
    }
}