# SmartPermissionKit

A modern Android library for effortless runtime permission handling using the Activity Result API.

![License](https://img.shields.io/github/license/GalAngel15/SmartPermissionKit)
![Platform](https://img.shields.io/badge/platform-Android-blue)

---

## ✨ Features

* ✅ Simple one-line permission requests
* ✅ Automatic rationale and settings redirect
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

* `PermissionHandler.kt` – Core class handling all permission request logic
* Supports rationale UI and permanent denial flow (redirects to settings)

---

## 🛡️ License

MIT License © 2024 [GalAngel15](https://github.com/GalAngel15)

---

> 💡 Found this helpful? Star the repo and share your feedback!
