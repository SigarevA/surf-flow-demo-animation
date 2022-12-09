/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import ru.surfstudio.android.demo.catalog.navigation.ProductsCatalogScreenNavigation
import ru.surfstudio.android.demo.components.compose.PaginationContainer
import ru.surfstudio.android.demo.components.compose.mapToLoadStateType
import ru.surfstudio.android.demo.components.compose.overload.texts.Text_Text_Regular_14
import ru.surfstudio.android.demo.components.compose.placeholder.PlaceholderSwipeRefreshContainer
import ru.surfstudio.android.demo.domain.entity.products.ShortProductEntity
import ru.surfstudio.mvi.vm.compose.renders

sealed class ProductsCatalogNavigationType {
    data class OpenProductDetails(val productId: String) : ProductsCatalogNavigationType()
}

@Destination
@Composable
fun ProductsCatalogScreen(
    // passed here for destination code generation
    @Suppress("UNUSED_PARAMETER") categoryId: String,
    navigation: ProductsCatalogScreenNavigation,
    viewModel: ProductsCatalogViewModel = hiltViewModel()
) {
    viewModel renders { state ->
        ProductsCatalogBody(
            state = state,
            openDetails = { navigation.navigate(ProductsCatalogNavigationType.OpenProductDetails(it)) },
            products = viewModel.products.collectAsLazyPagingItems()
        )
    }
}

@Composable
private fun ProductsCatalogBody(
    state: ProductsCatalogState,
    openDetails: (String) -> Unit,
    products: LazyPagingItems<ShortProductEntity>
) {
    val isEmpty = products.itemCount == 0
    val refreshState = rememberSwipeRefreshState(
        products.loadState.refresh is LoadState.Loading && !isEmpty
    )
    val loadStateType = products.mapToLoadStateType()

    PlaceholderSwipeRefreshContainer(
        loadStateType = loadStateType,
        refreshState = refreshState,
        retry = { products.retry() },
        refresh = { products.refresh() }
    ) {
        PaginationContainer(items = products) { product, index ->
            ShortProduct(product = product, index = index, openDetails = openDetails)
        }
    }
}

@Composable
private fun ShortProduct(
    index: Int,
    product: ShortProductEntity,
    openDetails: (String) -> Unit
) {
    Text_Text_Regular_14(
        modifier = Modifier
            .clickable { openDetails(product.id) }
            .padding(all = 16.dp),
        text = "$index ${product.name}"
    )
}