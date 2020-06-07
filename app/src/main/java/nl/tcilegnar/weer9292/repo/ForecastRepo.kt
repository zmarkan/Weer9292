package nl.tcilegnar.weer9292.repo

import nl.tcilegnar.weer9292.model.DailyForecast
import nl.tcilegnar.weer9292.network.WeatherServices
import nl.tcilegnar.weer9292.network.model.response.Coordinates
import nl.tcilegnar.weer9292.network.model.response.DailyForecastResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForecastRepo @Inject constructor(
    private val weatherService: WeatherServices
) : ApiCallRepo<DailyForecastResponse, DailyForecast>() {

    fun getDailyForecast(coordinates: Coordinates) {
        startApiCall({
            weatherService.getDailyForecast(lat = coordinates.lat, lon = coordinates.lon)
        }, processNetworkResponse = {
            DailyForecast.from(it)
        }, handleError = {
            "Unable to retrieve forecast: something went wrong."
        })
    }
}
