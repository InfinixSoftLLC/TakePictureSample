package com.example.library.takePicture

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.library.R
import com.example.library.utils.REQUEST_CODE_CHOOSE_PICTURE
import com.example.library.utils.REQUEST_CODE_EXTERNAL_STORAGE
import com.example.library.utils.launchTakePictureIntent

abstract class TakePictureViewModel : BaseViewModel() {
    private var imageUri: Uri? = null
    protected var bitmap: Bitmap? = null

    private val bitmapLiveData: MutableLiveData<Bitmap> = MutableLiveData()

    private val target = object : SimpleTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            bitmap = resource
            bitmapLiveData.postValue(bitmap)
        }
    }

    fun getImageLiveData() = bitmapLiveData

    protected abstract fun getAppName(): String

    private fun isStoragePermissionGranted(activity: Activity) =
        ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    fun takePicture(activity: Activity) {
        if (isStoragePermissionGranted(activity))
            imageUri = launchTakePictureIntent(activity, getAppName(), REQUEST_CODE_CHOOSE_PICTURE)
        else
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_EXTERNAL_STORAGE
            )
    }

    fun takePicture(fragment: Fragment) {
        if (isStoragePermissionGranted(fragment.activity!!))
            imageUri = launchTakePictureIntent(fragment, getAppName(), REQUEST_CODE_CHOOSE_PICTURE)
        else
            fragment.requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_EXTERNAL_STORAGE
            )
    }

    fun handlePermissionResult(context: Activity, requestCode: Int, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_EXTERNAL_STORAGE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            imageUri = launchTakePictureIntent(context, getAppName(), REQUEST_CODE_CHOOSE_PICTURE)
        else
            showMessage(context.getString(R.string.missing_permissions))
    }

    fun handlePermissionResult(fragment: Fragment, requestCode: Int, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_EXTERNAL_STORAGE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            imageUri = launchTakePictureIntent(fragment, getAppName(), REQUEST_CODE_CHOOSE_PICTURE)
        else
            showMessage(fragment.getString(R.string.missing_permissions))
    }

    open fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_PICTURE) {
            try {
                if (data?.data != null) imageUri = Uri.parse(data.dataString)
                Glide.with(contextRef!!.get()!!).asBitmap().load(imageUri)
                    .apply(RequestOptions.overrideOf(400).centerCrop()).into(target)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}