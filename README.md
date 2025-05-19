# SmartPermissionKit

A modern Android library for effortless runtime permission handling using the Activity Result API.

![License](https://img.shields.io/github/license/GalAngel15/SmartPermissionKit)
![Platform](https://img.shields.io/badge/platform-Android-blue)

---

## ✨ Features

* ✅ Simple one-line permission requests with default UI
* ✅ Optional `onDenied` handler – use yours or built-in dialogs
* ✅ Automatic rationale handling and settings redirection
* ✅ Fully generic – works with any Android permission
* ✅ Based on Jetpack's Activity Result API

---

## 📦 Installation

Clone the repository and add the module manually to your project:

```groovy
implementation project(":smartpermissionkit")
```

> Gradle/Maven publishing coming soon.

---

## 🚀 Usage

```kotlin
// Minimal usage with built-in denial dialog
PermissionHandler.withDefaultRationale(
    activity = this,
    permission = Manifest.permission.CAMERA,
    rationaleMessage = "Camera access is needed to scan QR codes.",
    onGranted = {
        startCamera()
    }
)
```
```kotlin
// Full customization with your own denial handler
val permissionHandler = PermissionHandler(
    activity = this,
    permission = Manifest.permission.CAMERA,
    rationaleMessage = "Camera access is required to scan QR codes.",
    onGranted = {
        // Permission granted – proceed
    },
    onDenied = { message ->
        showDialog(message) // Show rationale or redirect explanation
    }
)

permissionHandler.checkAndRequest()
```

---

## ⚙️ Customize

Use with any Android permission:

```kotlin
Manifest.permission.ACCESS_FINE_LOCATION
Manifest.permission.RECORD_AUDIO
Manifest.permission.READ_EXTERNAL_STORAGE
```

---

## 📂 Structure

* `PermissionHandler.kt` – Core class for permission request flow
  - Optional `onDenied` callback
  - Built-in rationale message and fallback dialogs
  - Redirects to app settings if permission is permanently denied
  - Includes shortcut `withDefaultRationale(...)` for one-liner usage

---

## 🛡️ License

MIT License © 2024 [GalAngel15](https://github.com/GalAngel15)

---

> 💡 Found this helpful? Star the repo and share your feedback!
