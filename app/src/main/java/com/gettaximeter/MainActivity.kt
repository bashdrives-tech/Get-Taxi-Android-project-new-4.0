package com.gettaximeter

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.gettaximeter.data.model.UserRole
import com.gettaximeter.ui.screens.*
import com.gettaximeter.ui.theme.GetTaxiMeterTheme
import com.gettaximeter.ui.viewmodel.TaxiViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: TaxiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            GetTaxiMeterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavigation(viewModel)
                }

                // Centralized Toast feedback collector
                val toastMsg by viewModel.toastMessage.collectAsState()
                LaunchedEffect(toastMsg) {
                    toastMsg?.let {
                        Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
                        viewModel.clearToast()
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavigation(viewModel: TaxiViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val selectedTrip by viewModel.selectedTrip.collectAsState()

    when (currentScreen) {
        "role_select" -> RoleSelectScreen(viewModel)
        "login_admin" -> LoginScreen(viewModel, UserRole.ADMIN)
        "login_driver" -> LoginScreen(viewModel, UserRole.DRIVER)
        "admin_dashboard" -> AdminDashboardScreen(viewModel)
        "driver_home" -> DriverHomeScreen(viewModel)
        "live_meter" -> LiveMeterScreen(viewModel)
        "receipt_screen" -> {
            if (selectedTrip != null) {
                ReceiptScreen(viewModel, selectedTrip!!)
            } else {
                RoleSelectScreen(viewModel)
            }
        }
        else -> RoleSelectScreen(viewModel)
    }
}
