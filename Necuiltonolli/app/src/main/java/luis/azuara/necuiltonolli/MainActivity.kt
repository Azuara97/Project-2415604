package luis.azuara.necuiltonolli

import android.R
import android.os.Bundle
import androidx.activity.*
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.*
import androidx.navigation.compose.*
import coil.compose.SubcomposeAsyncImage
import luis.azuara.necuiltonolli.data.*
import luis.azuara.necuiltonolli.ui.theme.AppTheme

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : Screen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )

    object Library : Screen(
        route = "library",
        title = "Library",
        icon = Icons.Default.LibraryBooks
    )

    object List : Screen(
        route = "list",
        title = "My List",
        icon = Icons.Default.FormatListNumbered
    )

    //Product Screen
    object Product : Screen(
        route = "product/",
        title = "",
        icon = Icons.Default.Movie
    )

    companion object {
        val items = listOf(Home, Library, List)
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme(darkTheme = true, dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

//Home Elements
@Composable
fun LoadImage(
    url: String,
    description: String,
    modifier: Modifier
){
    SubcomposeAsyncImage(
        model = url,
        contentDescription = description,
        modifier = modifier,
        contentScale = ContentScale.Fit,
        loading = {
            Box(
                modifier = Modifier.align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        },
        error = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Loading error")
            }
        }
    )
}

@Composable
fun HomeMediaImage(
    imageURL : String,
    description : String
){
    Box(
        modifier = Modifier.fillMaxWidth()
                           .background(color = MaterialTheme.colorScheme.primaryContainer)
                           .padding(30.dp)
    ){
        LoadImage(
            imageURL,
            description,
            modifier = Modifier.align(Alignment.Center)
                               .size(250.dp)
        )
    }
}

@Composable
fun HomeProductLayout(
    status : String,
    imageURL: String,
    description: String
){
    Spacer(modifier = Modifier.size(20.dp))
    Column(
        modifier = Modifier
    ) {
        Text(
            status,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.size(10.dp))
        HomeMediaImage(
            imageURL,
            description
        )
    }
}

//Library Elements
@Composable
fun LabelRowData(label : String, data : String){
    Row{
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 20.sp
        )
        Text(
            text = data,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropMenu(
    modifier: Modifier = Modifier,
    selectedType: String,
    onTypeSelected: (String) -> Unit
){
    var expanded by remember { mutableStateOf(false) }
    val types = listOf("Manga", "Anime", "Movie", "Book", "Show", "VideoGame")

    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedType,
                onValueChange = {},
                readOnly = true,
                label = { Text("Choose type:") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults
                        .TrailingIcon(expanded = expanded)
                },
                modifier = modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                types.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            onTypeSelected(type)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchTools(
    nameSearch: String,
    onNameChange: (String) -> Unit,
    type: String,
    onTypeChange: (String) -> Unit
){
    Row(modifier = Modifier.padding(top = 10.dp)) {
        TextField(
            value = nameSearch,
            label = { Text("Introduce Name...") },
            onValueChange = onNameChange,
            modifier = Modifier.width(200.dp)
        )
        Spacer(modifier = Modifier.size(5.dp))
        DropMenu(
            selectedType = type,
            onTypeSelected = onTypeChange,
            modifier = Modifier.width(200.dp)
        )
    }
}

@Composable
fun MediaProductLibraryBasicData(
    product: Product,
    expandable : Boolean = false
){
    var expanded by remember { mutableStateOf(expandable) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                expanded = !expanded
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LoadImage(
                    product.imgURL,
                    product.Name,
                    modifier = Modifier.size(100.dp)
                )
                Column {
                    LabelRowData("Name:", product.Name)
                    LabelRowData("Type:", product.Type)
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.size(15.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {

                        }
                    ) {
                        Text(
                            "Add To My List"
                        )
                    }
                }
            }
        }
    }
}

//MyList Elements
@Composable
fun MediaProductListBasicData(
    product: Product,
    expandable: Boolean = false
){
    var expanded by remember { mutableStateOf(expandable) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                expanded = !expanded
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LoadImage(
                    product.imgURL,
                    product.Name,
                    modifier = Modifier.size(100.dp)
                )
                Column {
                    LabelRowData("Name:", product.Name)
                    LabelRowData("Type:", product.Type)
                    LabelRowData("Status:", product.Status)
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.size(15.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {

                        }
                    ) {
                        Text(
                            "Remove"
                        )
                    }
                }
            }
        }
    }
}

//Screens Elements
@Composable
fun TitleRow(
    icon: ImageVector,
    title: String
){
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(36.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            title,
            modifier = Modifier.padding(top = 4.dp),
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

//Screens
@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
                           .padding(16.dp),
    ) {
        TitleRow(
            Icons.Default.Home,
            "Home"
        )
        HomeProductLayout(
            "New!",
            hungerGamesBook5SotR,
            "Sunrise on the Reaping : Book"
        )
        HomeProductLayout(
            "Coming...",
            demonSlayerMovie2IC,
            "Demon Slayer Infinity Castle : Movie"
        )
    }
}

@Composable
fun LibraryScreen() {
    var nameSearch by rememberSaveable { mutableStateOf("") }
    var typeFilter by rememberSaveable { mutableStateOf("") }
    var productList = listOf(
        Product(1, "Demon Slayer Infinity Castle : Movie", "Movie", "Chidorria", "Finished", demonSlayerMovie2IC),
        Product(2, "Sunrise on the Reaping", "Book", "Mamalon", "Not Started", hungerGamesBook5SotR)
    )

    Column(
        modifier = Modifier.fillMaxSize()
                           .padding(16.dp)
    ) {
        TitleRow(
            Icons.Default.LibraryBooks,
            "Library"
        )
        SearchTools(
            nameSearch = nameSearch,
            onNameChange = { nameSearch = it },
            type = typeFilter,
            onTypeChange = { type -> typeFilter = type }
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(1)
        ) {
            items(filterProducts(productList, nameSearch, typeFilter)) {
                product -> MediaProductLibraryBasicData(product = product)
            }
        }
    }
}

@Composable
fun ListScreen() {
    var nameSearch by rememberSaveable { mutableStateOf("") }
    var typeFilter by rememberSaveable { mutableStateOf("") }
    var productList = listOf(
        Product(1, "Demon Slayer Infinity Castle : Movie", "Movie", "Chidorria", "Finished", demonSlayerMovie2IC)
    )

    Column(
        modifier = Modifier.fillMaxSize()
                           .padding(16.dp)
    ) {
        TitleRow(
            Icons.Default.FormatListNumbered,
            "My List"
        )
        SearchTools(
            nameSearch = nameSearch,
            onNameChange = { nameSearch = it },
            type = typeFilter,
            onTypeChange = { type -> typeFilter = type }
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(1)
        ) {
            items(filterProducts(productList, nameSearch, typeFilter)) {
                    product -> MediaProductListBasicData(product = product)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Necuiltonolli",
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar {
                Screen.items.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(screen.icon, contentDescription = screen.title)
                        },
                        label = {
                            Text(
                                screen.title,
                                style = MaterialTheme.typography.labelMedium
                            ) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Évite l'empilement des destinations
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                // Évite les copies multiples de la même destination
                                launchSingleTop = true
                                // Restaure l'état lors de la reselection
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Home.route) {
                HomeScreen()
            }
            composable(Screen.Library.route) {
                LibraryScreen()
            }
            composable(Screen.List.route) {
                ListScreen()
            }
        }
    }
}