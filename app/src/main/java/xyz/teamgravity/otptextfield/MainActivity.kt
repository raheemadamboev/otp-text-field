package xyz.teamgravity.otptextfield

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import kotlinx.collections.immutable.persistentListOf
import xyz.teamgravity.otptextfield.ui.theme.OTPTextFieldTheme

class MainActivity : ComponentActivity() {

    private val viewmodel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OTPTextFieldTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { padding ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        val focusRequesters = remember { persistentListOf(FocusRequester(), FocusRequester(), FocusRequester(), FocusRequester()) }
                        val focusManager = LocalFocusManager.current
                        val keyboardManager = LocalSoftwareKeyboardController.current

                        LaunchedEffect(
                            key1 = viewmodel.focusedIndex,
                            block = {
                                viewmodel.focusedIndex?.let { index ->
                                    focusRequesters.getOrNull(index)?.requestFocus()
                                }
                            }
                        )

                        LaunchedEffect(
                            key1 = viewmodel.code,
                            key2 = keyboardManager,
                            block = {
                                val allNumbersEntered = viewmodel.code.none { it == null }
                                if (allNumbersEntered) {
                                    focusRequesters.forEach { it.freeFocus() }
                                    focusManager.clearFocus()
                                    keyboardManager?.hide()
                                }
                            }
                        )

                        OTPTextField(
                            code = viewmodel.code,
                            focusRequesters = focusRequesters,
                            onFocusChange = viewmodel::onFocusedIndexChange,
                            onNumberChange = { value, index ->
                                if (value != null) focusRequesters[index].freeFocus()
                                viewmodel.onEnterNumber(
                                    value = value,
                                    index = index
                                )
                            },
                            onKeyboardBack = viewmodel::onKeyboardBack
                        )

                        viewmodel.isValid?.let { isValid ->
                            Text(
                                text = stringResource(if (isValid) R.string.otp_is_valid else R.string.otp_is_invalid),
                                color = if (isValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}