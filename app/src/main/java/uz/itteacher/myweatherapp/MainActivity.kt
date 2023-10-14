package uz.itteacher.myweatherapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import coil.load
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import uz.itteacher.myweatherapp.adapter.ForecastAdapter
import uz.itteacher.myweatherapp.adapter.TodayAdapter
import uz.itteacher.myweatherapp.databinding.ActivityMainBinding
import uz.itteacher.myweatherapp.model.Forecast
import uz.itteacher.myweatherapp.model.today
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.math.roundToInt

const val TAG = "TAG"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var forecastAdapter: ForecastAdapter
    private lateinit var todayAdapter: TodayAdapter
    lateinit var obj: JSONObject
    private lateinit var todayList: MutableList<today>
    private lateinit var forecasts: MutableList<Forecast>
    private lateinit var city: String

    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        city = intent.getStringExtra("city").toString()
        if (city == "null") {
            city = "Tashkent"
        }
        url = "http://api.weatherapi.com/v1/forecast.json?key=71babad502344ef29b712606231210&q=$city&days=7&aqi=yes&alerts=yes"

        forecasts = mutableListOf()
        todayList = mutableListOf()
        todayAdapter = TodayAdapter(todayList)

        forecastAdapter = ForecastAdapter(forecasts, object : ForecastAdapter.DayInterface {
            override fun dayOnClick(day: String) {
                hourInfo(obj, day)
            }

        })

        binding.settings.setOnClickListener {
            intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }


        binding.rvDay.adapter = forecastAdapter
        binding.rvHour.adapter = todayAdapter

        val requestQueue = Volley.newRequestQueue(this)

        val request = JsonObjectRequest(url, object : Response.Listener<JSONObject> {
            override fun onResponse(response: JSONObject?) {
                mainInfo(response!!)

                val forecast = response.getJSONObject("forecast")

                val forecastday = forecast.getJSONArray("forecastday")
                obj = forecastday.getJSONObject(0)

                for (i in 0 until forecastday.length()) {
                    obj = forecastday.getJSONObject(i)
                    dayInfo(obj)
                    hourInfo(obj)
                }

            }

        }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError?) {
                Log.d(TAG, "onErrorResponse: $error")
            }

        })
        requestQueue.add(request)

    }

    @SuppressLint("NotifyDataSetChanged", "SimpleDateFormat")
    private fun hourInfo(obj: JSONObject) {
        val hours = obj.getJSONArray("hour")
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("HH")
        val currenth = formatter.format(time)

        for (i in 0 until hours.length()) {
            val hobj = hours.getJSONObject(i)
            val h = hobj.getString("time").substring(11, 13)
            val time = hobj.getString("time").substring(11)
            val temp_c = hobj.getString("temp_c").toDouble().roundToInt().toString() + "℃"
            val condi = hobj.getJSONObject("condition")
            val uri = "https:" + condi.getString("icon")
            val icon = uri
            var day = hobj.getString("time").substring(0, 9)
            if (todayList.size in 1..23) {
                todayList.add(today(time, temp_c, icon))
            }
            if (h == currenth && todayList.size < 24) {
                todayList.add(today(time, temp_c, icon))
            }
            todayAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun hourInfo(obj: JSONObject, day: String?) {
        val hours = obj.getJSONArray("hour")
        for (i in 0 until hours.length()) {
            val hobj = hours.getJSONObject(i)
            val time = hobj.getString("time").substring(11)
            val temp_c = hobj.getString("temp_c").toDouble().roundToInt().toString() + "℃"
            val condi = hobj.getJSONObject("condition")
            val uri = "https:" + condi.getString("icon")
            val icon = uri
            val d = hobj.getString("time").substring(0, 9)
            if (d == day) {
                todayList.add(today(time, temp_c, icon))
                todayAdapter.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun dayInfo(obj: JSONObject) {
        val date = obj.getString("date")
        val day = obj.getJSONObject("day")
        val maxtemp_c = day.getString("maxtemp_c").toDouble().roundToInt().toString() + "℃"
        val mintemp_c = day.getString("mintemp_c").toDouble().roundToInt().toString() + "℃"
        val condition = day.getJSONObject("condition")
        val con_text = condition.getString("text")
        val day_icon = "https:" + condition.getString("icon")
        forecasts.add(Forecast(date, maxtemp_c, mintemp_c, con_text, day_icon))

        forecastAdapter.notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    private fun mainInfo(response: JSONObject) {
        val location = response.getJSONObject("location")
        binding.region.text = location.getString("region")
        val current = response.getJSONObject("current")
        binding.temp.text = current.getString("temp_c").toDouble().roundToInt().toString() + "℃"
        val condition = current.getJSONObject("condition")
        binding.conText.text = condition.getString("text")
        val url = "https:" + condition.getString("icon")
        binding.icon.load(url)
        binding.feelsLike.text = current.getString("feelslike_c") + "℃"
        binding.humidity.text = current.getString("humidity") + "%"
        binding.windSpeed.text = current.getString("wind_kph") + " km/h"
    }
}