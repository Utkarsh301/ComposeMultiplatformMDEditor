package com.multiplatformcomposeapplication.common

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
public fun MyMarkdownEditor(
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
        mutableStateOf(true)
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
                                    folders.searchById(element.id)?.children?.add(
                                        MDContentElement(
                                            id = elementCount,
                                            title = "${
                                                if (type is MDContentElementType.SubFolder) {
                                                    "SubFolder"
                                                } else "File"
                                            } ${elementCount + 1}",
                                            type = type
                                        )
                                    )
                                    elementCount++
                                },
                                onSelectFile = {
                                    currentFile = it
                                },
                                onRename = { element, title ->
                                    folders.searchById(element.id)?.title = title
                                },
                                onDelete = { delete ->
                                    val updatedFolders = folders.deleteById(delete.id)
                                    folders.clear()
                                    folders.addAll(updatedFolders)
                                    elementCount--
                                    if (delete.id == currentFile?.id) currentFile = null
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
                if (currentFile != null) {
                    AddMDElementRow(
                        modifier = Modifier.fillMaxWidth()
                            .border(width = 0.5.dp, color = MaterialTheme.colorScheme.outline)
                    ) { mdElement ->
                        focusRequester.requestFocus()
                        /*val newText = if (mdElement.isAtStart && markdown.text.isNotBlank()) {
                            if (markdown.text.contains("\n"))
                                markdown.text.replaceAfterLast("\n", mdElement.content.toString())
                            else {
    //                            val previousContent =
    //                                (mdElements.filter { it.type == mdElement.type }.takeIf { it.isNotEmpty() }
    //                                    ?: mdElements.toMutableList().flatMap { it.subOptions ?: listOf() }
    //                                        .filter { it.type == mdElement.type }).asReversed().find { markdown.text.contains("${it.content.toString().trim()}\\s".toRegex()) }
                                *//*println(previousContent.findLast { markdown.text.contains("${it.content.toString().trim()}\\s".toRegex()) })
                            markdown.text.replace(
                                previousContent.findLast {
                                    markdown.text.contains(
                                        "${
                                            it.content.toString().trim()
                                        }\\s".toRegex()
                                    )
                                }?.content ?: "",
                                mdElement.content.toString()
                            )*//*
//                            println(previousContent)
//                            val text = markdown.text.replace(previousContent?.content.toString(), "")

                            mdElement.content?.plus(mdElement.regex?.find(markdown.text)?.groupValues?.last() ?: markdown.text)
                        }
                    } else {
                        markdown.text.plus(mdElement.content)
                    }*/
                        val lines = """^.*$""".toRegex(RegexOption.MULTILINE).findAll(
                            markdown.text
                        )
                        val currentLine = lines.findLast {
                            println(it.range)
                            IntRange(it.range.first, it.range.last + 1).contains(markdown.selection.start)
                        }.also {
                            println(it?.value)
                        }
                        val lastText = mdElement.regex?.find(
                            currentLine?.value ?: markdown.text
                        ).also {
                            println(it?.groupValues)
                        }
                        val formattedText = if (markdown.text.isNotBlank()) {
                            when (mdElement.type) {
                                ElementTypes.HEADING -> {
                                    mdElement.content?.plus(
                                        lastText?.groupValues?.last() ?: currentLine?.value ?: markdown.text
                                    )
                                }

                                ElementTypes.LIST -> {
                                    buildString {
                                        append(mdElement.content)
                                        append(lastText?.groupValues?.last() ?: currentLine?.value ?: markdown.text)
                                    }
                                }

                                ElementTypes.TEXT_FORMATTING -> {
                                    buildString {
                                        append(
                                            (currentLine?.value ?: markdown.text).substring(
                                                0,
                                                lastText?.range?.first ?: 0
                                            )
                                        )
                                        append(
                                            "${
                                                mdElement.content?.substring(
                                                    0,
                                                    mdElement.content.length.minus(mdElement.cursorDecrease ?: 0)
                                                )
                                            }${lastText?.groupValues?.joinToString(" ") { it }}${
                                                mdElement.content?.substring(
                                                    mdElement.cursorDecrease ?: 0
                                                )
                                            }"
                                        )
                                        append(
                                            (currentLine?.value ?: markdown.text).substring(
                                                lastText?.range?.last?.plus(1) ?: 0,
                                                (currentLine?.value ?: markdown.text).length
                                            )
                                        )
                                    }
                                }

                                ElementTypes.CODE -> {
                                    buildString {
                                        append("`${currentLine?.value ?: markdown.text}`")
                                    }
                                }

                                ElementTypes.LINK -> {
                                    buildString {
                                        append("[${currentLine?.value ?: markdown.text}]()")
                                    }
                                }

                                ElementTypes.SUB_OPTIONS -> TODO()
                                ElementTypes.YAML -> {
                                    buildString {
                                        append(mdElement.content)
                                        append("\n")
                                        append(markdown.text)
                                    }
                                }

                                ElementTypes.TABLE -> {
                                    buildString {
                                        append(mdElement.content)
                                        append("\n")
                                        append(markdown.text)
                                    }
                                }
                            }
                        } else {
                            markdown.text.plus(mdElement.content)
                        }

                        val newText = buildString {
                            if ((currentLine?.range?.first ?: 0) > 0) {
                                append(markdown.text.substring(0, currentLine?.range?.first?.minus(1) ?: 0))
                                append("\n")
                            }
                            append(formattedText)
                            if ((currentLine?.range?.last ?: 0) < markdown.text.length - 1) {
                                append("\n")
                                append(markdown.text.substring(currentLine?.range?.last?.plus(2) ?: 0))
                            }
                        }

                        if (formattedText != null) {
                            markdown = markdown.copy(
                                newText,
                                selection = TextRange(
                                    Regex(
                                        "\\b\\w+(?:\\s+\\w+)*\\b",
                                        RegexOption.IGNORE_CASE
                                    ).find(newText, currentLine?.range?.first ?: 0)?.range?.last ?: mdElement.cursorDecrease ?: 0
                                )
                            )

                        }


                    }
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
                                    text = "Editor:",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth().weight(1f).focusRequester(focusRequester),
                                    value = markdown,
                                    onValueChange = { textFieldValue ->
                                        markdown = textFieldValue
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
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outline,
                                            MaterialTheme.shapes.extraSmall
                                        )
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
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outline,
                                            MaterialTheme.shapes.extraSmall
                                        )
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
    onSelectFile: (MDContentElement) -> Unit,
    onRename: (MDContentElement, String) -> Unit,
    onDelete: (MDContentElement) -> Unit
) {
    var openElementDropdown by remember {
        mutableStateOf(false)
    }
    var openRenameDialog by remember {
        mutableStateOf(false)
    }
    var childTitle by remember {
        mutableStateOf(TextFieldValue(child.title, selection = TextRange(0, child.title.length)))
    }
    val focusRequester = remember { FocusRequester() }
    val scope = rememberCoroutineScope()
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
            Box {
                IconButton(
                    onClick = {
                        openElementDropdown = true
                    },
                    modifier = Modifier.size(18.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Open Options"
                    )
                }
                DropdownMenu(expanded = openElementDropdown, onDismissRequest = {
                    openElementDropdown = false
                }) {
                    DropdownMenuItem(
                        text = {
                            Text("Rename")
                        },
                        onClick = {
                            openElementDropdown = false
                            openRenameDialog = true
                            scope.launch {
                                delay(250)
                                focusRequester.requestFocus()
                            }
                        }
                    )
                    if (child.type != MDContentElementType.File) {
                        DropdownMenuItem(
                            text = {
                                Text("Add Sub Folder")
                            },
                            onClick = {
                                openElementDropdown = false
                                onAddChild(child, MDContentElementType.SubFolder)
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text("Add File")
                            },
                            onClick = {
                                openElementDropdown = false
                                onAddChild(child, MDContentElementType.File)
                            }
                        )
                    }
                    DropdownMenuItem(
                        text = {
                            Text("Delete")
                        },
                        onClick = {
                            openElementDropdown = false
                            onDelete(child)
                        }
                    )

                }
                if (openRenameDialog) {
                    Dialog(onDismissRequest = {
                        openRenameDialog = false
                    }) {
                        Card {
                            Column(
                                modifier = Modifier.padding(16.dp, 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                OutlinedTextField(
                                    modifier = Modifier.focusRequester(focusRequester),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    label = {
                                        Text("Title")
                                    },
                                    value = childTitle,
                                    onValueChange = {
                                        childTitle = it
                                    })
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(onClick = {
                                    openRenameDialog = false
                                    onRename(child, childTitle.text)
                                }) {
                                    Text("Submit")
                                }
                            }
                        }
                    }
                }
            }
        }
        child.children.forEach { subChild ->
            FolderItem(
                subChild, currentFile, onAddChild, onSelectFile, onRename, onDelete
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

public fun MutableList<MDContentElement>.deleteById(id: Int): MutableList<MDContentElement> {

    val updated = mutableListOf<MDContentElement>()

    for (element in this) {
        if (element.id != id) updated.add(element.copy(children = element.children.deleteById(id)))
    }
    return updated

}