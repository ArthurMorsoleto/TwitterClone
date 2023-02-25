package com.amb.camera

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class CameraActivityContract : ActivityResultContract<Unit, String>() {

    companion object {
        const val CAMERA_RESULT_DATA = "cameraResultData"
        const val CAMERA_ERROR_RESULT = "cameraResultError"
    }

    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(context, CameraActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String {
        return if (resultCode == Activity.RESULT_OK) {
            intent?.extras?.get(CAMERA_RESULT_DATA).toString()
        } else {
            CAMERA_ERROR_RESULT
        }
    }
}