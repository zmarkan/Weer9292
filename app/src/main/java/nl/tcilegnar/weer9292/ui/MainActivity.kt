package nl.tcilegnar.weer9292.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import nl.tcilegnar.weer9292.R
import nl.tcilegnar.weer9292.dagger.factories.InitializerFragmentFactory
import nl.tcilegnar.weer9292.dagger.factories.ViewModelFactory
import nl.tcilegnar.weer9292.dagger.factories.addInitializer
import nl.tcilegnar.weer9292.model.TemperatureUnit
import nl.tcilegnar.weer9292.network.model.response.Coordinates
import nl.tcilegnar.weer9292.storage.TemperaturePrefs
import nl.tcilegnar.weer9292.ui.forecast.ForecastFragment
import nl.tcilegnar.weer9292.ui.forecast.ForecastViewModel
import nl.tcilegnar.weer9292.ui.home.HomeFragment
import nl.tcilegnar.weer9292.ui.home.HomeViewModel
import nl.tcilegnar.weer9292.util.extensions.getViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity(), OnQueryTextListener {
    companion object {
        private val defaultCoordinates = Coordinates.get9292HQ()
    }

    @Inject
    lateinit var homeVMF: ViewModelFactory<HomeViewModel>

    @Inject
    lateinit var forecastVMF: ViewModelFactory<ForecastViewModel>

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var temperaturePrefs: TemperaturePrefs
    private lateinit var searchView: MenuItem

    private var currentTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        supportFragmentManager.fragmentFactory = InitializerFragmentFactory().apply {
            addInitializer { HomeFragment(homeVMF) }
            addInitializer { ForecastFragment(forecastVMF) }
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        homeViewModel = getViewModel(homeVMF, HomeViewModel::class.java)
        temperaturePrefs = TemperaturePrefs(this)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_forecast,
                R.id.navigation_radar
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        homeViewModel.currentWeather.observe(this, Observer { currentWeather ->
            currentWeather?.let {
                currentTitle = currentWeather.location.getCityWithCountryCode().toString()
                setActionBarTitle(currentTitle)
            }
        })
        homeViewModel.isLoading.observe(this, Observer {
            if (it) {
                showLoading()
            } else {
                setActionBarTitle(currentTitle)
            }
        })
        homeViewModel.errorMessage.observe(this, Observer {
            if (it.isNotBlank()) {
                Snackbar.make(findViewById(android.R.id.content), it, Snackbar.LENGTH_LONG).show()
            }
        })

        // Start app with current weather from default location
        if (savedInstanceState == null) {
            homeViewModel.getCurrentWeather(defaultCoordinates)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings, menu)

        temperaturePrefs.getTemperatureUnitLiveDataString().observe(this, Observer {
            val newIcon = TemperatureUnit.valueOf(it)
            menu.findItem(R.id.action_settings).icon = newIcon.getMenuIcon(this)
        })

        searchView = menu.findItem(R.id.action_search)
        (searchView.actionView as SearchView).setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        R.id.action_settings -> {
            temperaturePrefs.toggleTemperatureUnit()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    fun setActionBarTitle(title: String?) {
        supportActionBar!!.title = title
    }

    private fun showLoading() {
        setActionBarTitle("Loading...")
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query?.isNotBlank() == true) {
            searchView.collapseActionView()
            showLoading()
            homeViewModel.getCurrentWeather(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}
