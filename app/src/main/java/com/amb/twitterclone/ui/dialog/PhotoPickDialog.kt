package com.amb.twitterclone.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import com.amb.twitterclone.R

class PhotoPickDialog(context: Context) : Dialog(context) {

    private var pickListener: PhotoPickListener
    private val takePhotoButton by lazy { findViewById<TextView>(R.id.button_take_photo) }
    private val choosePhotoButton by lazy { findViewById<TextView>(R.id.button_choose_photo) }

    init {
        if (context is PhotoPickListener) {
            pickListener = context
        } else {
            throw java.lang.IllegalArgumentException("$context must implement PhotoPickListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_photo_pick)

        takePhotoButton.setOnClickListener {
            dismiss()
            pickListener.onTakePhotoClick()
        }
        choosePhotoButton.setOnClickListener {
            dismiss()
            pickListener.onChoosePhotoClick()
        }
    }
}
