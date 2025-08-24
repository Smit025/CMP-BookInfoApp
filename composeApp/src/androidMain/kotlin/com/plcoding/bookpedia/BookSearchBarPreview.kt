package com.plcoding.bookpedia

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.plcoding.bookpedia.book.data.network.KtorRemoteBookDataSource
import com.plcoding.bookpedia.book.data.repository.DefaultBookRepository
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.presentation.book_list.BookListScreenRoot
import com.plcoding.bookpedia.book.presentation.book_list.BookListViewModel
import com.plcoding.bookpedia.core.data.HttpClientFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttpEngine

@Preview
@Composable
fun BookSearchBarPreview() {
    val bookList = mutableListOf<Book>()
    for (i in 0..50) {
        bookList.add(
            Book(
                id = i.toString(),
                title = "Book $i",
                imageUrl = "https://example.com/book1.jpg",
                authors = listOf("Author 1"),
                description = "Description of Book 1",
                languages = listOf("English"),
                firstPublishYear = "2022",
                averageRating = 4.5,
                ratingCount = 100,
                numPages = 300,
                numEditions = 1
            )
        )
    }
    //  BookListScreen(bookListState = BookListState(searchResults = bookList, selectedTabIndex = 0), onAction = {}) { }
    // BookListState(searchResults = bookList, selectedTabIndex = 0)

}