package com.example.mezunproject.classes

import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener

class Spinner : OnItemSelectedListener{
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedItem = parent?.getItemAtPosition(position).toString()
        println(selectedItem)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}