package com.example.projectwestay.callback

import android.os.Parcel
import android.os.Parcelable
import android.view.View
import java.util.*

interface OnClickListener<T> {
    fun onClick(view: View, item: T)
}