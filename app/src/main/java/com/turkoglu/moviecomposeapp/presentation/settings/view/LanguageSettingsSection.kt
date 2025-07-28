package com.turkoglu.moviecomposeapp.presentation.settings.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.turkoglu.moviecomposeapp.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSettingSection(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit
) {
    val languageList = Constants.supportedLanguages
    var expanded by remember { mutableStateOf(false) }

    val selectedText =
        languageList.firstOrNull { it.first == selectedLanguage }?.second ?: selectedLanguage

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Dil Seçimi") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languageList.forEach { (code, name) ->
                DropdownMenuItem(
                    text = { Text(name) },
                    onClick = {
                        expanded = false
                        onLanguageSelected(code)
                    }
                )
            }
        }
    }
}
