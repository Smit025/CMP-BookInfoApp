package com.plcoding.bookpedia.book.presentation.book_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.no_search_result
import cmp_bookpedia.composeapp.generated.resources.search_favorites
import cmp_bookpedia.composeapp.generated.resources.search_result
import com.plcoding.bookpedia.core.presentation.DarkBlue
import com.plcoding.bookpedia.core.presentation.DesertWhite
import com.plcoding.bookpedia.core.presentation.SandYellow
import com.plcoding.bookpedia.book.presentation.book_list.BookListAction.*
import com.plcoding.bookpedia.book.presentation.book_list.components.BookList
import com.plcoding.bookpedia.book.presentation.book_list.components.BookSearchBar
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun BookListScreenRoot(
    viewModel: BookListViewModel = koinViewModel(),
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    BookListScreen(state) { action ->
        Unit
        when (action) {
            is OnBookClick -> {
                //      viewModel.onAction(action.book)
                scope.launch {

                }
            }

            else -> {
                viewModel.onAction(action)
            }
        }
    }
}

@Composable
 fun BookListScreen(
    bookListState: BookListState,
    onAction: (BookListAction) -> Unit
) {
    val keyBoardController = LocalSoftwareKeyboardController.current

    val pagerState = rememberPagerState { 2 }
    val searchResultListState = rememberLazyListState()
    val favouriteBookListState = rememberLazyListState()

    LaunchedEffect(bookListState.searchResults) {
        searchResultListState.animateScrollToItem(0)
    }

    LaunchedEffect(bookListState.selectedTabIndex){
        pagerState.animateScrollToPage(bookListState.selectedTabIndex)
    }

    LaunchedEffect(pagerState.currentPage){
            onAction(OnTabSelected(pagerState.currentPage))
    }

    Column(
        modifier = Modifier.fillMaxSize().background(color = DarkBlue).statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BookSearchBar(
            searchQuery = bookListState.searchQuery,
            onSearchQueryChange = {
                onAction(OnSearchQueryChange(it))
            },
            onImeSearch = {
                keyBoardController?.hide()
            },
            modifier = Modifier.widthIn(max = 400.dp).fillMaxWidth().padding(16.dp)
        )

        Surface(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            color = DesertWhite,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TabRow(
                    selectedTabIndex = bookListState.selectedTabIndex,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .widthIn(max = 700.dp)
                        .fillMaxWidth(),
                    containerColor = DesertWhite,
                    contentColor = SandYellow,
                    indicator = { tabPosition ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPosition[bookListState.selectedTabIndex]),
                        )
                    }
                ) {
                    Tab(
                        selected = bookListState.selectedTabIndex == 0,
                        onClick = { onAction(OnTabSelected(0)) },
                        modifier = Modifier.weight(1f),
                        selectedContentColor = SandYellow,
                        unselectedContentColor = Color.Black.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = stringResource(Res.string.search_result),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    Tab(
                        selected = bookListState.selectedTabIndex == 1,
                        onClick = { onAction(OnTabSelected(1)) },
                        modifier = Modifier.weight(1f),
                        selectedContentColor = SandYellow,
                        unselectedContentColor = Color.Black.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = stringResource(Res.string.search_favorites),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                }
                Spacer(Modifier.height(4.dp))
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) { pageIndex ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        when (pageIndex) {
                            0 -> {
                                if (bookListState.isLoading) {
                                    CircularProgressIndicator()
                                } else {
                                    when {
                                        bookListState.errorMessage != null -> {
                                            Text(
                                                text = bookListState.errorMessage.asString(),
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.headlineSmall,
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }

                                        bookListState.searchResults.isEmpty() -> {
                                            Text(
                                                text = stringResource(Res.string.no_search_result),
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.headlineSmall,
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }

                                        else -> {
                                            BookList(
                                                bookList = bookListState.searchResults,
                                                onBookClicked = { onAction(OnBookClick(it)) },
                                                modifier = Modifier.fillMaxWidth(),
                                                scrollState = searchResultListState
                                            )
                                        }
                                    }
                                }
                            }

                            1 -> {
                                if (bookListState.favouriteBooks.isEmpty()) {
                                    Text(
                                        text = stringResource(Res.string.no_search_result),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                } else {
                                    BookList(
                                        bookList = bookListState.favouriteBooks,
                                        onBookClicked = { onAction(OnBookClick(it)) },
                                        modifier = Modifier.fillMaxWidth(),
                                        scrollState = favouriteBookListState
                                    )
                                }
                            }
                        }
                    }

                }
            }


        }

    }
}