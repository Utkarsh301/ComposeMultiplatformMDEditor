package com.multiplatformcomposeapplication.common

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.SpaceDashboard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText

@Composable
public fun MarkdownEditor(
    modifier: Modifier = Modifier
) {
    var elementCount by remember {
        mutableStateOf(1)
    }
    val richTextState = rememberRichTextState()
    val folders = remember {
        mutableStateListOf(
            MDContentElement(
                id = 0,
                title = "Folder 1",
                type = MDContentElementType.Folder
            )
        )
    }
    var currentFile: MDContentElement? by remember {
        mutableStateOf(null)
    }
    var markdown by remember(currentFile) {
        mutableStateOf(TextFieldValue(currentFile?.content ?: ""))
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
        Row(
            modifier = Modifier.fillMaxHeight()
        ) {
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
                Column(
                    modifier = Modifier.fillMaxHeight().width(300.dp)
                        .border(width = 0.5.dp, color = MaterialTheme.colorScheme.outline)
                ) {
                    Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                        folders.forEach { folder ->
                            FolderItem(
                                folder, currentFile,
                                onAddChild = { element, type ->
                                    val updatedFolders = folders.toMutableList()
                                    updatedFolders.searchById(element.id)?.children?.add(
                                        MDContentElement(
                                            id = elementCount,
                                            title = "${
                                                if(type is MDContentElementType.SubFolder) {
                                                    "SubFolder"
                                                } else "File"
                                            } ${elementCount + 1}",
                                            type = type
                                        )
                                    )
                                    folders.clear()
                                    folders.addAll(updatedFolders)
                                    elementCount++
                                },
                                onSelectFile = {
                                    currentFile = it
                                }
                            )
                        }
                    }
                    TextButton(
                        onClick = {
                            folders += MDContentElement(
                                id = elementCount,
                                title = "Folder ${elementCount + 1}",
                                type = MDContentElementType.Folder
                            )
                            elementCount++
                        },
                        shape = RectangleShape,
                        modifier = Modifier.fillMaxWidth()
                            .border(width = 0.5.dp, color = MaterialTheme.colorScheme.outline)
                    ) {
                        Text("Add folder")
                    }
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
                if (currentFile != null) {
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
                                    text = "Editor:",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth().weight(1f).focusRequester(focusRequester),
                                    value = markdown,
                                    onValueChange = { textFieldValue ->
                                        markdown = textFieldValue
    //                                    println(sections)
                                        folders.searchById(currentFile?.id ?: -1)?.content = markdown.text
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
                                    text = "Markdown Text:",
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
                                    text = "Editor:",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth().weight(1f).focusRequester(focusRequester),
                                    value = markdown,
                                    onValueChange = { textFieldValue ->
                                        markdown = textFieldValue
    //                                    println(currentPage)
                                        folders.searchById(currentFile?.id ?: -1)?.content = markdown.text
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
                                    text = "Markdown Text:",
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
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No File is selected")
                    }
                }
            }
        }
    }
}

@Composable
public fun FolderItem(
    child: MDContentElement,
    currentFile: MDContentElement?,
    onAddChild: (MDContentElement, MDContentElementType) -> Unit,
    onSelectFile: (MDContentElement) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth().padding(
            start = if (child.type != MDContentElementType.Folder)
                16.dp else 0.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().background(
                if (currentFile?.id == child.id) MaterialTheme.colorScheme.outline.copy(alpha = 0.4f) else Color.Transparent
            ).clickable {
                if (child.type == MDContentElementType.File) {
                    onSelectFile(child)
                }
            }.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = child.title
            )
            if (child.type != MDContentElementType.File) {
                Row {
                    IconButton(
                        onClick = {
                            onAddChild(child, MDContentElementType.SubFolder)
                        },
                        modifier = Modifier.size(18.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.CreateNewFolder,
                            contentDescription = "Add sub folder"
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            onAddChild(child, MDContentElementType.File)
                        },
                        modifier = Modifier.size(18.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.PostAdd,
                            contentDescription = "Add file"
                        )
                    }
                }

            }
        }
        child.children.forEach { subChild ->
            FolderItem(
                subChild, currentFile, onAddChild, onSelectFile
            )
        }
    }

}

public fun MutableList<MDContentElement>.searchById(id: Int): MDContentElement? {
    for (element in this) {
        if (element.id == id) {
            return element
        }
        element.children.takeIf { it.isNotEmpty() }?.let { elements ->
            elements.searchById(id)?.let { return it }
        }
    }
    return null
}