package com.example.projectwestay.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Parcel
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.projectwestay.R
import com.example.projectwestay.callback.OnClickListener
import com.example.projectwestay.callback.OnLongClickListener
import com.example.projectwestay.databinding.LayoutHomestayBinding
import com.example.projectwestay.response.HomestayResponse
import com.example.projectwestay.response.Response

class HomestayAdapter (val mCtx : Context, val layoutResId : Int, val argList : Array<HomestayResponse>) : ArrayAdapter<HomestayResponse>(mCtx, layoutResId, argList) {
    private var onClickListener: OnClickListener<HomestayResponse>? = null
    private var onLongClickListener: OnLongClickListener? = null
    override fun getView(position: Int, convertView : View?, parent: ViewGroup) : View {
        val binding = LayoutHomestayBinding.inflate(LayoutInflater.from(context))
        val view = binding.root
        val item = getItem(position)

        if (onClickListener != null){
            view.setOnClickListener{ v ->
                onClickListener?.onClick(v, item as HomestayResponse)
            }
        }

        if (onLongClickListener != null){
            view.setOnLongClickListener{v ->
                onLongClickListener?.onLongClick(v, item as HomestayResponse)
                return@setOnLongClickListener true
            }
        }
        val encodedImage = item?.thumbnail
        val decodedBytes : ByteArray = Base64.decode(encodedImage, Base64.DEFAULT)
        val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0,decodedBytes.size)

        binding.textViewNamaHomestay.text = item?.namaHomestay
        binding.textViewDeskripsi.text = item?.fasilitas + " " + item?.jenisKamar + " " + item?.lokasi
        binding.imageViewThumbnail.setImageBitmap(decodedBitmap)
        return view
    }

    fun setOnClickListener(listener: OnClickListener<HomestayResponse>){
        onClickListener = listener
    }

    fun setOnLongClickListener(listener: OnLongClickListener){
        onLongClickListener = listener
    }
}