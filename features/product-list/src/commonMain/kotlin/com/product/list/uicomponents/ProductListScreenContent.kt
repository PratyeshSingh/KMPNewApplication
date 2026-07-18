package com.product.list.uicomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.api.product.list.data.ProductList
import com.product.list.viewmodel.ProductAction
import com.product.list.viewmodel.ProductActionHandler

@Composable
internal fun ProductListScreenContent(
    products: List<ProductList>,
    productAction: ProductActionHandler,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(180.dp),
        modifier = modifier.fillMaxSize(),
    ) {
        items(products, key = { it.id }) { item ->
            ProductFrame(
                item = item,
                productAction = productAction,
            )
        }
    }
}

@Composable
private fun ProductFrame(
    item: ProductList,
    productAction: ProductActionHandler,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .padding(8.dp)
            .clickable {
                productAction(ProductAction.ViewDetail(item.id.toString()))
            },
    ) {
        AsyncImage(
            model = item.thumbnail,
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Color.LightGray),
        )

        Spacer(Modifier.height(2.dp))

        Text(item.title, style = MaterialTheme.typography.titleMedium)
        Text(item.id.toString(), style = MaterialTheme.typography.bodyMedium)
        Text(item.description.orEmpty(), style = MaterialTheme.typography.bodySmall)
    }
}
