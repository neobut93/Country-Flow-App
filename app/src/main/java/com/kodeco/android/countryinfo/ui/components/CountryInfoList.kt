package com.kodeco.android.countryinfo.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kodeco.android.countryinfo.R
import com.kodeco.android.countryinfo.flow.Flows
import com.kodeco.android.countryinfo.models.Country
import com.kodeco.android.countryinfo.sample.sampleCountries

@Composable
fun CountryInfoList(
    countries: List<Country>,
    onRefreshClick: () -> Unit,
) {
    var selectedCountry: Country? by remember { mutableStateOf(null) }
    val tapCount by Flows.tapFlow.collectAsState()
    val backCount by Flows.backFlow.collectAsState()
    val refreshCount by Flows.refreshFlow.collectAsState()
    val combineNavigationCount by Flows.combineNavigationFlow.collectAsState(initial = 0)
    val context = LocalContext.current

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Taps: $tapCount", fontSize = 17.sp)
            Button(onClick = {
                // tracking refresh event here
                Flows.tapRefresh()
                onRefreshClick()
                Flows.triggerRefreshSharedFlow("The screen is refreshing currently")
            }) {
                Text(text = "Refresh: $refreshCount times")
            }
            Text(text = "Back: $backCount", fontSize = 17.sp)
        }
        // added extra row to display combine flow
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Combined navigation: $combineNavigationCount", fontSize = 17.sp)
        }

        selectedCountry?.let { country ->
            CountryDetailsScreen(country) { selectedCountry = null }
        } ?: run {
            LazyColumn {
                items(countries) { country ->
                    CountryInfoRow(country) {
                        selectedCountry = country
                        Flows.triggerCountrySharedFlow(country.commonName)
                        Flows.tap()
                    }
                }
            }
        }
    }

    LaunchedEffect(true) {
        Flows.countrySharedFlow.collect { message ->
            Toast.makeText(context, "The $message country row was tapped", Toast.LENGTH_SHORT)
                .show()
        }
    }

    LaunchedEffect(true) {
        Flows.refreshSharedFlow.collect { message ->
            Toast.makeText(
                context,
                message, Toast.LENGTH_SHORT
            )
                .show()
        }
    }
}

@Preview
@Composable
fun CountryInfoListPreview() {
    CountryInfoList(
        countries = sampleCountries,
        onRefreshClick = {},
    )
}
