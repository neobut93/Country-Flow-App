package com.kodeco.android.countryinfo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kodeco.android.countryinfo.flow.Flows
import com.kodeco.android.countryinfo.models.Country
import com.kodeco.android.countryinfo.sample.sampleCountries

@Composable
fun CountryInfoList(
    countries: List<Country>,
    onRefreshClick: () -> Unit, // TODO: Utilize this onRefreshClick
) {
    var selectedCountry: Country? by remember { mutableStateOf(null) }
    val tapCount = Flows.tapFlow.collectAsState(initial = 0)
    val backCount = Flows.backFlow.collectAsState(initial = 0)

    Column(
    ) {
        // TODO: Implement the Row composable here that contains the
        //  the tap/back flow data and the refresh button.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Taps: ${tapCount.value}", fontSize = 17.sp)
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Refresh")
            }
            Text(text = "Back: ${backCount.value}", fontSize = 17.sp)
        }

        selectedCountry?.let { country ->
            CountryDetailsScreen(country) { selectedCountry = null }
        } ?: run {
            LazyColumn {
                items(countries) { country ->
                    CountryInfoRow(country) {
                        selectedCountry = country
                        Flows.tap()
                    }
                }
            }
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
