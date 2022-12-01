package com.example.projectwestay.callback

interface CRUDCallback {
    fun onUpdate(itemId: Int)

    fun onDelete(itemId: Int)
}