package xyz.teamgravity.otptextfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList

@Composable
fun OTPTextField(
    code: ImmutableList<Int?>,
    focusRequesters: ImmutableList<FocusRequester>,
    onFocusChange: (index: Int) -> Unit,
    onNumberChange: (number: Int?, index: Int) -> Unit,
    onKeyboardBack: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterHorizontally
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        code.forEachIndexed { index, number ->
            OTPField(
                number = number,
                focusRequester = focusRequesters[index],
                onFocusChange = { value ->
                    if (value) onFocusChange(index)
                },
                onNumberChange = { value ->
                    onNumberChange(value, index)
                },
                onKeyboardBack = onKeyboardBack,
                modifier = Modifier
                    .weight(1F)
                    .aspectRatio(1F)
            )
        }
    }
}