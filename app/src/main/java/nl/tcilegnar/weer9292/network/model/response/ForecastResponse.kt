@file:Suppress("SpellCheckingInspection")

package nl.tcilegnar.weer9292.network.model.response

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("list")
    val weatherResponses: List<WeatherResponse>,
    @SerializedName("city")
    val location: Location
)
