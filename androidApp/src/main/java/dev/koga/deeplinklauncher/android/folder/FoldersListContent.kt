@file:OptIn(ExperimentalMaterial3Api::class)

package dev.koga.deeplinklauncher.android.folder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.model.Folder


@Composable
fun FoldersListContent(
    modifier: Modifier = Modifier,
    folders: List<Folder>,
    onClick: (Folder) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier,
        contentPadding = PaddingValues(24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalItemSpacing = 24.dp,
        columns = StaggeredGridCells.Fixed(2)
    ) {
        items(folders.size) { index ->
            FolderCard(
                folder = folders[index],
                onClick = onClick,
            )
        }
    }
}

@Composable
fun FolderCard(folder: Folder, onClick: (Folder) -> Unit) {
    ElevatedCard(
        onClick = { onClick(folder) },
        modifier = Modifier.size(184.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = folder.name, style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}