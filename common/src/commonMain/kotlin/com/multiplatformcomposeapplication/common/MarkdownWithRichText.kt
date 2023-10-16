package com.multiplatformcomposeapplication.common

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.WindowInfo
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.multiplatformcomposeapplication.common.ui.theme.md_theme_light_secondary

@Composable
public fun MarkdownWithRichText(
    modifier: Modifier = Modifier
) {
    var sectionCount by remember {
        mutableStateOf(1)
    }
    var pageCount by remember {
        mutableStateOf(0)
    }

    val richTextState = rememberRichTextState()
    var sections = remember {
        mutableStateListOf(
            MDContentElement(
                    id = 0,
                    title = "Section 1",
                    type = MDContentElementType.Section
                )
            )

    }

    var currentSection: MDContentElement? by remember {
        mutableStateOf(sections[0])
    }
    var currentPage: MDContentElement? by remember {
        mutableStateOf(null)
    }
    val pages = remember {
        mutableStateListOf<MDContentElement>()
    }
    var markdown by remember(currentPage) {
        mutableStateOf(TextFieldValue(currentPage?.content ?: ""))
    }
    var isWideScreen by remember {
        mutableStateOf(false)
    }
    var openDrawer by remember {
        mutableStateOf(false)
    }
    val focusRequester = remember { FocusRequester() }
    val mdElements = rememberMDElements()
    LaunchedEffect(markdown.text) {
        richTextState.setMarkdown(markdown.text)
//        println(markdown.text)
    }
    Box(
        modifier = modifier.padding(20.dp)
            .onSizeChanged {
                isWideScreen = it.width > 2000
            }
    ) {
        Row(modifier = Modifier.fillMaxHeight()) {
            Column(
                modifier = Modifier.fillMaxHeight()
                    .border(
                        width = 0.5.dp,
                        color = MaterialTheme.colorScheme.outline
                    ),
            ) {
                FilledTonalIconToggleButton(
                    checked = openDrawer,
                    onCheckedChange = {
                        openDrawer = !openDrawer
                    },
                    shape = RectangleShape
                ) {
                    Icon(
                        imageVector = Icons.Filled.SpaceDashboard,
                        contentDescription = "Open drawer"
                    )
                }
            }
            if (openDrawer) {
                BoxWithConstraints(
                    modifier = Modifier.fillMaxHeight().width(200.dp)
                        .border(width = 0.5.dp, color = MaterialTheme.colorScheme.outline)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        items(sections.toList()) { item: MDContentElement ->
                            Surface(
                                modifier = Modifier.fillMaxWidth()
                                    .clickable(true, onClick = {
                                        currentSection = item
                                        pages.clear()
                                        pages.addAll(item.pages)
                                        currentPage = pages.getOrNull(0)
                                    }),
                                color = if (currentSection?.id == item.id) {
                                    MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                                } else {
                                    MaterialTheme.colorScheme.background
                                },
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.title
                                    )
                                    IconButton(
                                        onClick = {
                                            val updatedSections = sections
                                            if (currentSection?.id != item.id) {
                                                currentSection = item
                                                pages.clear()
                                                pages.addAll(item.pages)
                                            }
                                            pages.add(
                                                MDContentElement(
                                                    id = pageCount,
                                                    title = "Page ${pageCount + 1}",
                                                    parent = item.id,
                                                    type = MDContentElementType.Page
                                                )
                                            )

                                            updatedSections.find { it.id == item.id }?.pages?.add(
                                                MDContentElement(
                                                    id = pageCount,
                                                    title = "Page ${pageCount + 1}",
                                                    parent = item.id,
                                                    type = MDContentElementType.Page
                                                ))

                                            sections = updatedSections
                                            pageCount++
                                        },
                                        modifier = Modifier.size(18.dp),
                                        colors = IconButtonDefaults.iconButtonColors(
                                            contentColor = MaterialTheme.colorScheme.secondaryContainer
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Add page to section"
                                        )
                                    }
                                }
                            }
                        }
                    }
                    TextButton(
                        onClick = {
                            sections += MDContentElement(
                                id = sectionCount,
                                title = "Section ${sectionCount + 1}",
                                type = MDContentElementType.Section
                            )
                            sectionCount++
                        },
                        shape = RectangleShape,
                        modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
                            .border(width = 0.5.dp, color = MaterialTheme.colorScheme.outline)
                    ) {
                        Text("Add section")
                    }
                }
                BoxWithConstraints(
                    modifier = Modifier.fillMaxHeight().width(200.dp)
                        .border(width = 0.5.dp, color = MaterialTheme.colorScheme.outline)
                ) {
                    PagesColumn(
                        pages = pages,
                        currentPage = currentPage,
                        onSelect = {
                            currentPage = it
                        },
                        onAddPage = { element ->
                            val updatedPages = pages.toMutableList()
                            updatedPages.find { it.id ==  element.id }?.pages?.add(
                                MDContentElement(
                                    id = pageCount,
                                    title = "Page ${pageCount + 1}",
                                    parent = element.id,
                                    type = MDContentElementType.SubPage
                                )
                            )
                            pages.clear()
                            pages.addAll(updatedPages)
                            sections.find { it.id == element.parent && it.type == MDContentElementType.Section }?.pages = updatedPages
                            pageCount++
                        }
                    )
                    /*TextButton(
                        onClick = {
                            val updatedSections = sections
                            pages += Page(
                                id = pageCount,
                                parentId = currentPage?.parentId ?: 0
                            )
                            updatedSections[currentSection?.id ?: 0].pages = pages
                            sections = updatedSections
                            pageCount++
                        },
                        shape = RectangleShape,
                        modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
                            .border(width = 0.5.dp, color = MaterialTheme.colorScheme.outline)
                    ) {
                        Text("Add page")
                    }*/
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                AddMDElementRow(modifier = Modifier.fillMaxWidth()
                    .border(width = 0.5.dp, color = MaterialTheme.colorScheme.outline), onClick = { mdElement ->
                    focusRequester.requestFocus()
                    val newText = if (mdElement.isAtStart && markdown.text.isNotBlank()) {
                        if (markdown.text.contains("\n"))
                            markdown.text.replaceAfterLast("\n", mdElement.content.toString())
                        else {
                            val previousContent =
                                (mdElements.filter { it.type == mdElement.type }.takeIf { it.isNotEmpty() }
                                    ?: mdElements.toMutableList().flatMap { it.subOptions ?: listOf() }
                                        .filter { it.type == mdElement.type })/*.find { markdown.text.contains("${it.content.toString().trim()}\\s".toRegex()) }*/
//                        println(previousContent.findLast { markdown.text.contains("${it.content.toString().trim()}\\s".toRegex()) })
                            markdown.text.replace(
                                previousContent.findLast {
                                    markdown.text.contains(
                                        "${
                                            it.content.toString().trim()
                                        }\\s".toRegex()
                                    )
                                }?.content ?: "",
                                mdElement.content.toString()
                            )
                        }
                    } else markdown.text.plus(mdElement.content)


                    markdown = markdown.copy(
                        newText,
                        selection = TextRange(newText.length.minus(mdElement.cursorDecrease ?: 0))
                    )


                })
                Spacer(modifier = Modifier.height(8.dp))
                if (isWideScreen) {
                    Row(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Text(
                                text = "Rich Text Editor:",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth().weight(1f).focusRequester(focusRequester),
                                value = markdown,
                                onValueChange = { textFieldValue ->
                                    markdown = textFieldValue
//                                    println(sections)
                                    (pages.find { it.id == currentPage?.id } ?: pages.flatMap { it.pages }.find { it.id == currentPage?.id })?.content = markdown.text
                                    sections.find { it.id == currentSection?.id }?.pages = pages.toMutableList()
                                }
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Text(
                                text = "Markdown Code:",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.extraSmall)
                                    .padding(vertical = 12.dp, horizontal = 12.dp)
                            ) {
                                item {
                                    RichText(
                                        state = richTextState,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Text(
                                text = "Rich Text Editor:",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth().weight(1f).focusRequester(focusRequester),
                                value = markdown,
                                onValueChange = { textFieldValue ->
                                    markdown = textFieldValue
//                                    println(currentPage)
                                    (pages.find { it.id == currentPage?.id } ?: pages.flatMap { it.pages }.find { it.id == currentPage?.id })?.content = markdown.text
                                    sections.find { it.id == currentSection?.id }?.pages = pages.toMutableList()
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Text(
                                text = "Markdown Code:",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.extraSmall)
                                    .padding(vertical = 12.dp, horizontal = 12.dp)
                            ) {
                                item {
                                    RichText(
                                        state = richTextState,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }


    }
}

@Composable
public fun AdaptiveLayout(
    isWideScreen: Boolean,
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit
) {
    if (isWideScreen) {
        LazyRow(
            modifier = modifier,
            content = content
        )
    } else LazyColumn(
        modifier = modifier,
        content = content
    )
}

public data class MDContentElement(
    val id: Int,
    val title: String = "Untitled",
    var content: String? = null,
    val parent: Int? = null,
    var pages: MutableList<MDContentElement> = mutableListOf(),
    val type: MDContentElementType
)

public sealed class MDContentElementType() {
    public data object Section : MDContentElementType()
    public data object Page : MDContentElementType()
    public data object SubPage : MDContentElementType()
}

//public data class Section(
//    val id: Int,
//    val title: String = "Untitled",
//    var pages: List<Page> = listOf()
//)
//
//public data class Page(
//    val id: Int,
//    val title: String = "Untitled",
//    var content: String? = null,
//    val parentId: Int,
//    val subpages: List<Page> = listOf(),
//)

