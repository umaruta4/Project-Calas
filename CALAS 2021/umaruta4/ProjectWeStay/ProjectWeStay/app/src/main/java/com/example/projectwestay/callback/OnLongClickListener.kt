package com.example.projectwestay.callback

import android.os.Parcelable
import android.view.View

interface OnLongClickListener {
    fun onLongClick(view: View, itemId: Parcelable)
}