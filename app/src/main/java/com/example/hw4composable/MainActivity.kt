package com.example.hw4composable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.hw4composable.ui.theme.HW4ComposableTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import android.content.Context
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextField
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import org.json.JSONException
import java.time.Instant
import java.time.ZoneId
import kotlinx.coroutines.delay
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.rememberDismissState
import androidx.compose.material3.DismissState
import androidx.compose.material3.contentColorFor
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

//{"country":"US","currency":"USD","estimateCurrency":"USD","exchange":"NASDAQ NMS - GLOBAL MARKET","finnhubIndustry":"Technology","ipo":"1980-12-12","logo":"https://static2.finnhub.io/file/publicdatany/finnhubimage/stock_logo/AAPL.png","marketCapitalization":2579411.77875,"name":"Apple Inc","phone":"14089961010","shareOutstanding":15441.88,"ticker":"AAPL","weburl":"https://www.apple.com/"}
const val URL = "https://csci571hw3shingomorita8843v2.uw.r.appspot.com/api/"
const val numNews = 20
data class TickerInfo(
    val ticker: String = "",
    val name: String = "",
    val quantity: Int = 0,
    val avgshare: Double=0.00,
    val price: Double=0.00
)
data class FavoriteItem(
    var ticker: String = "",
    var name: String = "",
    var current: Double = 0.00,
    var change: Double = 0.00,
    var changeP: Double = 0.00,
    var open: Double = 0.00,
    var high: Double = 0.00,
    var low: Double = 0.00,
    var prev: Double = 0.00
)
data class PortfolioItem(
    var ticker: String = "",
    var share: Int = 0,
    var totalCost: Double = 0.00,
    var change: Double = 0.00,
    var changeP: Double = 0.00,
    var current: Double = 0.00,
)
data class WalletBalance(
    var netWorth: Double = 0.00,
    var cashBalance: Double = 25000.00
)
data class NewsArticle(
    val title: String = "",
    val source: String = "",
    val imageUrl: String = "",
    val publishedDate: LocalDateTime ,
    val description: String="",
    val url: String = ""
)
const val numberOfAllData = 6
data class TabIcon(val iconResId: Int, val contentDescription: String)
data class Stats(var open: Double = 0.00, var high: Double = 0.00, var low: Double = 0.00, var prev: Double = 0.00)
data class About(var IPO: String = "", var  industry: String = "", var website: String = "", var name: String = "", var peers: List<String> = listOf(""))
data class SocialSentiments(var totalMSPR: Double = 0.00, var posMSPR: Double = 0.00, var negMSPR : Double = 0.00, var totalChange: Double = 0.00, var posChange: Double = 0.00, var negChange: Double = 0.00, var name: String = "")
data class ResultPortfolio(
    var ticker: String = "",
    var sharesOwned: Int = 0,
    var avgCostPerShare: Double = 0.00,
    var totalCost: Double = 0.00,
    var change: Double = 0.00,
    var marketValue: Double = 0.00,
    var name: String = ""
)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HW4ComposableTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }
}
@Composable
fun PeriodicUpdateEffect(onTick: () -> Unit) {
    LaunchedEffect(Unit) {
        while (true) {
            delay(15000L)
            onTick()
        }
    }
}
@Composable
fun MyApp(){
    val context = LocalContext.current
    val queue = Volley.newRequestQueue(context)
    val coroutineScope = rememberCoroutineScope()
    var loading = remember {
        mutableStateOf(
          true
        )
    }
    var query = remember {
        mutableStateOf(
            "AAPL"
        )
    }
    var showSearchResult = remember {
        mutableStateOf(
            false
        )
    }
    var today by remember {
        mutableStateOf(LocalDate.now().format(DateTimeFormatter.ofPattern("d MMMM yyyy")))
    }
    var favorites = remember {
        mutableStateOf(
            listOf(
                FavoriteItem(ticker = "AAPL", name = "Apple Inc.", current = 150.00, change = -1.5, changeP=0.71),
                FavoriteItem(ticker = "GOOGL", name = "Alphabet Inc.", current = 2800.00, change = 5.4, changeP=0.04),
            )
        )
    }
    var currentWalletBalance = remember {
        mutableStateOf(
            WalletBalance()
        )
    }
    var tickerInfo = remember{
        mutableStateOf(
            TickerInfo(ticker = "AAPL", name = "Apple Inc.")
        )
    }
    var portfolioItems by remember {
        mutableStateOf(
            listOf(
                PortfolioItem(ticker = "AAPL", share = 10, totalCost = 1500.0, change = -15.0, changeP = -1.0)
            )
        )
    }
    var watchlistReceived = false
    var portfolioReceived = false
    fun updater(balance:Double = 25000.00){
        val watchlistUrl = "${URL}watchlist/GET"
        val portfolioUrl = "${URL}portfolio/GET"
        val quoteUrl = "${URL}quote/"
        val newWalletBalance = WalletBalance(cashBalance = balance)
        val allTickers = mutableListOf<String>()
        fun updateLoadingState() {
            if (watchlistReceived && portfolioReceived) {
                allTickers.forEach { ticker ->
                    val quoteUrl = "${quoteUrl}$ticker"
                    val quoteRequest = JsonObjectRequest(Request.Method.GET, quoteUrl, null,
                        { response ->
                            val currentPrice = response.getDouble("c")
                            val change = response.getDouble("d")
                            val changePercentage = response.getDouble("dp")
                            portfolioItems = portfolioItems.map {
                                if (it.ticker == ticker) it.copy(
                                    change = change,
                                    changeP = changePercentage
                                ) else it
                            }
                            favorites.value = favorites.value.map {
                                if (it.ticker == ticker) it.copy(
                                    current = currentPrice,
                                    change = change,
                                    changeP = changePercentage
                                ) else it
                            }
                        },
                        { error ->
                            Log.e("Quote", "Error fetching quote for $ticker: ${error.toString()}")
                        })
                    queue.add(quoteRequest)
                }
                loading.value = false
            }
        }
        val portfolioRequest = JsonArrayRequest(Request.Method.GET, portfolioUrl, null,
            { response ->
                val portfolioList = mutableListOf<PortfolioItem>()
                for (i in 0 until response.length()) {
                    val item = response.getJSONObject(i)
                    portfolioList.add(PortfolioItem(
                        ticker = item.getString("ticker"),
                        share = item.getInt("quantity"),
                        totalCost = item.getDouble("avgshare") * item.getInt("quantity"),
                        change = 0.0,
                        changeP = 0.0
                    ))
                    allTickers += item.getString("ticker")
                    newWalletBalance.netWorth += item.getDouble("avgshare") * item.getInt("quantity")
                    newWalletBalance.cashBalance -= item.getDouble("avgshare") * item.getInt("quantity")
                }
                portfolioItems = portfolioList
                currentWalletBalance.value = newWalletBalance
                portfolioReceived = true
                updateLoadingState()
            },
            { error ->
                Log.e("Portfolio", error.toString())
            })

        val watchlistRequest = JsonArrayRequest(Request.Method.GET, watchlistUrl, null,
            { response ->

                val watchlist = mutableListOf<FavoriteItem>()
                for (i in 0 until response.length()) {
                    val item = response.getJSONObject(i)
                    watchlist.add(FavoriteItem(
                        ticker = item.getString("ticker"),
                        name = item.getString("name"),
                        current = 0.0,
                        change = 0.0,
                        changeP = 0.0
                    ))
                    allTickers += item.getString("ticker")
                }
                favorites.value = watchlist
                watchlistReceived = true
                updateLoadingState()
            },
            { error ->
                Log.e("Watchlist", error.toString())

            })

        queue.add(portfolioRequest)
        queue.add(watchlistRequest)
    }
    PeriodicUpdateEffect(onTick = {
        updater()
        println("Home Periodic update triggered")
    })
    LaunchedEffect(query.value) {
        updater()
    }
    if (showSearchResult.value){
        Log.i("Favorite", "Query is in Favorite: ${favorites.value.any { it.ticker.equals(query.value, ignoreCase = true)}}")
        loading.value = false
        Column {
            Header(fav = favorites.value.any { it.ticker.equals(query.value, ignoreCase = true)}, searchKey=query, showSearchResult=showSearchResult, loading=loading, quote = tickerInfo, favorites = favorites)
            ResultScreen( searchKey = query, currentWalletBalance=currentWalletBalance, tickerInfo = tickerInfo)
        }
    }
    else{
        Column {
            SearchBarUI( searchKey=query, showSearchResult=showSearchResult, loading=loading)
            if (loading.value){
                Box(contentAlignment = Alignment.Center, modifier = Modifier
                    .fillMaxSize()
                    .fillMaxHeight()) {
                    CircularProgressIndicator()
                }
            }
            else{
                DateBar(today = today)
                Box(modifier = Modifier.background(color= Color(200,200,200))){
                    Text(
                        text = "PORTFOLIO",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    )
                }

                Wallet(netWorth = currentWalletBalance.value.netWorth, cashBalance = currentWalletBalance.value.cashBalance)


                LazyColumn {
                    items(items = portfolioItems) { item ->
                        FavoriteCard(
                            leftTop = item.ticker,
                            rightTop = "$" + String.format("%.2f", item.totalCost),
                            leftBottom = item.share.toString() + " shares",
                            rightBottom = "$" + item.change.toString() + "(" + String.format(
                                "%.2f",
                                item.changeP
                            ) + "%)",
                            up = item.change > 0
                        )
                        { ticker ->
                            query.value = ticker
                            showSearchResult.value = true
                        }
                    }
                }
                Box(modifier = Modifier.background(color= Color(200,200,200))) {
                    Text(
                        text = "FAVORITE",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    )
                }
                FavoritesList(favorites = favorites, query = query, showSearchResult = showSearchResult, context = context)
                Footer(context = context)
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesList(favorites: MutableState<List<FavoriteItem>>, query: MutableState<String>, showSearchResult: MutableState<Boolean>, context: Context) {
    Log.d("Favorites", "Current favorites count: ${favorites.value.size}")
    LazyColumn {
        items(items = favorites.value, key = {it.ticker}) { item ->
            var dismissState = rememberDismissState(

                confirmValueChange = {  dismissValue ->
                    if (dismissValue == DismissValue.DismissedToStart ) {
                        onDeleteFavorite(item.ticker, context, favorites)
                    }
                    true
                }
            )
            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart), // Swipe from right to left only
                background = { Background(dismissState) },
                dismissContent = {
                    FavoriteCard(
                        leftTop = item.ticker,
                        rightTop = "$${String.format("%.2f", item.current)}",
                        leftBottom = item.name,
                        rightBottom = "$${item.change} (${String.format("%.2f", item.changeP)}%)",
                        up =  item.change > 0
                    ){ ticker ->
                        query.value = ticker
                        showSearchResult.value = true
                    }
                }
            )
            Divider(color = Color.Gray, thickness = 1.dp)
        }
    }
}
fun onDeleteFavorite(ticker: String, context: Context, favorites: MutableState<List<FavoriteItem>>){
    val queue: RequestQueue = Volley.newRequestQueue(context)
    val url = "${URL}watchlist/DELETE/$ticker"

    val item = favorites.value.find { it.ticker == ticker }

    favorites.value = ArrayList(favorites.value).apply {
        removeAll { it.ticker == ticker }
    }

    val jsonObjectRequest = JsonObjectRequest(
        Request.Method.DELETE, url, null,
        { response ->
            Log.i("Favorite", "$ticker has been removed from the watchlist")
            // Item successfully removed from backend, no further action needed
        },
        { error ->
            if (item != null) {
                val newList = favorites.value.toMutableList().apply {
                    add(item)
                }
                favorites.value = newList
            }
            error.printStackTrace()
            // Optionally, you can notify the UI/user about the error.
        }
    )
    queue.add(jsonObjectRequest)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Background(dismissState: DismissState) {
    val color = Color.Red

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(8.dp)
            .fillMaxWidth()
        ,
        contentAlignment = Alignment.CenterEnd
    ) {
        // You can place an icon or text here to indicate the dismiss action
        Icon(
            painter = painterResource(id = R.drawable.delete),
            contentDescription = "Delete",
            tint = Color.White
        )
    }
}
@Composable
fun ComposeWebView(html: String) {
    AndroidView(factory = { context ->
        WebView(context).apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
        }
    }, update = { webView ->
        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
    })
}
@Composable
fun ResultScreen(searchKey: MutableState<String>, currentWalletBalance: MutableState<WalletBalance>, tickerInfo: MutableState<TickerInfo>) {
    val context = LocalContext.current
    val queue = Volley.newRequestQueue(context)
    val coroutineScope = rememberCoroutineScope()
    var scrollState = rememberScrollState()
    val historicalHtml = """
        <!DOCTYPE html>
        <html lang="en">
          <head>
            <meta charset="UTF-8" />
            <script src="https://code.highcharts.com/stock/highstock.js"></script>
            <script src="https://code.highcharts.com/indicators/indicators.js"></script>
            <script src="https://code.highcharts.com/indicators/indicators-all.js"></script>
            <script src="https://code.highcharts.com/stock/modules/data.js"></script>
            <script src="https://code.highcharts.com/stock/modules/drag-panes.js"></script>
            <script src="https://code.highcharts.com/stock/modules/exporting.js"></script>
            <script src="https://code.highcharts.com/stock/indicators/indicators.js"></script>
            <script src="https://code.highcharts.com/stock/indicators/volume-by-price.js"></script>
            <script src="https://code.highcharts.com/modules/accessibility.js"></script>
            <script src="https://code.highcharts.com/modules/sma.js"></script>
          </head>
          <body>
            <div id="container" style="height: 100%; min-width: 310px"></div>
            <script>
              const url = "https://csci571hw3shingomorita8843v2.uw.r.appspot.com/api"; // Change this to your API endpoint
        
              async function fetchHistoricalPrices() {
                ticker = "AAPL";
                const queryParams = new URLSearchParams({
                  timeNumber: 1,
                  timeUnit: "day",
                  fromDate: "2022-01-01",
                  toDate: "2024-04-15",
                });
                console.log("Query Params", queryParams.toString());
                console.log(`${URL}historical/${searchKey.value}?` + queryParams.toString());
                try {
                  const response = await fetch(
                    `${URL}historical/${searchKey.value}?` + queryParams.toString(),
                    {
                      method: "GET",
                      headers: {
                        Accept: "application/json",
                        "Content-Type": "application/json",
                      },
                    }
                  );
                  const data = await response.json(); // Ensure that the JSON data is fully parsed before proceeding
                  updateChart(data); // Then update the chart with this data
                } catch (error) {
                  console.error("Failed to fetch historical data:", error);
                  document.getElementById("container").innerHTML =
                    "<div>Error loading data.</div>";
                }
              }
        
              function updateChart(historicalPrices) {
                console.log("Historical Prices:", historicalPrices);
                let ohlc = [];
                let volume = [];
        
                for (let i = 0; i < historicalPrices.results.length; i++) {
                  let date = new Date(historicalPrices.results[i].t);
                  ohlc.push([
                    historicalPrices.results[i].t,
                    historicalPrices.results[i].o,
                    historicalPrices.results[i].h,
                    historicalPrices.results[i].l,
                    historicalPrices.results[i].c,
                  ]);
                  volume.push([
                    historicalPrices.results[i].t,
                    historicalPrices.results[i].v,
                  ]);
                }
                const groupingUnits = [
                  ["week", [1]],
                  ["month", [1, 3, 6]],
                  ["year", [1]],
                ];
                const options = {
                  chart: {
                    backgroundColor: "#f8f9fa",
                  },
                  rangeSelector: {
                    enabled: true,
                    selected: 1,
                    buttons: [
                      {
                        type: "month",
                        count: 1,
                        text: "1m",
                      },
                      {
                        type: "month",
                        count: 3,
                        text: "3m",
                      },
                      {
                        type: "month",
                        count: 6,
                        text: "6m",
                      },
                      {
                        type: "ytd",
                        text: "YTD",
                      },
                      {
                        type: "year",
                        count: 1,
                        text: "1y",
                      },
                      {
                        type: "all",
                        text: "All",
                      },
                    ],
                  },
                  legend: {
                    enabled: false,
                  },
                  title: {
                    text: historicalPrices.ticker + " Historical",
                  },
        
                  subtitle: {
                    text: "With SMA and Volume by Price technical indicators",
                  },
                  navigator: {
                    enabled: true,
                    height: 40,
                    margin: 5,
                    xAxis: {
                      labels: {
                        format: "{value:%e %b}",
                        style: {
                          color: "#666",
                          fontSize: "10px",
                        },
                      },
                    },
                  },
        
                  xAxis: {
                    type: "datetime",
                    dateTimeLabelFormats: {
                      day: "%e %b",
                    },
                  },
        
                  yAxis: [
                    {
                      startOnTick: false,
                      endOnTick: false,
                      labels: {
                        align: "right",
                        x: -3,
                      },
                      title: {
                        text: "OHLC",
                      },
                      height: "60%",
                      lineWidth: 2,
                      resize: {
                        enabled: true,
                      },
                      opposite: true,
                    },
                    {
                      labels: {
                        align: "right",
                        x: -3,
                      },
                      title: {
                        text: "Volume",
                      },
                      top: "65%",
                      height: "35%",
                      offset: 0,
                      lineWidth: 2,
                      opposite: true,
                    },
                  ],
        
                  tooltip: {
                    split: true,
                  },
        
                  plotOptions: {
                    series: {
                      dataGrouping: {
                        units: groupingUnits,
                      },
                    },
                  },
        
                  series: [
                    {
                      type: "candlestick",
                      name: historicalPrices.ticker,
                      id: historicalPrices.ticker,
                      zIndex: 2,
                      data: ohlc,
                    },
                    {
                      type: "column",
                      name: "Volume",
                      id: "volume",
                      data: volume,
                      yAxis: 1,
                    },
                    {
                      type: "vbp",
                      linkedTo: historicalPrices.ticker,
                      params: {
                        volumeSeriesID: "volume",
                      },
                      dataLabels: {
                        enabled: false,
                      },
                      zoneLines: {
                        enabled: false,
                      },
                    },
                    {
                      type: "sma",
                      linkedTo: historicalPrices.ticker,
                      zIndex: 1,
                      marker: {
                        enabled: false,
                      },
                    },
                  ],
                };
        
                Highcharts.stockChart("container", options);
              }
        
              // Assuming the current UNIX timestamp and a ticker symbol are known
              document.addEventListener("DOMContentLoaded", function () {
                fetchHistoricalPrices();
              });
            </script>
          </body>
        </html>
        
        
        """
    val hourlyhtml = """
        <!DOCTYPE html>
        <html lang="en">
          <head>
            <meta charset="UTF-8" />
            <script src="https://code.highcharts.com/stock/highstock.js"></script>
            <script src="https://code.highcharts.com/indicators/indicators.js"></script>
            <script src="https://code.highcharts.com/indicators/indicators-all.js"></script>
            <script src="https://code.highcharts.com/stock/modules/data.js"></script>
            <script src="https://code.highcharts.com/stock/modules/drag-panes.js"></script>
            <script src="https://code.highcharts.com/stock/modules/exporting.js"></script>
            <script src="https://code.highcharts.com/stock/indicators/indicators.js"></script>
            <script src="https://code.highcharts.com/stock/indicators/volume-by-price.js"></script>
            <script src="https://code.highcharts.com/modules/accessibility.js"></script>
            <script src="https://code.highcharts.com/modules/sma.js"></script>
          </head>
          <body>
            <div id="container" style="height: 100%; min-width: 310px"></div>
            <script>        
                async function fetchHistoricalPrices() {
                  const ticker = "${searchKey.value}";
                  const queryParams = new URLSearchParams({
                    timeNumber: 1,
                    timeUnit: "hour",
                    fromDate: "2022-04-15",
                    toDate: "2024-04-15",
                  });
                  console.log("Query Params", queryParams.toString());
                  
                  try {
                    const response = await fetch(`${URL}historical/${searchKey.value}?` + queryParams.toString(), {
                      method: "GET",
                      headers: {
                        Accept: "application/json",
                        "Content-Type": "application/json",
                      },
                    });
                    const data = await response.json(); // Ensure that the JSON data is fully parsed before proceeding
                    if (data && data.results) {
                      updateChart(data.results.slice(0, 9), ticker); // Then update the chart with this data
                    } else {
                      console.error("No results found");
                      document.getElementById("container").innerHTML = "<div>No data available.</div>";
                    }
                  } catch (error) {
                    console.error("Failed to fetch historical data:", error);
                    document.getElementById("container").innerHTML = "<div>Error loading data.</div>";
                  }
                }
                
                function updateChart(hourlyPrices, ticker) {
                  if (!hourlyPrices) {
                    document.getElementById("container").innerHTML = "<div>Loading...</div>";
                    return;
                  }
                  const hours = hourlyPrices.map(result => new Date(result.t * 1000).getHours() + ":00");
                  const prices = hourlyPrices.map(result => result.c);
                  const options = {
                    chart: {
                      type: "line",
                      renderTo: 'container'
                    },
                    legend: {
                      enabled: false,
                    },
                    title: {
                      text: `${searchKey.value} Stock Price Hourly`,
                    },
                    xAxis: {
                      categories: hours,
                    },
                    yAxis: {
                      title: {
                        text: "Price",
                      },
                    },
                    series: [
                      {
                        name: "Stock Price",
                        data: prices,
                        color: "#28a745",
                      },
                    ],
                  };
                  Highcharts.chart(options);
                }
              // Assuming the current UNIX timestamp and a ticker symbol are known
              document.addEventListener("DOMContentLoaded", function () {
                fetchHistoricalPrices();
              });
            </script>
          </body>
        </html>        
        """
    val recommendationhtml = """
        <!DOCTYPE html>
        <html lang="en">
          <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <title>Recommendation Trends</title>
            <script src="https://code.highcharts.com/stock/highstock.js"></script>
            <script src="https://code.highcharts.com/indicators/indicators.js"></script>
            <script src="https://code.highcharts.com/indicators/indicators-all.js"></script>
            <script src="https://code.highcharts.com/stock/modules/data.js"></script>
            <script src="https://code.highcharts.com/stock/modules/drag-panes.js"></script>
            <script src="https://code.highcharts.com/stock/modules/exporting.js"></script>
            <script src="https://code.highcharts.com/stock/indicators/indicators.js"></script>
            <script src="https://code.highcharts.com/stock/indicators/volume-by-price.js"></script>
            <script src="https://code.highcharts.com/modules/accessibility.js"></script>
            <script src="https://code.highcharts.com/modules/sma.js"></script>
          </head>
          <body>
            <div id="container" style="width: 100%; height: 400px"></div>
            <script>
              async function fetchAndDisplayRecommendations() {
                const URL = "https://csci571hw3shingomorita8843v2.uw.r.appspot.com/api/";
                const ticker = "AAPL";
        
                try {
                  const response = await fetch(`${URL}recommendation/${searchKey.value}`);
                  const recommendationTrend = await response.json();
        
                  const categories = recommendationTrend.map((item) => item.period).reverse();
                  const recommendationTrendData = {
                    "Strong Buy": recommendationTrend.map((item) => item.strongBuy),
                    Buy: recommendationTrend.map((item) => item.buy),
                    Hold: recommendationTrend.map((item) => item.hold),
                    Sell: recommendationTrend.map((item) => item.sell),
                    "Strong Sell": recommendationTrend.map((item) => item.strongSell),
                  };
        
                  Highcharts.chart("container", {
                    chart: { type: "column", backgroundColor: "#f4f4f4" },
                    title: { text: "Recommendation Trends" },
                    xAxis: { categories: categories },
                    yAxis: { min: 0, title: { text: "Number of Analysts" } },
                    tooltip: {
                      pointFormat:
                        '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b> ({point.percentage:.0f}%)<br/>',
                      shared: true,
                      useHTML: true,
                    },
                    plotOptions: {
                      column: {
                        stacking: "normal",
                        pointPadding: 0.2,
                        borderWidth: 0,
                        dataLabels: {
                          enabled: true,
                          color: "black",
                        },
                      },
                    },
                    series: [
                      {
                        name: "Strong Buy",
                        data: recommendationTrendData["Strong Buy"].reverse(),
                        color: "#006837",
                      },
                      {
                        name: "Buy",
                        data: recommendationTrendData.Buy.reverse(),
                        color: "#4daf4a",
                      },
                      {
                        name: "Hold",
                        data: recommendationTrendData.Hold.reverse(),
                        color: "#fdae61",
                      },
                      {
                        name: "Sell",
                        data: recommendationTrendData.Sell.reverse(),
                        color: "#d73027",
                      },
                      {
                        name: "Strong Sell",
                        data: recommendationTrendData["Strong Sell"].reverse(),
                        color: "#7F3232",
                      },
                    ],
                  });
                } catch (error) {
                  console.error("Failed to fetch recommendation data:", error);
                  document.getElementById("container").innerHTML = "<div>Error loading data.</div>";
                }
              }
        
              fetchAndDisplayRecommendations();
            </script>
          </body>
        </html>

    """
    val historicalEPShtml = """
        <!DOCTYPE html>
        <html lang="en">
          <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <title>Historical EPS Surprises</title>
            <script src="https://code.highcharts.com/stock/highstock.js"></script>
            <script src="https://code.highcharts.com/indicators/indicators.js"></script>
            <script src="https://code.highcharts.com/indicators/indicators-all.js"></script>
            <script src="https://code.highcharts.com/stock/modules/data.js"></script>
            <script src="https://code.highcharts.com/stock/modules/drag-panes.js"></script>
            <script src="https://code.highcharts.com/stock/modules/exporting.js"></script>
            <script src="https://code.highcharts.com/stock/indicators/indicators.js"></script>
            <script src="https://code.highcharts.com/stock/indicators/volume-by-price.js"></script>
            <script src="https://code.highcharts.com/modules/accessibility.js"></script>
            <script src="https://code.highcharts.com/modules/sma.js"></script>
          </head>
          <body>
            <div id="container" style="width: 100%; height: 400px"></div>
            <script>
              const URL = "https://csci571hw3shingomorita8843v2.uw.r.appspot.com/api/";
              async function fetchAndDisplayEPS() {
                try {
                  const response = await fetch(`${URL}earnings/${searchKey.value}`);
                  const historicalEPSSurprises = await response.json();
        
                  if (!historicalEPSSurprises) {
                    console.error("No data received or invalid format");
                    document.getElementById("container").innerHTML =
                      "<div>No data available.</div>";
                    return;
                  }
        
                  const actualEPS = historicalEPSSurprises.map((surprise, index) => {
                    return {
                      x: index,
                      y: surprise.actual,
                      surprise: surprise.surprise,
                      period: surprise.period,
                    };
                  });
                  const estimatedEPS = historicalEPSSurprises.map((surprise, index) => {
                    return {
                      x: index,
                      y: surprise.estimate,
                      surprise: surprise.surprise,
                      period: surprise.period,
                    };
                  });
        
                  Highcharts.chart("container", {
                    chart: {
                      type: "spline",
                      backgroundColor: "#f4f4f4",
                    },
                    title: {
                      text: "Historical EPS Surprises",
                    },
                    xAxis: {
                      categories: historicalEPSSurprises.map(
                        (surprise) =>
                          surprise.period + "<br/> Surprise: " + surprise.surprise
                      ),
                    },
                    yAxis: {
                      title: {
                        text: "Quarterly EPS",
                      },
                      min: 1,
                    },
                    tooltip: {
                      headerFormat: "<b>{series.name}</b><br>",
                      pointFormat:
                        "Date: {point.period}<br>Surprise: {point.surprise:.4f}<br>Earnings per Share: {point.y:.2f}",
                    },
                    plotOptions: {
                      spline: {
                        marker: {
                          enabled: true,
                        },
                      },
                    },
                    series: [
                      {
                        name: "Actual EPS",
                        data: actualEPS,
                        color: "blue",
                      },
                      {
                        name: "Estimated EPS",
                        data: estimatedEPS,
                        color: "lightblue",
                      },
                    ],
                  });
                } catch (error) {
                  console.error("Failed to fetch EPS data:", error);
                  document.getElementById("container").innerHTML =
                    "<div>Error loading data.</div>";
                }
              }
        
              fetchAndDisplayEPS();
            </script>
          </body>
        </html>
    """
    var selectedTabIndex by remember { mutableStateOf(0) }
    val icons = listOf(
        TabIcon(iconResId = R.drawable.chart_hour, contentDescription = "Hourly"),
        TabIcon(iconResId = R.drawable.chart_historical, contentDescription = "Historical"),
    )
    var newsArticles by remember {
        mutableStateOf(
            listOf(
                NewsArticle(
                    title = "Tech Innovation in 2024",
                    source = "Tech News",
                    imageUrl = "https://example.com/images/article1.jpg",
                    publishedDate = LocalDateTime.now(),
                    description = "An in-depth look at upcoming technology innovations expected to revolutionize the industry in 2024.",
                    url = "https://example.com/news/tech-innovation-2024"
                ),
                NewsArticle(
                    title = "Market Analysis for Q2",
                    source = "Market Watch",
                    imageUrl = "https://example.com/images/article2.jpg",
                    publishedDate = LocalDateTime.now().minusDays(1),
                    description = "Detailed analysis of Q2 market trends and expert predictions for the stock market's performance.",
                    url = "https://example.com/news/market-analysis-q2"
                ),
                NewsArticle(
                    title = "Global Politics Review",
                    source = "Global News",
                    imageUrl = "https://example.com/images/article3.jpg",
                    publishedDate = LocalDateTime.now().minusWeeks(1),
                    description = "A review of recent developments in global politics, including elections, policy changes, and international relations.",
                    url = "https://example.com/news/global-politics-review"
                )
            )
        )
    }
    var loaded by remember { mutableStateOf(0) }
    val quote = remember {
        mutableStateOf(
            FavoriteItem()
        )
    }
    val portfolioItem = remember {
        mutableStateOf(
            ResultPortfolio()
        )
    }
    var showTradeModal = remember {
        mutableStateOf(
            false
        )
    }
    var count = remember {
        mutableStateOf(
            0
        )
    }
    var about = remember {
        mutableStateOf(
            About()
        )
    }
    var stats = remember {
        mutableStateOf(
            Stats()
        )
    }
    var socialSentiment = remember {
        mutableStateOf(
            SocialSentiments()
        )
    }
    var showSuccessMessage = remember {
        mutableStateOf(
            false
        )
    }
    var successMessage = remember {
        mutableStateOf(
            ""
        )
    }
    fun buyStock(portfolioItem: MutableState<ResultPortfolio>, ticker: String, number: Int, currentWalletBalance: MutableState<WalletBalance>) {
        coroutineScope.launch {
            val url = if (portfolioItem.value.sharesOwned > 0) {
                "${URL}portfolio/UPDATE/${ticker}"
            } else {
                "${URL}portfolio/ADD"
            }
            val method = if (portfolioItem.value.sharesOwned > 0) {
                Request.Method.PUT
            } else {
                Request.Method.POST
            }
            println(url)
            val price = portfolioItem.value.marketValue
            val share = portfolioItem.value.sharesOwned + number
            val total = portfolioItem.value.totalCost + price * number
            val avg = total / share
            val change = price - avg
            val jsonRequest = JSONObject().apply {
                put("ticker", searchKey.value)
                put("quantity", share)
                put("avgshare", avg)
                put("price", price)
                put("name", portfolioItem.value.name)
            }
//            PUT for update, Post for add
            val jsonObjectRequest = JsonObjectRequest(
                method, url, jsonRequest,
                { response ->
                    // Update portfolioItems
                    portfolioItem.value = ResultPortfolio(ticker, sharesOwned = share, avgCostPerShare = avg, totalCost = total, change = change, marketValue = price, name =  portfolioItem.value.name)
                    // Update Wallet
                    currentWalletBalance.value = WalletBalance(currentWalletBalance.value.netWorth - price * number, currentWalletBalance.value.cashBalance - price * number)
                    successMessage.value = "You have successfully bought $number shares of ${searchKey.value} "
                    showSuccessMessage.value = true
                },
                { error ->
                    Log.e("Buy", error.toString())
                }
            )
            queue.add(jsonObjectRequest)
        }
    }
    fun sellStock(portfolioItem: MutableState<ResultPortfolio>, ticker: String, number: Int, currentWalletBalance: MutableState<WalletBalance>) {
        coroutineScope.launch {
            val url = if (number < portfolioItem.value.sharesOwned) {
                "${URL}portfolio/UPDATE/${ticker}"
            } else {
                "${URL}portfolio/DELETE/${ticker}"
            }
            val method = if (number < portfolioItem.value.sharesOwned) {
                Request.Method.PUT
            } else {
                Request.Method.DELETE
            }
//            PUT for update, DELETE for delete
            val price = portfolioItem.value.marketValue
            val share = portfolioItem.value.sharesOwned - number
            val total = portfolioItem.value.totalCost - price * number
            val avg = if(share < 1) 0.0 else total / share
            val change = price - avg

            val RequestBody = if (number < portfolioItem.value.sharesOwned) {
                JSONObject().apply {
                    put("ticker", searchKey.value)
                    put("quantity", share)
                    put("avgshare", avg)
                    put("price", price)
                    put("name", portfolioItem.value.name)
                }
            }
            else{
                null
            }
            val jsonObjectRequest = JsonObjectRequest(
                method, url, RequestBody,
                { response ->
                    // Update portfolioItems
                    portfolioItem.value = ResultPortfolio(ticker, sharesOwned = share, avgCostPerShare = avg, totalCost = total, change = change, marketValue = price, name =  portfolioItem.value.name)
                    // Update Wallet
                    currentWalletBalance.value = WalletBalance(currentWalletBalance.value.netWorth + price * number, currentWalletBalance.value.cashBalance + price * number)
                    successMessage.value = "You have successfully sold $number shares of ${searchKey.value} "
                    showSuccessMessage.value = true

                },
                { error ->
                    Log.e("SELL", error.toString())
                }
            )
            queue.add(jsonObjectRequest)
        }
    }
    fun updater(){
        coroutineScope.launch {
            fetchNewsArticles(searchKey.value, context) { articles ->
                newsArticles = articles
                Log.i("News", "News Fetched for ${searchKey.value}")
            }
            fetchPortfolio(searchKey.value, context, walletBalance = currentWalletBalance, price = 0.0) { item ->
                portfolioItem.value.ticker = item.ticker
                portfolioItem.value.sharesOwned = item.sharesOwned
                portfolioItem.value.avgCostPerShare = item.avgCostPerShare
                portfolioItem.value.totalCost = item.totalCost
                Log.i("Portfolio", "Portfolio Fetched for ${searchKey.value}")
            }
            fetchQuote(searchKey.value, context) { quotedata ->
                quote.value.ticker = quotedata.ticker
                quote.value.change = quotedata.change
                quote.value.changeP = quotedata.changeP
                quote.value.current = quotedata.current
                portfolioItem.value.marketValue = quotedata.current
                stats.value.high = quotedata.high
                stats.value.low = quotedata.low
                stats.value.open = quotedata.open
                stats.value.prev = quotedata.prev
                Log.i("Quote", "Quote Fetched for ${searchKey.value}")

            }
            fetchCompany(searchKey.value, context) { company ->
                quote.value.name = company.name
                portfolioItem.value.name = company.name
                about.value.IPO = company.IPO
                about.value.industry = company.industry
                about.value.website = company.website
                Log.i("Company", "Company Fetched for ${searchKey.value}")
            }
            fetchPeers(searchKey.value, context){peers->
                about.value.peers = peers
                Log.i("Peer", "Peer Fetched for ${searchKey.value}")
            }
            fetchSocialSentiment(searchKey.value, context){data ->
                socialSentiment.value = data
                Log.i("Sentiment", "Sentiment Fetched for ${searchKey.value}")
            }
        }
    }
//    TODO
//    PeriodicUpdateEffect {
//        updater()
//        println("Result Screen Periodic Update Triggered")
//    }
    LaunchedEffect(searchKey.value) {
        loaded = 0
        coroutineScope.launch {
            fetchNewsArticles(searchKey.value, context) { articles ->
                newsArticles = articles
                loaded += 1
                Log.i("News", "News Fetched for ${searchKey.value}")
            }
            fetchPortfolio(searchKey.value, context, walletBalance = currentWalletBalance, price = 0.0) { item ->
                portfolioItem.value.ticker = item.ticker
                portfolioItem.value.sharesOwned = item.sharesOwned
                portfolioItem.value.avgCostPerShare = item.avgCostPerShare
                portfolioItem.value.totalCost = item.totalCost
                loaded += 1
                Log.i("Portfolio", "Portfolio Fetched for ${searchKey.value}")
            }
            fetchQuote(searchKey.value, context) { quotedata ->
                quote.value.ticker = quotedata.ticker
                quote.value.change = quotedata.change
                quote.value.changeP = quotedata.changeP
                quote.value.current = quotedata.current
                portfolioItem.value.marketValue = quotedata.current
                portfolioItem.value.change = quotedata.change
                stats.value.high = quotedata.high
                stats.value.low = quotedata.low
                stats.value.open = quotedata.open
                stats.value.prev = quotedata.prev
                loaded += 1
                Log.i("Quote", "Quote Fetched for ${searchKey.value}")

            }
            fetchCompany(searchKey.value, context) { company ->
                quote.value.name = company.name
                portfolioItem.value.name = company.name
                socialSentiment.value.name = company.name
                tickerInfo.value = TickerInfo(name =  company.name)
                about.value.IPO = company.IPO
                about.value.industry = company.industry
                about.value.website = company.website
                loaded += 1
                Log.i("Company", "Company Fetched for ${searchKey.value}")
            }
            fetchPeers(searchKey.value, context){peers->
                about.value.peers = peers
                loaded += 1
                Log.i("Peer", "Peer Fetched for ${searchKey.value}")
            }
            fetchSocialSentiment(searchKey.value, context){data ->
                socialSentiment.value = data
                loaded += 1
                Log.i("Sentiment", "Sentiment Fetched for ${searchKey.value}")
            }

        }
    }
    Box(contentAlignment = Alignment.Center,
            modifier = Modifier
        .fillMaxSize()
        ) {
        if (loaded < numberOfAllData) {      // Assuming numberOfAllData is the count of data you're expecting
            CircularProgressIndicator()        // Show the loading indicator while data is loading
        } else{
            Column(
                modifier = Modifier
                    .verticalScroll(enabled = true, state = scrollState)
                    .fillMaxSize()
                    .fillMaxHeight()
            ) {

                ResultFav(
                    leftTop = quote.value.ticker,
                    rightTop = "$" + quote.value.current.toString(),
                    leftBottom = quote.value.name,
                    rightBottom = "$"+quote.value.change.toString() + "(" + String.format(
                        "%.2f",
                        quote.value.changeP
                    ) + "%)",
                    up = quote.value.change > 0
                )
                when (selectedTabIndex) {
                    0 -> ComposeWebView(html = hourlyhtml)
                    1 -> ComposeWebView(html = historicalHtml )
                }
                TabRow(selectedTabIndex = selectedTabIndex) {
                    icons.forEachIndexed { index, tabIcon ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            selectedContentColor = Color.Blue,
                            unselectedContentColor = Color.Black,
                            icon = {
                                Icon(
                                    painter = painterResource(id = tabIcon.iconResId),
                                    contentDescription = tabIcon.contentDescription
                                )
                            }
                        )
                    }
                }
                Text("  Portfolio", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                PortfolioResult(portfolioItem = portfolioItem.value) {
                    showTradeModal.value = true
                }
                Text("  Stats", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Statics(stats = stats.value)
                Text("  About", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                AboutSection(about = about.value, searchKey=searchKey)
                Text("  Insight", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                SocialSentimentSection(socialSentiments = socialSentiment.value)
                ComposeWebView(html =recommendationhtml)
                ComposeWebView(html = historicalEPShtml)
                Text("  News", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                NewsScreen(newsArticles = newsArticles)
                TradeDialog(
                    showDialog = showTradeModal,
                    currentWalletBalance = currentWalletBalance,
                    portfolioItem = portfolioItem,
                    count = count
                ) { trade ->
                    if (trade) {
                        Log.i("Trade", "Buy #${count.value} ${searchKey.value} ")
                        buyStock(portfolioItem = portfolioItem, ticker=searchKey.value, number =  count.value, currentWalletBalance = currentWalletBalance)
                    } else {
                        Log.i("Trade", "Sell #${count.value} ${searchKey.value} ")
                        sellStock(portfolioItem = portfolioItem, ticker=searchKey.value, number =  count.value, currentWalletBalance = currentWalletBalance)
                    }
                }
                SuccessDialog(message = successMessage.value, showDialog = showSuccessMessage)

            }

        }
    }
}
@Composable
fun ResultFav(leftTop: String, rightTop: String, leftBottom: String, rightBottom: String, up: Boolean = true) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White),
    ) {
        Row(){
            Column(modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 5.dp,)
                .weight(1f)) {
                Row (modifier = Modifier
                    .padding(horizontal = 32.dp)){
                    Text(
                        text = leftTop,
                        modifier = Modifier.weight(1f),
                        style = androidx.compose.ui.text.TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 18.sp
                        )
                    )
                    Text(
                        text = rightTop,
                        style = androidx.compose.ui.text.TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 18.sp
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row (modifier = Modifier
                    .padding(horizontal = 32.dp)){
                    Text(
                        text = leftBottom,
                        modifier = Modifier.weight(1f) ,
                        style = androidx.compose.ui.text.TextStyle(
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    )
                    Row(modifier = Modifier, ){
                        Icon( painter = if (up) painterResource(id = R.drawable.trending_up) else painterResource(id = R.drawable.trending_down) , contentDescription = "", tint = if (up) Color.Green else Color.Red)
                        Text(text = rightBottom, style = androidx.compose.ui.text.TextStyle(
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            color = if(up) Color.Green else Color.Red
                        ))
                    }
                }
            }
        }
    }
}
@Composable
fun SuccessDialog(message: String, showDialog: MutableState<Boolean>) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp)
                ,
                contentAlignment = Alignment.Center
            ){Text(text = "Congratulations!") }},
            text = { Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                ,
                contentAlignment = Alignment.Center
            ){Text(text = message, textAlign = TextAlign.Center,)}},
            confirmButton = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp)
                    ,
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { showDialog.value = false },
                        modifier = Modifier

                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(5.dp)) {
                        Text("Done",color = Color.Green, fontWeight = FontWeight.Bold)
                    }
                }
            },

            containerColor = Color(34, 187, 51),
            titleContentColor = Color(255, 255, 255),
            textContentColor = Color(255, 255, 255)
        )
    }
}
@Composable
fun TradeDialog(showDialog: MutableState<Boolean>, currentWalletBalance: MutableState<WalletBalance>, portfolioItem: MutableState<ResultPortfolio>, count: MutableState<Int>, onTradeCompletion: (Boolean) -> Unit ) {
    val context = LocalContext.current
    val inputText = remember { mutableStateOf("") }

    if (showDialog.value) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showDialog.value = false },
            title = {
                Box(contentAlignment = Alignment.Center, modifier = Modifier
                    .fillMaxWidth(1f)

                ){
                    Text(text = "Trade ${portfolioItem.value.name} Shares")
                }

                    },
            text = {
                val numShares = inputText.value.toDoubleOrNull() ?: 0.0
                val tradeCost = numShares * portfolioItem.value.marketValue
                Column {

                    Row (){
                        Column (modifier =  Modifier.weight(1f)){
                            TextField(
                                value = inputText.value,
                                onValueChange = { newValue ->
                                    inputText.value = newValue.filter { char -> char.isDigit() || char == '.' }
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(4.dp)),
                                textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedTextColor = Color.Black,
                                    disabledTextColor = Color.Transparent,
                                    focusedContainerColor = Color(255,255,255),
                                    unfocusedContainerColor = Color.White,
                                    focusedIndicatorColor = Color.Cyan,
                                    unfocusedIndicatorColor = Color.Cyan,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                placeholder = { Text(text = "0")}
                            )
                        }
                        Column (modifier =  Modifier.align(Alignment.CenterVertically)){
                            Text(text = "          shares")
                        }

                    }

                    Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(vertical = 16.dp)
                        ){
                        Text(text = "${numShares}*${portfolioItem.value.marketValue}/share = ${tradeCost}")
                    }
                    Box(contentAlignment = Alignment.Center, modifier = Modifier
                        .fillMaxWidth(1f)
                        ){
                        Text(text = "$${String.format("%.2f", currentWalletBalance.value.cashBalance)} to Buy ${portfolioItem.value.ticker}", color=Color.Gray)
                    }
                }
            },
            confirmButton = {
                Row(horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()){

                    Button(
                        onClick = {
                            val numShares = inputText.value.toIntOrNull()
                            when {
                                numShares == null ->{
                                    Toast.makeText(context, "Please enter a valid amount", Toast.LENGTH_LONG).show()
                                }
                                numShares <= 0 -> {
                                    Toast.makeText(context, "Cannot buy non-positive shares", Toast.LENGTH_LONG).show()
                                }
                                numShares * portfolioItem.value.marketValue > currentWalletBalance.value.cashBalance -> {
                                    Toast.makeText(context, "Not enough money to buy", Toast.LENGTH_LONG).show()
                                }
                                else -> {
                                    count.value = numShares.toInt()
                                    onTradeCompletion(true)
                                    showDialog.value = false
                                }
                            } },
                        modifier = Modifier
                            .padding(start = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Text("BUY", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { val numShares = inputText.value.toIntOrNull()
                            when {
                                numShares == null ->{
                                    Toast.makeText(context, "Please enter a valid amount", Toast.LENGTH_LONG).show()
                                }
                                numShares <= 0 -> {
                                    Toast.makeText(context, "Cannot sell non-positive shares", Toast.LENGTH_LONG).show()
                                }
                                numShares > portfolioItem.value.sharesOwned -> {
                                    Toast.makeText(context, "Not enough shares to sell", Toast.LENGTH_LONG).show()
                                }
                                else -> {
                                    count.value = numShares.toInt()
                                    onTradeCompletion(false) // Signal a sell operation
                                    showDialog.value = false
                                }
                            } },
                        modifier = Modifier
                            .padding(start = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Text("SELL", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        )
    }
}
@Composable
fun SocialSentimentSection(socialSentiments: SocialSentiments) {
    Box(
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .fillMaxWidth()
    ) {
        Column {
            Box(modifier = Modifier
                .padding(vertical = 32.dp)
                .fillMaxWidth(), contentAlignment = Alignment.Center){
                Text("Social Sentiments", style = MaterialTheme.typography.titleLarge)
            }
            Column(){
                Row() {
                    Text(socialSentiments.name,
                        Modifier
                            .weight(1f)
                            .background(color = Color(222, 222, 222)))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("MSRP", Modifier
                        .weight(1f)
                        .background(color = Color(222, 222, 222)))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Change", Modifier
                        .weight(1f)
                        .background(color = Color(222, 222, 222)))
                }
                SocialSentimentRow("Total", socialSentiments.totalMSPR, socialSentiments.totalChange)
                SocialSentimentRow("Positive", socialSentiments.posMSPR, socialSentiments.posChange)
                SocialSentimentRow("Negative", socialSentiments.negMSPR, socialSentiments.negChange)
            }
        }
    }
}
@Composable
fun SocialSentimentRow(label: String, mspr: Double, change: Double) {
    Divider(
        color = Color.White,
        thickness = 3.dp
    )
    Row(Modifier.fillMaxWidth()) {
        Text(label, Modifier
            .weight(1f)
            .background(color = Color(222, 222, 222)))
        Spacer(modifier = Modifier.width(3.dp))
        Text("%.2f".format(mspr), Modifier
            .weight(1f)
            .background(color = Color(240, 240, 240)))
        Spacer(modifier = Modifier.width(3.dp))
        Text("%,.0f".format(change), Modifier
            .weight(1f)
            .background(color = Color(240, 240, 240)))
    }
}
@Composable
fun Statics(stats: Stats){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),) {
        Row {
            Column(modifier = Modifier
                .padding(8.dp)) {
                Text(text = "Open Price: ")
                Text(text = "High Price: ")
            }
            Column(modifier = Modifier
                .padding(vertical = 8.dp)) {
                Text(text = "$"+stats.open.toString())
                Text(text = "$"+stats.high.toString())
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(modifier = Modifier
                .padding(vertical = 8.dp)) {
                Text(text = "Low Price: ")
                Text(text = "Prev. Price: ")
            }
            Column(modifier = Modifier
                .padding(8.dp)) {
                Text(text = "$"+stats.low.toString())
                Text(text = "$"+stats.prev.toString())
            }
        }
    }
}
@Composable
fun AboutSection(about: About, searchKey: MutableState<String>){
    val context = LocalContext.current
    val annotatedString = buildAnnotatedString {
        about.peers.forEachIndexed { index, peer ->
            // Attach a string annotation that stores the peer
            pushStringAnnotation(tag = "URL", annotation = peer)
            withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                append(peer)
            }
            pop()
            if (index < about.peers.size - 1) {
                append(", ")
            }
        }
    }
    val annotatedStringWeb = buildAnnotatedString {
        pushStringAnnotation(tag = "URL", annotation = about.website)
        withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
            append(about.website)
        }
        pop()
    }
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),){
        Row{
            Column {
                Text(text = "IPO Start Date    ")
                Text(text = "Industry          ")
                Text(text = "Webpage           ")
                Text(text = "Company Peers     ")
            }
            Column {
                Text(text = about.IPO)
                Text(text = about.industry)
                ClickableText(
                    text = annotatedStringWeb,
                    onClick = { offset ->
                        // Retrieve the annotations at the click position
                        annotatedStringWeb.getStringAnnotations(tag = "URL", start = offset, end = offset)
                            .firstOrNull()?.let { annotation ->
                                // Use the annotation as the URL
                                openUrl(annotation.item, context)
                            }
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    itemsIndexed(about.peers) { index, peer ->
                        val annotatedString = buildAnnotatedString {
                            pushStringAnnotation(tag = "URL", annotation = peer)
                            withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                                append(peer)
                            }
                            pop()
                        }
                        ClickableText(
                            text = annotatedString,
                            onClick = { offset ->
                                annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                                    .firstOrNull()?.let { annotation ->
                                        // Trigger your desired action here
                                        onClickPeer(annotation.item, searchKey)
                                    }
                            }
                        )
                        if (index < about.peers.size - 1) {
                            Text(text = ", ", modifier = Modifier.padding(end = 8.dp))
                        }
                    }
                }
            }
        }
    }

}
fun onClickPeer(peer: String, searchKey: MutableState<String>){
//    To Be Implemented
    searchKey.value = peer
    Log.i("Peers", "Clicked on $peer")
}
@Composable
fun PortfolioResult(portfolioItem: ResultPortfolio, onTradeClick: () -> Unit) {
    val change = portfolioItem.totalCost - portfolioItem.marketValue*portfolioItem.sharesOwned
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Shares Owned:")
            Text("Avg. Cost/Share:")
            Text("Total Cost:")
            Text("Change:")
            Text("Market Value:")
        }
        if(portfolioItem.sharesOwned > 0){
            Column(modifier = Modifier.weight(1f)) {
                Text("${portfolioItem.sharesOwned}")
                Text("$${String.format("%.2f", portfolioItem.avgCostPerShare)}")
                Text("$${String.format("%.2f", portfolioItem.totalCost)}")
                Text("$${String.format("%.2f", change)}", color = if(change > 0)  Color.Green else if(change == 0.0) Color.Black else Color.Red)
                Text("$${String.format("%.2f", portfolioItem.marketValue*portfolioItem.sharesOwned)}", color = if(change> 0)  Color.Green else if(change == 0.0) Color.Black else Color.Red)
            }
        }
        else{
            Column(modifier = Modifier.weight(1f)) {
                Text("0")
                Text("$0.00")
                Text("$0.00")
                Text("$0.00")
                Text("$0.00")
            }
        }

//        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { onTradeClick() },
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("TRADE", color = Color.White, fontWeight = FontWeight.Bold)
        }

    }
}
fun fetchNewsArticles(ticker: String, context: Context, onSuccess: (List<NewsArticle>) -> Unit) {
    val queue: RequestQueue = Volley.newRequestQueue(context)
    val url = "${URL}news/$ticker"
    val jsonArrayRequest = JsonArrayRequest(
        Request.Method.GET, url, null,
        { response ->
            try {
                val newsArticles: MutableList<NewsArticle> = mutableListOf()
                for (i in 0 until response.length() ) {
                    val articleJson = response.getJSONObject(i)
                    if (articleJson.getString("image").isNotEmpty()) {
                        val datetime = articleJson.getLong("datetime")
                        val publishedDate = Instant.ofEpochSecond(datetime).atZone(ZoneId.systemDefault()).toLocalDateTime()
                        val newsArticle = NewsArticle(
                            title = articleJson.getString("headline"),
                            source = articleJson.getString("source"),
                            imageUrl = articleJson.getString("image"),
                            publishedDate = publishedDate,
                            description = articleJson.getString("summary"),
                            url = articleJson.getString("url")
                        )
                        newsArticles.add(newsArticle)
                        if (newsArticles.size == numNews) break
                    }
                }

                onSuccess(newsArticles)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        },
        { error ->
            error.printStackTrace()
        }
    )
    queue.add(jsonArrayRequest)
}
fun fetchQuote(ticker: String, context: Context, onSuccess: (FavoriteItem) -> Unit){
    val queue: RequestQueue = Volley.newRequestQueue(context)
    val url = "${URL}quote/$ticker"

    val jsonObjectRequest = JsonObjectRequest(
        Request.Method.GET, url, null,
         { response ->
            try {
                val favoriteItem = FavoriteItem(
                    ticker = ticker,
                    name = "", // Assume name is not provided in API and is set elsewhere or needs another call
                    current = response.getDouble("c"),
                    change = response.getDouble("d"),
                    changeP = response.getDouble("dp"),
                    prev = response.getDouble("pc"),
                    high =  response.getDouble("h"),
                    low =  response.getDouble("l"),
                    open =  response.getDouble("o")

                )

                onSuccess(favoriteItem)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        },
        { error ->
            error.printStackTrace()
        }
    )
    queue.add(jsonObjectRequest)
}
fun fetchCompany(ticker: String, context: Context, onSuccess: (About) -> Unit){
    val queue: RequestQueue = Volley.newRequestQueue(context)
    val url = "${URL}company/$ticker"

    val jsonObjectRequest = JsonObjectRequest(
        Request.Method.GET, url, null,
        { response ->
            try {
                val favoriteItem = About(
                    IPO = response.getString("ipo"),
                    website = response.getString("weburl"),
                    industry = response.getString("finnhubIndustry"),
                    name = response.getString("name"), // Assume name is not provided in API and is set elsewhere or needs another call
                )

                onSuccess(favoriteItem)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        },
        { error ->
            error.printStackTrace()
        }
    )
    queue.add(jsonObjectRequest)
}
fun fetchPeers(ticker: String, context: Context, onSuccess: (List<String>) -> Unit) {
    val queue: RequestQueue = Volley.newRequestQueue(context)
    val url = "${URL}peers/$ticker"

    val jsonArrayRequest = JsonArrayRequest(
        Request.Method.GET, url, null,
        { response ->
            try {
                // Extract the peers' tickers from the JSON array response and convert it to a List<String>
                val peers = List(response.length()) { index -> response.getString(index) }
                onSuccess(peers)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        },
        { error ->
            // Network error handling
            error.printStackTrace()
        }
    )
    queue.add(jsonArrayRequest)
}
fun fetchPortfolio(ticker: String, context: Context, walletBalance: MutableState<WalletBalance>, price: Double, onSuccess: (ResultPortfolio) -> Unit) {
    val queue: RequestQueue = Volley.newRequestQueue(context)
    val url = "${URL}portfolio/GET"

    val jsonArrayRequest = JsonArrayRequest(
        Request.Method.GET, url, null,
        { response ->
            try {
                var portfolioItem: ResultPortfolio? = null
                for (i in 0 until response.length()) {
                    val item = response.getJSONObject(i)
                    if (item.getString("ticker") == ticker) {
                        val sharesOwned = item.getInt("quantity")
                        val avgCostPerShare = item.getDouble("avgshare")
                        portfolioItem = ResultPortfolio(
                            ticker = ticker,
                            sharesOwned = sharesOwned,
                            avgCostPerShare = avgCostPerShare,
                            totalCost = sharesOwned * avgCostPerShare,
                            name = item.getString("name")
                        )
                        walletBalance.value.cashBalance -= sharesOwned * avgCostPerShare
                        walletBalance.value.netWorth += sharesOwned * price
                    }
                }

                // If the ticker was not found, return a ResultPortfolio with default values
                if (portfolioItem == null) {
                    portfolioItem = ResultPortfolio(ticker = ticker, sharesOwned = 0, avgCostPerShare = 0.0, totalCost = 0.0, name = "")
                }

                onSuccess(portfolioItem)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        },
        { error ->
            error.printStackTrace()
        }
    )

    queue.add(jsonArrayRequest)
}
fun fetchSocialSentiment(ticker: String, context: Context, onSuccess: (SocialSentiments) -> Unit,) {
    val queue: RequestQueue = Volley.newRequestQueue(context)
    val url = "${URL}insider/$ticker"

    val jsonObjectRequest = JsonObjectRequest(
        Request.Method.GET, url, null,
        { response ->
            try {
                val data = response.getJSONArray("data")
                var totalChange = 0.0
                var posChange = 0.0
                var negChange = 0.0
                var totalMSPR = 0.0
                var posMSPR = 0.0
                var negMSPR = 0.0

                for (i in 0 until data.length()) {
                    val item = data.getJSONObject(i)
                    val change = item.getDouble("change")
                    val mspr = item.getDouble("mspr")

                    totalChange += change
                    totalMSPR += mspr

                    if (change > 0) posChange += change
                    if (mspr > 0) posMSPR += mspr
                    if (change < 0) negChange += change
                    if (mspr < 0) negMSPR += mspr
                }

                val socialSentiments = SocialSentiments(
                    totalMSPR = totalMSPR,
                    posMSPR = posMSPR,
                    negMSPR = negMSPR,
                    totalChange = totalChange,
                    posChange = posChange,
                    negChange = negChange
                )

                onSuccess(socialSentiments)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        },
        { error ->
            error.printStackTrace()
        }
    )
    queue.add(jsonObjectRequest)
}
@Composable
fun NewsSection(newsItems: List<NewsArticle>, onArticleClick: (NewsArticle) -> Unit) {
    Column {
        newsItems.forEachIndexed { index, news ->
            if (index == 0) {
                FirstNewsItem(newsArticle = news, onArticleClick = { onArticleClick(news) })
            } else {
                NewsItem(newsArticle = news, onArticleClick = { onArticleClick(news) })
            }
        }
    }
}
@Composable
fun NewsItem(newsArticle: NewsArticle,  onArticleClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onArticleClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)

    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .fillMaxHeight(1f)
            ) {
                Row (
                    modifier = Modifier
                ){
                    Text(
                        text = newsArticle.source,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "   ${elapsedTimeSince(newsArticle.publishedDate)} ago",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Text(
                    text = newsArticle.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            val painter = rememberAsyncImagePainter(model = newsArticle.imageUrl)
            Image(
                painter = painter,
                contentDescription = "News article image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp) // You can adjust the size to fit your layout
                    .clip(RoundedCornerShape(4.dp)) // Rounded corners for the image
            )
        }
    }
}
@Composable
fun FirstNewsItem(newsArticle: NewsArticle,  onArticleClick: () -> Unit){
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onArticleClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .fillMaxHeight(1f)
            ) {
                val painter = rememberAsyncImagePainter(model = newsArticle.imageUrl)
                Card (
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ){
                    Image(
                        painter = painter,
                        contentDescription = "News article image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .align(Alignment.CenterHorizontally)
                    )
                }
                Row (
                    modifier = Modifier
                ){
                    Text(
                        text = newsArticle.source,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "   ${elapsedTimeSince(newsArticle.publishedDate)} ago",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Text(
                    text = newsArticle.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDialog(newsArticle: NewsArticle, onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.White, RoundedCornerShape(16.dp)),
        content = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = newsArticle.source,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = newsArticle.publishedDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Divider(color = Color.LightGray, thickness = 1.dp)
                Text(
                    text = newsArticle.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = newsArticle.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val context = LocalContext.current
                    IconButton(onClick = { openUrl(newsArticle.url, context) }) {
                        Icon(painter = painterResource(id = R.drawable.chrome),
                            contentDescription = "Open in Chrome",
                            tint = Color.Unspecified) // Replace with your actual drawable resource ID
                    }
                    IconButton(onClick = { openUrl("https://twitter.com/intent/tweet?text=${newsArticle.url}", context) }) {
                        Icon(painter = painterResource(id = R.drawable.twitter_x_icon),
                            contentDescription = "Share on Twitter",
                            tint = Color.Unspecified) // Replace with your actual drawable resource ID
                    }
                    IconButton(onClick = { openUrl("https://www.facebook.com/sharer/sharer.php?u=${newsArticle.url}", context) }) {
                        Icon(painter = painterResource(id = R.drawable.facebook),
                            contentDescription = "Share on Facebook",
                            tint = Color.Unspecified) // Replace with your actual drawable resource ID
                    }
                }
            }
        }
    )
}
fun openUrl(url: String, context: android.content.Context) {
    val customTabsIntent = CustomTabsIntent.Builder().build()
    customTabsIntent.launchUrl(context, android.net.Uri.parse(url))
}
fun elapsedTimeSince(publishedDate: LocalDateTime): String {
    val now = LocalDateTime.now()
    val seconds = ChronoUnit.SECONDS.between(publishedDate, now)
    return when {
        seconds < 60 -> "$seconds seconds"
        seconds < 3600 -> "${seconds / 60} minutes"
        else -> "${seconds / 3600} hours"
    }
}
@Composable
fun NewsScreen(newsArticles: List<NewsArticle>) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedArticle by remember { mutableStateOf<NewsArticle?>(null) }

    NewsSection(newsItems = newsArticles) { article ->
        selectedArticle = article
        showDialog = true
    }

    if (showDialog) {
        selectedArticle?.let { article ->
            NewsDialog(newsArticle = article, onDismissRequest = { showDialog = false })
        }
    }
}
@Composable
fun Header(fav: Boolean, searchKey: MutableState<String>, showSearchResult: MutableState<Boolean>, loading: MutableState<Boolean>, quote: MutableState<TickerInfo>, favorites: MutableState<List<FavoriteItem>>){
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var inFav  by remember {
        mutableStateOf(
            fav
        )
    }
    val context = LocalContext.current
    val queue = Volley.newRequestQueue(context)

    val deleteUrl = "${URL}watchlist/DELETE/${searchKey.value}"
    val addUrl = "${URL}watchlist/ADD"

    val addFavRequestBody = JSONObject().apply {
        put("ticker", searchKey.value)
        put("name", quote.value.name)
    }

    fun toggleFavorite() {
        if (inFav) {
            val deleteRequest = JsonObjectRequest(Request.Method.DELETE, deleteUrl, null,
                { response ->
                    Log.i("Favorite", "Delete successful, updating state to false")
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "${searchKey.value} is removed from favorites",
                            duration = SnackbarDuration.Short
                        )
                        inFav = false
                        favorites.value = favorites.value.filter { it.ticker != searchKey.value }
                    }
                },
                { error ->
                    Log.e("Favorite", "Failed to delete ${searchKey.value}")
                    error.printStackTrace()
                    coroutineScope.launch {
                        inFav = true  // Revert if failed
                    }
                }
            )
            queue.add(deleteRequest)
            Log.i("Favorite", "Sent delete request, optimistically setting false")
            inFav = false
        } else {
            val addRequest = JsonObjectRequest(Request.Method.POST, addUrl, addFavRequestBody,
                { response ->
                    Log.i("Favorite", "Add successful, updating state to true")
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "${searchKey.value} is added to favorites",
                            duration = SnackbarDuration.Short
                        )
                        inFav = true
                        val newfavorites = favorites.value.toMutableList().apply { add(FavoriteItem(ticker = searchKey.value, name =  quote.value.name)) }
                        favorites.value = newfavorites
                    }
                },
                { error ->
                    Log.e("Favorite", "Failed to add ${searchKey.value}")
                    error.printStackTrace()
                    coroutineScope.launch {
                        inFav = false  // Revert if failed
                    }
                }
            )
            queue.add(addRequest)
            Log.i("Favorite", "Sent add request, optimistically setting true")
            inFav = true
        }
    }


    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(1.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Arrow Back",
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        showSearchResult.value = false
                        loading.value = false
                    }
            )
            Text(
                text = searchKey.value,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = if (inFav) painterResource(id = R.drawable.full_star) else painterResource(id = R.drawable.star_border),
                contentDescription = if (inFav) "FavStar" else "NotFavStar",
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { toggleFavorite() }
            )
            Log.d("Composable", "Icon recomposed with inFav: $inFav")
        }
    }
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBarUI( searchKey: MutableState<String>, showSearchResult: MutableState<Boolean>, loading: MutableState<Boolean>) {
    var query = remember {
        mutableStateOf("")
    }
    var isSearchActive by remember { mutableStateOf(false) }
    var suggestions by remember { mutableStateOf(listOf<String>()) }
    val queue = Volley.newRequestQueue(LocalContext.current)
    var showDropdown by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(query.value) {
        if (query.value.isNotBlank()) {
            val url = "${URL}search/${query.value}"
            val request = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    val results = response.getJSONArray("result")
                    val filteredResults = (0 until results.length()).map { index ->
                        results.getJSONObject(index)
                    }.filterNot { it.getString("displaySymbol").contains(".") }
                        .map { it.getString("displaySymbol") + " | " + it.getString("description") }
                    suggestions = filteredResults
                    Log.i("Suggestion", "Suggestion Loaded$url")
                },
                { error ->
                    Log.e("Suggestion", "Failed to fetch suggestion")
                    error.printStackTrace()
                }
            )

            queue.add(request)
            showDropdown = true
        }
    }
    if (isSearchActive) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Arrow Back",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            isSearchActive = false
                            showDropdown = false
                            showSearchResult.value = false
                            loading.value = false
                        }
                )
                TextField(
                    value = query.value,
                    onValueChange = { newValue ->
                        query.value = newValue
                        showDropdown = newValue.isNotBlank()
                    },
                    placeholder = { Text("Search") },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                )
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Clear",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            query.value = ""
                            showDropdown = false // Hide the dropdown when clear icon is clicked
                            focusRequester.requestFocus()
                        }
                )
            }
            if (showDropdown){
                DropdownMenu(
                    expanded = showDropdown,
                    onDismissRequest = {
                        showDropdown = false
                        suggestions = emptyList()
                    }
                ) {
                    suggestions.forEach { suggestion ->
                        DropdownMenuItem(
                            text = {Text(text = suggestion)},
                            onClick = {
                                searchKey.value = suggestion.split(" | ").first()
                                query.value = suggestion.split(" | ").first()
                                showDropdown = false // Hide the dropdown when an item is clicked
                                suggestions = emptyList()
                                focusManager.clearFocus()
                                showSearchResult.value = true
                                loading.value = true
                            }
                        )
                    }
                }
            }

        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()

                .shadow(1.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Stocks",
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 18.sp
                )
            )
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                modifier = Modifier.padding(16.dp).clickable { isSearchActive = true }
            )
        }
    }
}
@Composable
fun DateBar(today: String){
    Column {
        Text(
            text = today,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            style = androidx.compose.ui.text.TextStyle(
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                fontSize = 18.sp
            )

        )
    }
}
@Composable
fun Wallet(netWorth: Double, cashBalance: Double){
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row {
            Text(
                text = "Net Worth",
                modifier = Modifier.weight(1f),
                style = androidx.compose.ui.text.TextStyle(
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                fontSize = 18.sp
            ))
            Text(
                text = "Cash Balance",
                modifier = Modifier,
                style = androidx.compose.ui.text.TextStyle(
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                fontSize = 18.sp
            ))
        }
        Row (modifier = Modifier){
            Text(
                text = "$"+ String.format("%.2f", netWorth+cashBalance),
                modifier = Modifier.weight(1f),
                style = androidx.compose.ui.text.TextStyle(
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                fontSize = 18.sp
            ))
            Text(
                text = "$" + String.format("%.2f", cashBalance),
                modifier = Modifier,
                style = androidx.compose.ui.text.TextStyle(
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                fontSize = 18.sp
            ))
        }
    }
    Divider(color = Color.Gray, thickness = 0.5.dp)
}
@Composable
fun FavoriteCard(leftTop: String, rightTop: String, leftBottom: String, rightBottom: String, up:Boolean=true, onclick: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White),
    ) {
        Row(){
            Column(modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 5.dp)
                .weight(1f)) {
                Row {
                    Text(
                        text = leftTop,
                        modifier = Modifier.weight(1f),
                        style = androidx.compose.ui.text.TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 18.sp
                        )
                    )
                    Text(
                        text = rightTop,
                        style = androidx.compose.ui.text.TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 18.sp
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Text(
                        text = leftBottom,
                        modifier = Modifier.weight(1f) ,
                        style = androidx.compose.ui.text.TextStyle(
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    )
                    Row(modifier = Modifier){
                        Icon( painter = if (up) painterResource(id = R.drawable.trending_up) else painterResource(id = R.drawable.trending_down) , contentDescription = "", tint = if (up) Color.Green else Color.Red)
                        Text(text = rightBottom, style = androidx.compose.ui.text.TextStyle(
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            color = if(up) Color.Green else Color.Red
                        ))
                    }
                }
            }
            Box(modifier = Modifier.align(Alignment.CenterVertically)){
                Icon(painter = painterResource(id = R.drawable.right_arrow), contentDescription = "arrowright", modifier = Modifier.clickable { onclick(leftTop) })
            }
        }
    }
    Divider(color = Color.Gray, thickness = 1.dp)
}
@Composable
fun Footer(context: Context) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { openUrl("https://finnhub.io/", context) },
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Powered by Finnhub", color = Color(128, 128, 128))
    }
}
@Preview(showBackground = true)
@Composable
fun HomePreview() {

    HW4ComposableTheme {
//        var socialSentiments = SocialSentiments(name ="Apple")
//        SocialSentimentSection(socialSentiments = socialSentiments)
//        SuccessDialog(message = successMessage, showDialog = showSuccessMessage)
        MyApp()
    }
}