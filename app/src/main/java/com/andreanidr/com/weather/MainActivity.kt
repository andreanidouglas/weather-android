package com.andreanidr.com.weather


import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.andreanidr.com.weather.Controller.WeatherController
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.util.concurrent.atomic.AtomicBoolean


class MainActivity : AppCompatActivity(), SearchCityDialogFragment.CityDialogListener {

    private val url = "https://api.openweathermap.org/data/2.5/weather?q=London&appid=a8c8aefc78b24c6903d228b837ad1c7d&units=metric"
    private var citiesWeather = WeatherController()


    lateinit var tempText: TextView
    lateinit var cityText: TextView
    lateinit var city: String

    lateinit var queue: RequestQueue
    private val TAG = "Weather1.0"

    private var currentCity: String = "London"

    @Volatile
    private var initialized: AtomicBoolean = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_layout)
        setSupportActionBar(findViewById(R.id.main_toolbar))
        tempText = findViewById(R.id.Temp)
        cityText = findViewById(R.id.city_name)
        queue = Volley.newRequestQueue(this)


    }

    override fun onResume() {
        super.onResume()
        queue = Volley.newRequestQueue(this)
        updateWeather()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                return true
            }
            R.id.search -> {
                showSearch()
                return true
            }
            R.id.license -> {
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun addCity(city: String) {
        citiesWeather.addCity(city)
    }

    private fun showSearch() {
        val fm = supportFragmentManager
        val search = SearchCityDialogFragment()
        search.show(supportFragmentManager, "")

    }

    private fun updateWeather() {
        val req = citiesWeather.requestUpdateCityWeather(currentCity)
        this.queue.add(req)
        this.queue.addRequestFinishedListener<JSONObject> { _ ->
            run {
                var cw = citiesWeather.getCityByName(currentCity)
                tempText.text = cw?.temperature.toString()
                cityText.text = cw?.city
            }
        }

    }

    private fun setStatusValue(status: String) {
        val statusText = findViewById<TextView>(R.id.status_value)
        statusText.text = " %s".format(status.capitalize())
    }

    private fun defineIcon(state: String) {
        val weatherId = when (state) {
            "Rain" -> R.drawable._14_rain_2
            "Thunderstorm" -> R.drawable._02_storm_2
            "Drizzle" -> R.drawable._15_rain_1
            "Snow" -> R.drawable._11_snowing_3
            "Clear" -> R.drawable._06_sun
            "Clouds" -> R.drawable._09_cloudy_day_1
            else -> R.drawable._08_foggy_day
        }

        val weatherDrawable = getDrawable(weatherId)

        val weather = findViewById<ImageView>(R.id.forecast_img)
        weather.setImageDrawable(weatherDrawable)
    }

    override fun onDialogPositiveClick(dialog: SearchCityDialogFragment) {
        Log.d(TAG, "onDialogPositiveClick: %s".format(dialog.getCityName()))
    }

    override fun onDialogNegativeClick(dialog: SearchCityDialogFragment) {
        dialog.dismiss()
    }
}

class SearchCityDialogFragment : DialogFragment() {


    private lateinit var listener: CityDialogListener
    private lateinit var editCity: EditText

    interface CityDialogListener {


        fun onDialogPositiveClick(dialog: SearchCityDialogFragment)
        fun onDialogNegativeClick(dialog: SearchCityDialogFragment)
    }


    fun getCityName(): String {
        return editCity.text.toString()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as CityDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(("$context must implement CityDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val v = inflater.inflate(R.layout.activity_search_dialog, null)
            builder.setView(v)
                    .setPositiveButton(R.string.ok,
                            DialogInterface.OnClickListener { dialog, id ->
                                editCity = v.findViewById(R.id.search_city)
                                listener.onDialogPositiveClick(this)
                            })
                    .setNegativeButton(R.string.cancel,
                            DialogInterface.OnClickListener { dialog, id ->
                                listener.onDialogNegativeClick(this)
                            })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


}