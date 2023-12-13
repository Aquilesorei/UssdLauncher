package com.example.ussdlauncher

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.compose.runtime.mutableStateOf


var  MSG =  mutableStateOf("")

class USSDAccessibilityService : AccessibilityService() {

    private var TAG = "XXXX"

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        Log.d(TAG, "onAccessibilityEvent")
        val text = event.text.toString()
        if (event.className == "android.app.AlertDialog") {
            performGlobalAction(GLOBAL_ACTION_BACK)
            Log.d(TAG, text)
            MSG.value = text;
            val intent = Intent("com.times.ussd.action.REFRESH")
            intent.putExtra("message", text)


        }
    }

    override fun onInterrupt() {}

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "onServiceConnected")
        val info = AccessibilityServiceInfo()
        info.flags = AccessibilityServiceInfo.DEFAULT
        info.packageNames = arrayOf("com.android.phone")
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        serviceInfo = info
    }
}
