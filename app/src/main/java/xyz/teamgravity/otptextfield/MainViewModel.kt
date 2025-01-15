package xyz.teamgravity.otptextfield

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

class MainViewModel : ViewModel() {

    var code: ImmutableList<Int?> by mutableStateOf(persistentListOf(null, null, null, null))
        private set

    var focusedIndex: Int? by mutableStateOf(null)
        private set

    var isValid: Boolean? by mutableStateOf(null)
        private set

    private fun getPreviousFocusedIndex(): Int? {
        return focusedIndex?.minus(1)?.coerceAtLeast(0)
    }

    private fun getFirstEmptyFieldIndexAfterFocusedIndex(): Int? {
        val currentFocusedIndex = focusedIndex ?: return null

        code.forEachIndexed { index, number ->
            if (index <= currentFocusedIndex) return@forEachIndexed
            if (number == null) return index
        }

        return currentFocusedIndex
    }

    private fun getNextFocusedIndex(): Int? {
        if (focusedIndex == null) return null
        if (focusedIndex == 3) return focusedIndex
        return getFirstEmptyFieldIndexAfterFocusedIndex()
    }

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    fun onFocusedIndexChange(value: Int) {
        focusedIndex = value
    }

    fun onEnterNumber(
        value: Int?,
        index: Int
    ) {
        code = code.mapIndexed { currentIndex, number ->
            if (currentIndex == index) value else number
        }.toImmutableList()
        focusedIndex = if (value == null) focusedIndex else getNextFocusedIndex()
        isValid = if (code.none { it == null }) code.joinToString("") == "7777" else null
    }

    fun onKeyboardBack() {
        val previousIndex = getPreviousFocusedIndex()
        code = code.mapIndexed { index, number ->
            if (index == previousIndex) null else number
        }.toImmutableList()
        focusedIndex = previousIndex
    }
}