package com.multiplatformcomposeapplication.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun PagesColumn(
    pages: List<MDContentElement>,
    currentPage: MDContentElement?,
    onSelect: (MDContentElement) -> Unit,
    onAddPage: (MDContentElement) -> Unit

) {
    LazyColumn {
        pages(
            pages,
            currentPage,
            onSelect,
            onAddPage
        )
        /*pages.forEach { page: MDContentElement ->
            stickyHeader {
                Surface(
                    modifier = Modifier.fillMaxWidth()
                        .clickable(true, onClick = {
                            onSelect(page)
                        }),
                    color = if (currentPage?.id == page.id) {
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
                            text = page.title
                        )
                        IconButton(
                            onClick = {
                                onAddPage(page)
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
            items(page.pages) { item: MDContentElement ->
                Surface(
                    modifier = Modifier.fillMaxWidth()
                        .clickable(true, onClick = {
                            onSelect(item)
                        }),
                    color = if (currentPage?.id == item.id) {
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
        }*/
    }
}

public fun LazyListScope.pages(
    pages: List<MDContentElement>,
    currentPage: MDContentElement?,
    onSelect: (MDContentElement) -> Unit,
    onAddPage: (MDContentElement) -> Unit
) {
    pages.forEach { page ->
        page(
            page,
            currentPage = currentPage,
            onSelect = onSelect,
            onAddPage = onAddPage
        )
    }
}

public fun LazyListScope.page(
    page: MDContentElement,
    currentPage: MDContentElement?,
    onSelect: (MDContentElement) -> Unit,
    onAddPage: (MDContentElement) -> Unit
) {
    item {

        Surface(
            modifier = Modifier.fillMaxWidth()
                .clickable(true, onClick = {
                    onSelect(page)
                }),
            color = if (currentPage?.id == page.id) {
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
                    text = if (page.type == MDContentElementType.SubPage)
                        "   " + page.title
                    else page.title
                )
                if (page.type != MDContentElementType.SubPage) {
                    IconButton(
                        onClick = {
                            onAddPage(
                                page
                            )
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

    pages(
        page.pages,
        currentPage,
        onSelect,
        onAddPage
    )


}