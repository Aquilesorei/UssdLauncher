package com.example.ussdlauncher

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.accessibility.AccessibilityManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ussdlauncher.ui.theme.UssdLauncherTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {




    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UssdLauncherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                   var enabled  by remember {
                       mutableStateOf(false)
                   }
                    val context = LocalContext.current
                    val p = rememberMultiplePermissionsState(permissions = listOf(
                        Manifest.permission.CALL_PHONE,
                    ))
                    val permissionState = rememberPermissionState(
                        Manifest.permission.CALL_PHONE,
                    )
                    SideEffect {
                        p.launchMultiplePermissionRequest()

                        CoroutineScope(Dispatchers.IO).launch {
                            while (true){
                                delay(1000)
                                enabled = context.isAccessibilityEnabled()
                                if(enabled) break
                            }
                        }
                    }





                    if(permissionState.status.isGranted) {


                        if(enabled) {
                            LaunchUriComposable(onClick = { code ->
                                startService(Intent(this, USSDAccessibilityService::class.java))
                                launchUssdCode(code)
                            }, message = MSG.value)
                        }
                        else{
                            AccessibilitySettingsScreen()
                        }
                    }

                }
            }
        }
    }

    private fun launchUssdCode( ussdCode :String) {


        val ussdUri = Uri.fromParts("tel", ussdCode, null)
        val ussdIntent = Intent(Intent.ACTION_CALL, ussdUri)
        startActivity(ussdIntent)
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchUriComposable(onClick : (String) -> Unit, message : String) {

    val context = LocalContext.current

    // Register the accessibility service
/*    DisposableEffect(Unit) {
        val accessibilityService = USSDAccessibilityService::class.java
        val componentName = ComponentName(context, accessibilityService)

        context.packageManager.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        onDispose {
            // Unregister the accessibility service when the composable is disposed
            context.packageManager.setComponentEnabledSetting(
                componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }
    }*/

    var uri by remember{
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(message)
        TextField(
            value = uri,
            onValueChange = { uri = it },
            label = { Text("Enter URI") },
            modifier = Modifier.padding(bottom = 16.dp),
            textStyle = MaterialTheme.typography.bodyMedium,
            keyboardOptions =  KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Button(
            onClick = {
            onClick(uri)
            }
        ) {
            Text("Launch URI")
        }
    }
}


fun Context.isAccessibilityEnabled(): Boolean {
    val accessibilityManager = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    return accessibilityManager.isEnabled
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UssdLauncherTheme {
        Greeting("Android")

    }
}
