package com.carousell.testmyapplication.uicomponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carousell.testmyapplication.viewmodel.ProductAction
import com.carousell.testmyapplication.viewmodel.ProductActionHandler
import org.jetbrains.compose.resources.stringResource
import testmyapplication.shared.generated.resources.Res
import testmyapplication.shared.generated.resources.feed_loading_failed
import testmyapplication.shared.generated.resources.retry

@Composable
internal fun FeedErrorScreen(
    productAction: ProductActionHandler,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.feed_loading_failed),
            modifier = Modifier.padding(20.dp),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.error
        )

        Button(
            onClick = {
                productAction(ProductAction.Retry)
            },
        ) {
            Text(
                text = stringResource(Res.string.retry),
                modifier = Modifier.padding(10.dp),
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorWarningTextViewPreview() {
    FeedErrorScreen(productAction = {})
}