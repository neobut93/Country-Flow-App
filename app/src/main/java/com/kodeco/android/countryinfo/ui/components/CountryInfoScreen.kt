package com.kodeco.android.countryinfo.ui.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.kodeco.android.countryinfo.models.Country
import com.kodeco.android.countryinfo.network.CountryService
import com.kodeco.android.countryinfo.sample.sampleCountries
import retrofit2.Response

sealed class CountryInfoState {
    object Loading : CountryInfoState()
    data class Success(val countries: List<Country>) : CountryInfoState()
    data class Error(val error: Throwable) : CountryInfoState()
}

@Composable
fun CountryInfoScreen(
    service: CountryService,
) {
    var state: CountryInfoState by remember { mutableStateOf(CountryInfoState.Loading) }

    Surface {
        when(val curState = state) {
            is CountryInfoState.Loading -> Loading()
            is CountryInfoState.Success -> CountryInfoList(curState.countries) {
                state = CountryInfoState.Loading
            }
            is CountryInfoState.Error -> CountryErrorScreen(curState.error) {
                state = CountryInfoState.Loading
            }
        }
    }

    if (state == CountryInfoState.Loading) {
        LaunchedEffect(key1 = "fetch-countries") {
            // TODO: Move this to a private method
            //  and have the method return a Flow<CountryInfoState>
            //  NOTE: This method can utilize the flow { } builder.
            //  Don't forget you can also remove the try/catch and catch directly from the flow!
            state = try {
                val countriesResponse = service.getAllCountries()

                if (countriesResponse.isSuccessful) {
                    CountryInfoState.Success(countriesResponse.body()!!)
                } else {
                    CountryInfoState.Error(Throwable("Request failed: ${countriesResponse.message()}"))
                }
            } catch (exception: Exception) {
                CountryInfoState.Error(exception)
            }
        }
    }
}

@Preview
@Composable
fun CountryInfoScreenPreview() {
    CountryInfoScreen(
        service = object : CountryService {
            override suspend fun getAllCountries(): Response<List<Country>> =
                Response.success(sampleCountries)
        },
    )
}
