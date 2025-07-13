package com.turkoglu.moviecomposeapp.presentation.search.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.turkoglu.moviecomposeapp.R
import com.turkoglu.moviecomposeapp.domain.model.Search
import com.turkoglu.moviecomposeapp.presentation.component.CircularBackButtons
import com.turkoglu.moviecomposeapp.presentation.search.SearchViewModel
import com.turkoglu.moviecomposeapp.presentation.ui.primaryDarkVariant
import com.turkoglu.moviecomposeapp.presentation.ui.primaryGray
import com.turkoglu.moviecomposeapp.presentation.ui.primaryPink
import com.turkoglu.moviecomposeapp.util.Constants
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchResults = viewModel.searchResults.value.collectAsLazyPagingItems()
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(Modifier.fillMaxSize()) {
        Row (Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            Alignment.CenterVertically){
            CircularBackButtons {
                navController.popBackStack()
            }
            SearchBar(
                searchTerm = viewModel.searchTerm.value,
                onSearchChanged = viewModel::setSearchTerm,
                onSearch = {
                    viewModel.searchAll(it)
                    keyboardController?.hide()
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(Modifier.fillMaxSize()) {
            LazyColumn(
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.Top
            ) {
                itemsIndexed(
                    items = searchResults.itemSnapshotList.items,
                    key = { _, item -> item.id ?: -1 }
                ) { _, search ->
                    SearchItem(
                        search = search,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .padding(4.dp),
                        onClick = { navController.navigate("Detail/${search.id}") }
                    )
                }

                if (searchResults.loadState.append == LoadState.Loading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                }
            }

            when (val state = searchResults.loadState.refresh) {
                is LoadState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = primaryPink,
                        strokeWidth = 2.dp
                    )
                }

                is LoadState.Error -> {
                    val message = when (state.error) {
                        is HttpException -> "Oops, something went wrong!"
                        is IOException -> "Check your internet connection!"
                        else -> "Unknown error occurred"
                    }
                    Text(
                        text = message,
                        modifier = Modifier.align(Alignment.Center).padding(16.dp),
                        color = primaryPink,
                        textAlign = TextAlign.Center
                    )
                }

                is LoadState.NotLoading -> {
                    if (searchResults.itemCount == 0) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_empty_cuate),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(250.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    searchTerm: String,
    onSearchChanged: (String) -> Unit,
    onSearch: (String) -> Unit,
    hint: String = "Search..."
) {
    TextField(
        value = searchTerm,
        onValueChange = onSearchChanged,
        placeholder = { Text(text = hint, color = primaryGray) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .shadow(2.dp, shape = RoundedCornerShape(25.dp)),
        shape = RoundedCornerShape(25.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Words,
            autoCorrect = true,
            keyboardType = KeyboardType.Text
        ),
        textStyle = TextStyle(color = Color.White),
        maxLines = 1,
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = { onSearch(searchTerm) }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = primaryGray
                )
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            backgroundColor = primaryDarkVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun SearchItem(
    search: Search,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp
    ) {
        Row {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = "${Constants.IMAGE_BASE_URL}/${search.posterPath}").apply(block = fun ImageRequest.Builder.() {
                            placeholder(R.drawable.ic_placeholder)
                            crossfade(true)
                        }).build()
                ),
                contentDescription = "Poster",
                modifier = Modifier.fillMaxWidth(0.3f),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = search.name ?: search.originalTitle ?: "No title",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(5.dp))
                search.firstAirDate?.let {
                    Text(
                        text = it,
                        fontSize = 10.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = search.overview ?: "No description",
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 11.sp
                )
            }
        }
    }
}
