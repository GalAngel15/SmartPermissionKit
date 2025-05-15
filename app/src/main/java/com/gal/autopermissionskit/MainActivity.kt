package com.gal.autopermissionskit

import android.hardware.Camera
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.Manifest

class MainActivity : AppCompatActivity() {

    private lateinit var surfaceView: SurfaceView
    private var camera: Camera? = null
    private lateinit var permissionHandler: PermissionHandler
    private var isSurfaceReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        surfaceView = findViewById(R.id.cameraPreview)

        // הרשאות
        permissionHandler = PermissionHandler(
            activity= this,
            permission = Manifest.permission.CAMERA,
            rationaleMessage = "נדרש לאשר את הרשאת המצלמה כדי להציג את ה-Preview",
            onGranted = {
                if (isSurfaceReady) startCamera()
            },
            onDenied = { message ->
                showDialog(message)
            }
        )

        // תזמון פתיחת מצלמה רק כשה־Surface מוכן
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                isSurfaceReady = true
                permissionHandler.checkAndRequest()
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                stopCamera()
                isSurfaceReady = false
            }
        })
    }

    private fun startCamera() {
        try {
            camera = Camera.open()
            camera?.setPreviewDisplay(surfaceView.holder)
            camera?.startPreview()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopCamera() {
        camera?.stopPreview()
        camera?.release()
        camera = null
    }

    private fun showDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("שימו לב")
            .setMessage(message)
            .setPositiveButton("סגור", null)
            .show()
    }
}
