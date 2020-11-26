package nl.tcilegnar.weer9292.model

import nl.tcilegnar.weer9292.network.model.response.*
import org.joda.time.DateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

private const val CITY = "City"
private const val COUNTRY_CODE = "NL"
private val COORDINATES = Coordinates(12.0, 13.0)
private val WIND = Wind(6.0, 360, 7.0)
private const val PRESSURE = 1000
private const val HUMIDITY = 50
private const val CITY_ID = 123L

class WeatherTest {

    @Test
    fun weatherFromCurrentWeatherResponse() {
        val response = CurrentWeatherResponse(
            0,
            WeatherProperties(1.0, 2.0, 3.0, 4.0, PRESSURE, HUMIDITY),
            listOf(WeatherType(803, "Clouds", "broken clouds")),
            WIND,
            COORDINATES,
            Sys(12345, COUNTRY_CODE, 1590895545, 1590954547),
            CITY,
            CITY_ID
        )

        val weather = Weather.from(response)

        val expectedWeather = Weather(
            DateTime(0),
            Location(CITY_ID, CITY, COUNTRY_CODE, COORDINATES),
            WeatherCondition.CLOUDS,
            Temperatures(1, 2, 3, 4),
            WIND
        )
        assertEquals(expectedWeather, weather)
    }

    @Test
    fun weatherFromDailyWeatherResponseAndLocation() {
        val response = DailyWeatherResponse(
            0,
            1590895545,
            1590954547,
            TemperatureProperties(1.0, 2.0),
            PRESSURE,
            HUMIDITY,
            listOf(WeatherType(803, "Clouds", "broken clouds")),
            WIND.speed,
            WIND.degrees
        )

        val location = Location(CITY_ID, CITY, COUNTRY_CODE, COORDINATES)

        val weather = Weather.from(response, location)

        val expectedWeather = Weather(
            DateTime(0),
            location,
            WeatherCondition.CLOUDS,
            Temperatures(null, null, 1, 2),
            Wind(WIND.speed, WIND.degrees, null)
        )
        assertEquals(expectedWeather, weather)
    }
}
