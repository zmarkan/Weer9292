package nl.tcilegnar.weer9292.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_home.*
import nl.tcilegnar.weer9292.R
import nl.tcilegnar.weer9292.model.Temperatures
import nl.tcilegnar.weer9292.model.Weather
import nl.tcilegnar.weer9292.storage.TemperaturePrefs
import nl.tcilegnar.weer9292.ui.MainActivity
import nl.tcilegnar.weer9292.ui.home.HomeFragmentDirections.Companion.actionHomeFragmentToWeatherDetailsFragment

class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setActionbarTitle("Loading...")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.currentWeatherDetails.observe(viewLifecycleOwner, Observer { currentWeatherDetails ->
            currentWeatherDetails?.let {
                Log.d("TEST", "currentWeatherDetails: $currentWeatherDetails")
                home_weather_details.setOnClickListener {
                    findNavController().navigate(actionHomeFragmentToWeatherDetailsFragment(currentWeatherDetails))
                }
            }
        })
        homeViewModel.currentWeather.observe(viewLifecycleOwner, Observer { currentWeather ->
            currentWeather?.let {
                setData(currentWeather)
            }
        })
    }

    private fun setData(weather: Weather) {
        setActionbarTitle(weather.location.cityWithCountryCode.toString())
        home_date.text = weather.getDateTimeFormatted()
        weather_icon.setImageResource(weather.weatherCondition.getIconRes())
        setTemperatures(weather.temperatures)
    }

    private fun setTemperatures(temperatures: Temperatures) {
        val context = requireContext()
        temperatures.currentTemperature?.let {
            home_temp_now.text = TemperaturePrefs(context).getTemperatureText(it)
        }
        home_temp_max.setTemperature(context, temperatures.temperatureMax)
        home_temp_min.setTemperature(context, temperatures.temperatureMin)
    }

    private fun setActionbarTitle(title: String) {
        (context as MainActivity).setActionBarTitle(title)
    }
}
