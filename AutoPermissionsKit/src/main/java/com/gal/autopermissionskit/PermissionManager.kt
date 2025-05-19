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


/**
 * A generic Android runtime permission handler using Jetpack's Activity Result API.
 *
 * Handles the full permission flow:
 * - Checks if permission is granted
 * - Requests permission if needed
 * - Shows rationale if applicable
 * - Redirects to app settings if permission was permanently denied
 *
 * @param activity A ComponentActivity (e.g., AppCompatActivity)
 * @param permission The Android permission string (e.g., Manifest.permission.CAMERA)
 * @param rationaleMessage Optional message to display when permission is denied with rationale
 * @param onGranted Callback when the permission is granted
 * @param onDenied Callback with a message when the permission is denied or blocked
 *
 * Example usage:
 * ```
 * val handler = PermissionHandler(
 *     activity = this,
 *     permission = Manifest.permission.CAMERA,
 *     rationaleMessage = "Camera access is required to scan QR codes.",
 *     onGranted = { startCamera() },
 *     onDenied = { message -> showDialog(message) }
 * )
 *
 * handler.checkAndRequest()
 * ```
 */
class PermissionHandler(
    private val activity: ComponentActivity,
    private val permission: String,
    private val rationaleMessage: String = "נדרשת הרשאה להפעלת הפיצ'ר הזה.",
    private val onGranted: () -> Unit,
    private val onDenied: ((message: String) -> Unit)? = null
) {

    private var launcher: ActivityResultLauncher<String>? = null

    init {
        registerLauncher()
    }

    /**
     * Registers the Activity Result Launcher once during initialization.
     *
     *  Handles granted, denied, and permanently blocked cases.
     */
    private fun registerLauncher() {
        launcher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission())
            { isGranted ->
                if (isGranted) {
                    onGranted()
                } else {
                    val shouldShowRationale = activity.shouldShowRequestPermissionRationale(permission)
                    if (!shouldShowRationale) {
                        // Permission was denied permanently (Don't ask again)
                        val msg = "ההרשאה נדחתה לצמיתות. נא לאשר אותה דרך הגדרות."
                        onDenied?.invoke(msg) ?: showDefaultDialog(msg)
                        showSettingsRedirectDialog()
                    } else {
                        // Permission denied temporarily – show rationale message
                        onDenied?.invoke(rationaleMessage) ?: showDefaultDialog(rationaleMessage)
                    }
                }
            }
    }

    /**
     * Initiates the permission flow: if granted, calls onGranted(),
     * otherwise launches the system permission request dialog.
     */
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

    /**
     * Shows a default dialog with a message.
     * This is used when the user denies the permission but can still be prompted again.
     */
    private fun showDefaultDialog(message: String) {
        MaterialAlertDialogBuilder(activity)
            .setTitle("שימו לב")
            .setMessage(message)
            .setPositiveButton("סגור", null)
            .show()
    }

    /**
     * Shows a dialog that redirects the user to the app's settings screen,
     * typically used when the permission is permanently denied.
     */
    private fun showSettingsRedirectDialog() {
        MaterialAlertDialogBuilder(activity)
            .setTitle("הרשאה חסומה")
            .setMessage("יש לאפשר את ההרשאה דרך ההגדרות כדי להמשיך.")
            .setPositiveButton(
                "פתח הגדרות"
            ) { dialog: DialogInterface?, which: Int -> openAppSettings() }
            .setNegativeButton("ביטול", null)
            .show()
    }
    /**
     * Opens the application details settings page so the user can manually grant permissions.
     */
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

    companion object{
        /**
         * Convenience method to create a PermissionHandler with a default rationale message.
         */
        fun withDefaultRationale(
            activity: ComponentActivity,
            permission: String,
            rationaleMessage: String = "נדרשת הרשאה להפעלת הפיצ'ר הזה.",
            onGranted: () -> Unit,
        ): PermissionHandler {
            return PermissionHandler(
                activity = activity,
                permission = permission,
                rationaleMessage = rationaleMessage,
                onGranted = onGranted,
                onDenied = null
            ).apply {
                checkAndRequest()
            }
        }
    }
}
