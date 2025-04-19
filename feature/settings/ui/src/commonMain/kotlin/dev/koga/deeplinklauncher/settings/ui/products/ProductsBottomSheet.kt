package dev.koga.deeplinklauncher.settings.ui.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.designsystem.DLLModalBottomSheet
import dev.koga.deeplinklauncher.purchase.api.Product
import kotlinx.collections.immutable.PersistentList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsBottomSheet(
    products: PersistentList<Product>,
    onDismissRequest: () -> Unit,
    onSelectProduct: (Product) -> Unit,
) {
    DLLModalBottomSheet(onDismiss = onDismissRequest) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Buy Me a Coffee",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Support my work with a cup of coffee",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                ),
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn {
                items(products) { product ->
                    ProductCard(
                        product = product,
                        modifier = Modifier.clickable { onSelectProduct(product) },
                    )
                }
            }
        }
    }
}

@Composable
internal fun ProductCard(modifier: Modifier = Modifier, product: Product) {
    ListItem(
        modifier = modifier,
        headlineContent = {
            Text(
                text = product.title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )
        },
        supportingContent = {
            Text(text = product.description)
        },

        trailingContent = {
            Text(
                text = product.formattedAmount,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )
        },
    )
}
