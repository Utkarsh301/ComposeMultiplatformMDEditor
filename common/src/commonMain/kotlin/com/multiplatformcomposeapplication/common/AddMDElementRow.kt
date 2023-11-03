package com.multiplatformcomposeapplication.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
public fun AddMDElementRow(
    modifier: Modifier = Modifier,
    onClick: (MDElement) -> Unit
) {
    val mdElements = rememberMDElements()

    LazyRow(
        modifier = modifier
    ) {
        items(mdElements) { mdElement ->
            AddMDElementButton(
                modifier = Modifier,
                mdElement = mdElement,
                onClick = onClick
            )
            Spacer(modifier = Modifier.width(2.dp))
        }
    }
}

@Composable
public fun rememberMDElements(): MutableList<MDElement> {
    return mutableListOf(
        MDElement(
            imageVector = Icons.Default.Title, description = "Add Heading",
            subOptions = mutableListOf(
                MDElement(
                    title = "Heading 1",
                    content = "# ",
                    description = "Add Heading 1",
                    titleTextStyle = MaterialTheme.typography.titleLarge,
                    isAtStart = true,
                    type = ElementTypes.HEADING,
                    regex = Regex("#{1,3} (.*)")
                ),
                MDElement(
                    title = "Heading 2",
                    content = "## ",
                    description = "Add Heading 2",
                    titleTextStyle = MaterialTheme.typography.titleMedium,
                    isAtStart = true,
                    type = ElementTypes.HEADING,
                    regex = Regex("#{1,3} (.*)")
                ),
                MDElement(
                    title = "Heading 3",
                    content = "### ",
                    description = "Add Heading 3",
                    titleTextStyle = MaterialTheme.typography.titleSmall,
                    isAtStart = true,
                    type = ElementTypes.HEADING,
                    regex = Regex("#{1,3} (.*)")
                )
            ),
            type = ElementTypes.SUB_OPTIONS
        ),
        MDElement(
            imageVector = Icons.Default.FormatBold,
            content = "****",
            description = "Add Bold Text",
            cursorDecrease = 2,
            type = ElementTypes.TEXT_FORMATTING,
            regex = Regex("\\b\\w+(?:\\s+\\w+)*\\b", RegexOption.IGNORE_CASE)
        ),
        MDElement(
            imageVector = Icons.Default.FormatItalic,
            content = "__",
            description = "Add Italic Text",
            cursorDecrease = 1,
            type = ElementTypes.TEXT_FORMATTING,
            regex = Regex("\\b\\w+(?:\\s+\\w+)*\\b", RegexOption.IGNORE_CASE)
        ),
        MDElement(
            imageVector = Icons.Default.Link,
            content = "[]()",
            description = "Add Link",
            cursorDecrease = 3,
            type = ElementTypes.LINK,
            regex = Regex("\\b\\w+(?:\\s+\\w+)*\\b", RegexOption.IGNORE_CASE)
        ),
        MDElement(
            imageVector = Icons.Default.Code,
            content = "``",
            description = "Add Code",
            cursorDecrease = 1,
            type = ElementTypes.CODE,
            regex = Regex("\\b\\w+(?:\\s+\\w+)*\\b", RegexOption.IGNORE_CASE)
        ),
        MDElement(
            imageVector = Icons.Default.FormatListBulleted,
            content = "- ",
            description = "Add Bulleted List",
            type = ElementTypes.LIST,
            regex = Regex("""^(?:1\. |- \[ \] |- \[x\] |- )\s*(.+)""", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
        ),
        MDElement(
            imageVector = Icons.Default.FormatListNumbered,
            content = "1. ",
            description = "Add Numbered List",
            type = ElementTypes.LIST,
            regex = Regex("""^(?:1\. |- \[ \] |- \[x\] |- )\s*(.+)""", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
        ),
        MDElement(
            imageVector = Icons.Default.Checklist,
            content = "- [ ] ",
            description = "Add Check List",
            type = ElementTypes.LIST,
            regex = Regex("""^(?:1\. |- \[ \] |- \[x\] |- )\s*(.+)""", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
        ),
        MDElement(
            imageVector = Icons.Filled.IntegrationInstructions,
            content = """---
                |tags:
                |- post
                |- code
                |- web
                |title: Hello World
                |---""".trimMargin(),
            description = "Add Yaml Tag",
            type = ElementTypes.YAML
        ),
        MDElement(
            imageVector = Icons.Default.Subtitles,
            content = """[[_TOSP_]]
                |
            """.trimMargin(),
            description = "Add Table of Subpages",
            type = ElementTypes.TEXT_FORMATTING
        ),
        MDElement(
            imageVector = Icons.Default.TableChart,
            content = "",
            description = "Add Table",
            type = ElementTypes.TABLE
        )
//        MDElement("Add List Point", content = "- Content")
    )
}
