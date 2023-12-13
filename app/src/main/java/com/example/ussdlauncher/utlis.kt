package com.example.ussdlauncher

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri


/*class MainActivimty : ComponentActivity() {

    private val ussdCode = "*123#"
    private lateinit var telephonyManager: TelephonyManager

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UssdLauncherTheme {
                Surface {
                    val context = LocalContext.current
                    val permissionState = rememberPermissionState(Manifest.permission.CALL_PHONE)

                    telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    val phoneStateListener = UssdPhoneStateListener(context)

                    SideEffect {
                        if (permissionState.hasPermission) {
                            launchUssdCode(context)
                        }
                    }

                    if (permissionState.permissionRequested && !permissionState.hasPermission) {
                        // Handle denied or permanently denied permission state
                        // You can show an error message or request the permission again
                    }

                    PermissionRequestHandler(
                        state = permissionState,
                        onPermissionRequested = { permissionState.launchPermissionRequest() }
                    )

                    // Register the phone state listener when the activity is resumed
                    lifecycle.addObserver(object : LifecycleObserver {
                        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
                        fun onResume() {
                            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
                        }

                        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
                        fun onPause() {
                            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
                        }
                    })
                }
            }
        }
    }

    private fun launchUssdCode(context: Context) {
        val ussdUri = Uri.fromParts("tel", ussdCode, null)
        val ussdIntent = Intent(Intent.ACTION_CALL, ussdUri)
        startActivity(ussdIntent)
    }

    private inner class UssdPhoneStateListener(private val context: Context) : PhoneStateListener() {
        private var isUssdExecuted = false

        override fun onCallStateChanged(state: Int, phoneNumber: String?) {
            super.onCallStateChanged(state, phoneNumber)

            if (!isUssdExecuted) {
                // USSD code execution is triggered
                if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    isUssdExecuted = true

                    // Start a timer to wait for USSD response (e.g., 10 seconds)
                    val timer = object : CountDownTimer(10000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {}

                        override fun onFinish() {
                            // Timer finished, USSD execution is assumed to be completed
                            // Handle the logic based on the assumption
                            if (isUssdExecuted) {
                                Log.d("ussd", "Success")
                            } else {
                                Log.d("ussd", "Error")
                            }
                        }
                    }
                    timer.start()
                }
            } else {
                // USSD code execution completed or failed
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    isUssdExecuted = false
                }
            }
        }
    }
}*/


const val USSD_REQUEST_CODE = 123

fun launchUssdCode(context: Context, ussdCode: String) {
    val ussdUri = Uri.fromParts("tel", ussdCode, null)
    val ussdIntent = Intent(Intent.ACTION_CALL, ussdUri)

    if (context is Activity) {
        context.startActivityForResult(ussdIntent, USSD_REQUEST_CODE)
    } else {
        context.startActivity(ussdIntent)
    }
}
