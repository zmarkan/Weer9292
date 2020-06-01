package nl.tcilegnar.weer9292.ui.forecast

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_forecast.*
import nl.tcilegnar.weer9292.R

class ForecastFragment : Fragment() {
    private lateinit var forecastViewModel: ForecastViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forecastViewModel = ViewModelProvider(this).get(ForecastViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_forecast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        forecastViewModel.forecast.observe(viewLifecycleOwner, Observer { dailyForecast ->
            dailyForecast?.let {
                text_forecast.text = it.location.cityName
                Log.d("TEST", "forecast for ${it.location}")
                it.weathers.forEach { weather ->
                    Log.d("TEST", "forecast: $weather")
                }
            }
        })
    }
}
