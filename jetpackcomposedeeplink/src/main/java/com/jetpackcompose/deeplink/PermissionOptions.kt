package com.jetpackcompose.deeplink

class NotificationPermissionTextProvider: PermissionTextProvider {

    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "It seems you permanently declined notification permission. "+
                    "You can go to the app settings to grant it."
        } else {
            "This app needs notification permission in order to receive important notification"
        }
    }
}