/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog.products.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import ru.surfstudio.android.demo.components.compose.overload.texts.Text_Headline_Medium_16
import ru.surfstudio.android.demo.components.compose.placeholder.PlaceholderContainer
import ru.surfstudio.mvi.vm.compose.renders

@Destination
@Composable
fun ProductDetailsScreen(
    // passed here for destination code generation
    @Suppress("UNUSED_PARAMETER") productId: String,
    viewModel: ProductDetailsViewModel = hiltViewModel()
) {
    viewModel renders { state ->
        ProductDetailsBody(state = state, onRetry = { emit(ProductDetailsEvent.Retry) })
    }
}

@Composable
private fun ProductDetailsBody(state: ProductDetailsState, onRetry: () -> Unit) {
    PlaceholderContainer(loadStateType = state.loadState, retry = { onRetry() }) {
        state.product?.let { product ->
            Box(modifier = Modifier.fillMaxSize()) {
                Text_Headline_Medium_16(
                    text = product.name,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}