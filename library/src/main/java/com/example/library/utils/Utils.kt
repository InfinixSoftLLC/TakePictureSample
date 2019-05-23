package com.example.library.utils

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.widget.Toast
import com.example.library.R
import java.io.File
import java.util.*

fun Context.showMessage(message: String?) {
    if (message != null) Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun launchTakePictureIntent(context: Activity, appName: String, requestCode: Int): Uri? {
    val root = File("${Environment.getExternalStorageDirectory().absolutePath}/$appName/")
    if (!(root.mkdirs() || root.isDirectory)) return null
    val mOutputFileUri = Uri.fromFile(File(root, "TMP_${System.currentTimeMillis()}.jpg"))
    context.startActivityForResult(getPictureIntent(context, mOutputFileUri), requestCode)
    return mOutputFileUri
}

fun launchTakePictureIntent(fragment: Fragment, appName: String, requestCode: Int): Uri? {
    val root = File("${Environment.getExternalStorageDirectory().absolutePath}/$appName/")
    if (!(root.mkdirs() || root.isDirectory)) return null
    val mOutputFileUri = Uri.fromFile(File(root, "TMP_${System.currentTimeMillis()}.jpg"))
    fragment.startActivityForResult(getPictureIntent(fragment.context!!, mOutputFileUri), requestCode)
    return mOutputFileUri
}

private fun getPictureIntent(context: Context, outputFileUri: Uri): Intent {
    val cameraIntents = ArrayList<Intent>()
    val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    val listCam = context.packageManager.queryIntentActivities(captureIntent, 0)
    for (res in listCam) {
        val intent = Intent(captureIntent)
        intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
        intent.setPackage(res.activityInfo.packageName)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
        cameraIntents.add(intent)
    }

    val galleryIntent = Intent()
    galleryIntent.type = "image/*"
    galleryIntent.action = Intent.ACTION_PICK

    return Intent.createChooser(galleryIntent, context.getString(R.string.select_source))
        .putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toTypedArray<Parcelable>())
}