package nl.tcilegnar.weer9292.ui.forecast

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import nl.tcilegnar.weer9292.repo.CurrentWeatherRepo
import nl.tcilegnar.weer9292.repo.ForecastRepository

class ForecastViewModel(
    forecastRepo: ForecastRepository = ForecastRepository.getInstance(),
    currentWeatherRepo: CurrentWeatherRepo = CurrentWeatherRepo.getInstance()
) : ViewModel() {
    val forecast = Transformations.switchMap(currentWeatherRepo.response) {
        it?.let {
            forecastRepo.getDailyForecast(it.basicWeather.location.coordinates)
        }
        forecastRepo.response
    }
}
