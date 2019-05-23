package com.example.takepicture

import com.example.library.takePicture.TakePictureViewModel

class PictureViewModel : TakePictureViewModel() {
    override fun getAppName(): String = contextRef!!.get()!!.getString(R.string.app_name)
}