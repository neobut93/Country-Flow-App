package com.kodeco.android.countryinfo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.kodeco.android.countryinfo.models.Country
import com.kodeco.android.countryinfo.sample.sampleCountries

@Composable
fun CountryInfoList(
    countries: List<Country>,
    onRefreshClick: () -> Unit, // TODO: Utilize this onRefreshClick
) {
    var selectedCountry: Country? by remember { mutableStateOf(null) }

    Column {
        // TODO: Implement the Row composable here that contains the
        //  the tap/back flow data and the refresh button.

        selectedCountry?.let { country ->
            CountryDetailsScreen(country) { selectedCountry = null }
        } ?: run {
            LazyColumn {
                items(countries) { country ->
                    CountryInfoRow(country) {
                        selectedCountry = country
                        // TODO: Call into Flows.tap() here
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
