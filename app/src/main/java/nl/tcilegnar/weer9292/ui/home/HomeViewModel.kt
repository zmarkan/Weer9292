package nl.tcilegnar.weer9292.ui.home

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import nl.tcilegnar.weer9292.model.WeatherDetails
import nl.tcilegnar.weer9292.network.model.response.Coordinates
import nl.tcilegnar.weer9292.repo.CurrentWeatherRepo

private val defaultCoordinates = Coordinates.get9292HQ()

class HomeViewModel(
    currentWeatherRepo: CurrentWeatherRepo = CurrentWeatherRepo.getInstance()
) : ViewModel() {
    val currentWeatherDetails = Transformations.map(currentWeatherRepo.currentWeatherResponse) {
        it?.let { WeatherDetails.from(it) }
    }
    val currentWeather = Transformations.map(currentWeatherDetails) {
        it?.basicWeather
    }
    val currentCoordinates = currentWeatherRepo.currentCoordinates

    init {
        currentWeatherRepo.getCurrentWeather(defaultCoordinates)
    }
}
