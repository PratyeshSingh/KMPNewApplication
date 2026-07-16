package com.product.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.api.product.list.data.ProductList
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun ProductDetailsScreen(
    item: String,
    topBar: @Composable () -> Unit = {},
    onClick: () -> Unit
) {
    val viewModel: ProductDetailsViewModel = koinViewModel()
    LaunchedEffect(item) {
        viewModel.getProductDetails(item)
    }
    val product = viewModel.state.collectAsStateWithLifecycle().value

    AnimatedContent(product != null) {
        Scaffold(
            topBar = topBar,
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            product?.let {
                ProductDetailsBody(
                    modifier = Modifier.padding(innerPadding),
                    onClick = onClick,
                    item = product
                )
            }
        }
    }
}


@Composable
private fun ProductDetailsBody(
    item: ProductList,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = item.thumbnail,
            contentDescription = item.title,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
        )

        SelectionContainer {
            Column(Modifier.padding(12.dp)) {
                Text(item.title, style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(6.dp))
                LabeledInfo(stringResource(Res.string.label_brand), item.brand.orEmpty())
                LabeledInfo(
                    stringResource(Res.string.label_warranty),
                    item.warrantyInformation.orEmpty()
                )
                LabeledInfo(stringResource(Res.string.label_category), item.category.orEmpty())
                LabeledInfo(stringResource(Res.string.label_description), item.description.orEmpty())
            }
        }
    }
}

@Composable
private fun LabeledInfo(
    label: String,
    data: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier.padding(vertical = 4.dp)) {
        Spacer(Modifier.height(6.dp))
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("$label: ")
                }
                append(data)
            }
        )
    }
}
