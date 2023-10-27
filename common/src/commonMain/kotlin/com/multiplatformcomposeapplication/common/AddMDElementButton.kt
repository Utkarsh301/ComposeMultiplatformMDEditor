package com.multiplatformcomposeapplication.common

import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.PopupProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach


//@Composable
//fun RichTextStyleButton(
//    onClick: () -> Unit,
//    icon: ImageVector,
//    tint: Color? = null,
//    isSelected: Boolean
//)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun AddMDElementButton(
    modifier: Modifier = Modifier, mdElement: MDElement, onClick: (MDElement) -> Unit
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    var openDialog by remember {
        mutableStateOf(false)
    }
    var columnRows by remember {
        mutableStateOf(Pair("2", "2"))
    }
//    val tooltipState = remember { PlainTooltipState() }
//    val interactionSource = remember { MutableInteractionSource() }
//    val isHovered by interactionSource.collectIsHoveredAsState()
//    LaunchedEffect(isHovered) {
//        delay(1500)
//        if (isHovered) tooltipState.show() else tooltipState.dismiss()
//    }
//    PlainTooltipBox(
//        tooltip = {
//            Text(mdElement.description.toString())
//        }
//        , tooltipState = tooltipState
//    ) {
    IconButton(modifier = modifier.focusProperties { canFocus = false }/*.hoverable(interactionSource)*/, onClick = {
        when {
            mdElement.type == ElementTypes.TABLE -> {
                openDialog = true
            }

            mdElement.subOptions == null -> {
                onClick(mdElement)
            }

            else -> {
                isExpanded = true
            }
        }
    }) {

        if (mdElement.imageVector != null) {
            Icon(imageVector = mdElement.imageVector, contentDescription = mdElement.description)
        } else {
            Text(mdElement.title.toString())
        }
    }
    if (openDialog) {
        println("open dialog")
        Dialog(onDismissRequest = {
            openDialog = false
        }) {
            Card(
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp, 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(/*modifier = Modifier.weight(1f),*/
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = {
                            Text("Columns")
                        },
                        value = columnRows.first,
                        onValueChange = {
                            columnRows = columnRows.copy(first = it)
                        })
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(/*modifier = Modifier.weight(1f),*/
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = {
                            Text("Rows")
                        },
                        value = columnRows.second,
                        onValueChange = {
                            columnRows = columnRows.copy(second = it)
                        })
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        openDialog = false
                        onClick(mdElement.copy(content = columnRows.getMDContent()))
                    }) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
    mdElement.subOptions?.let {
        DropdownMenu(expanded = isExpanded, onDismissRequest = {
            isExpanded = false
        }) {
            it.forEachIndexed { index, mdElement ->
                DropdownMenuItem(onClick = {
                    onClick(mdElement)
                    isExpanded = false
                }, text = {
                    Text(
                        text = mdElement.title.toString(),
                        style = mdElement.titleTextStyle ?: LocalTextStyle.current
                    )
                })
            }
        }
    }


//    }

}

public data class MDElement(
    val imageVector: ImageVector? = null,
    val title: String? = null,
    val titleTextStyle: TextStyle? = null,
    val description: String? = null,
    val content: String? = null,
    val subOptions: MutableList<MDElement>? = null,
    val cursorDecrease: Int? = null,
    val isAtStart: Boolean = false,
    val type: ElementTypes,
    val regex: Regex? = null
)

public enum class ElementTypes {
    HEADING, LIST, TEXT_FORMATTING, SUB_OPTIONS, TABLE, LINK, CODE, YAML
}

public fun Pair<String, String>.getMDContent(): String {
    if (second.toInt() <= 0 || first.toInt() <= 0) {
        return "Invalid table dimensions."
    }

    val headerRow = (1..first.toInt()).joinToString(" | ", prefix = "| ", postfix = " |") { "" }
    val separatorRow = (1..first.toInt()).joinToString(" | ", prefix = "|", postfix = "|") { "---" }

    val dataRows = (1..second.toInt()).joinToString("\n") { rowNumber ->
        (1..first.toInt()).joinToString(" | ", prefix = "| ") { "" } + " |"
    }

    return """
$headerRow
$separatorRow
$dataRows
    """.trimIndent()

}