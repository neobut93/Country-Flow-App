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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
t
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
            getCountryInfoFlow(service)
                .handleErrors()
                .collect { newState ->
                    state = newState
                }
        }
    }
}

@Preview
@Composable
fun CountryInfoScreenPreview() {
    CountryInfoScreen(
        service = object : CountryService {
            override suspend fun getAllCountries(): List<Country> =
                sampleCountries
        },
    )
}

fun getCountryInfoFlow(service: CountryService): Flow<CountryInfoState> = flow {
    val countriesResponse = service.getAllCountries()
    delay(3000)
    emit(
        CountryInfoState.Success(countriesResponse)
    )
}

fun <T> Flow<T>.handleErrors(): Flow<T> {
    return catch { e -> CountryInfoState.Error(e) }
}
