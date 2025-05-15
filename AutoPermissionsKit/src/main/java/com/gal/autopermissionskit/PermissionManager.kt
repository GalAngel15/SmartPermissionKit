package com.gal.autopermissionskit

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PermissionHandler(
    private val activity: ComponentActivity,
    private val permission: String,
    private val rationaleMessage: String = "נדרשת הרשאה להפעלת הפיצ'ר הזה.",
    private val onGranted: () -> Unit,
    private val onDenied: (message: String) -> Unit
) {

    private var launcher: ActivityResultLauncher<String>? = null

    init {
        registerLauncher()
    }

    private fun registerLauncher() {
        launcher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission())
            { isGranted ->
                if (isGranted) {
                    onGranted()
                } else {
                    val shouldShowRationale = activity.shouldShowRequestPermissionRationale(permission)
                    if (!shouldShowRationale) {
                        // נדחה לצמיתות – שלח להגדרות
                        onDenied("ההרשאה נדחתה לצמיתות. נא לאשר אותה דרך הגדרות.")
                        //openAppSettings()
                        showSettingsRedirectDialog(permission)
                    } else {
                        // נדחה זמנית – הצג הסבר
                        onDenied(rationaleMessage)
                    }
                }
            }
    }

    fun checkAndRequest() {
        when {
            ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED -> {
                onGranted()
            }
            else -> {
                launcher?.launch(permission)
            }
        }
    }

    private fun showSettingsRedirectDialog(permission: String) {
        MaterialAlertDialogBuilder(activity)
            .setTitle("הרשאה חסומה")
            .setMessage("יש לאפשר את ההרשאה דרך ההגדרות כדי להמשיך.")
            .setPositiveButton(
                "פתח הגדרות"
            ) { dialog: DialogInterface?, which: Int -> openAppSettings() }
            .setNegativeButton("ביטול", null)
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            .apply {
            data = Uri.fromParts(
                "package",
                activity.packageName,
                null)
        }
        activity.startActivity(intent)
    }
}
