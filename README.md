# SmartPermissionKit

A modern Android library for effortless runtime permission handling using the Activity Result API.

[![](https://jitpack.io/v/GalAngel15/SmartPermissionKit.svg)](https://jitpack.io/#GalAngel15/SmartPermissionKit)

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

Add JitPack to your `settings.gradle.kts` (or `project-level build.gradle` if you're not using Kotlin DSL):

```kotlin
dependencyResolutionManagement {
    repositories {
        maven("https://jitpack.io")
        google()
        mavenCentral()
    }
}
````

Then, add the library to your app/module `build.gradle.kts` dependencies:

```kotlin
dependencies {
    implementation("com.github.GalAngel15:SmartPermissionKit:1.0.0")
}
```

> Replace `1.0.0` with the latest version if needed.
> See the badge above or visit [JitPack](https://jitpack.io/#GalAngel15/SmartPermissionKit) for updates.

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
