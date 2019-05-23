package com.example.takepicture

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.library.utils.showMessage
import kotlinx.android.synthetic.main.activity_picture.*

class PictureActivity : AppCompatActivity() {
    private lateinit var viewModel: PictureViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(PictureViewModel::class.java)
        viewModel.getImageLiveData().observe(this, Observer { ivProfile.setImageBitmap(it) })
        viewModel.getShowMessageLiveData().observe(this, Observer { showMessage(it) })
        viewModel.init(this)
    }

    fun onClickTakePicture(v: View) = viewModel.takePicture(this)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) =
        viewModel.handlePermissionResult(this, requestCode, grantResults)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) =
        viewModel.handleActivityResult(requestCode, resultCode, data)
}