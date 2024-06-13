package com.example.wapp

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    val CITY: String = "Poznan,pl"
    val API: String = "1181410caf3294777ac65c8f736f5faf"







    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        weatherTask().execute()
    }
    inner class weatherTask(): AsyncTask<String, Void, String>()
    {
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errortext).visibility = View.GONE

        }

        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try {
                //https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API
                //https://api.openweathermap.org/data/3.0/onecall?lat=$lat&lon=$lon&exclude=$part&appid=$API
                //https://api.openweathermap.org/data/2.5/weather?lat=33.44&lon=-94.04&appid=$API
                //https://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&appid=$API")
                    .readText(Charsets.UTF_8)
            }
            catch (e: Exception)
            {
                response = "{\n" +
                        "  \"coord\": {\n" +
                        "    \"lon\": 10.99,\n" +
                        "    \"lat\": 44.34\n" +
                        "  },\n" +
                        "  \"weather\": [\n" +
                        "    {\n" +
                        "      \"id\": 501,\n" +
                        "      \"main\": \"Rain\",\n" +
                        "      \"description\": \"moderate rain\",\n" +
                        "      \"icon\": \"10d\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"base\": \"stations\",\n" +
                        "  \"main\": {\n" +
                        "    \"temp\": 298.48,\n" +
                        "    \"feels_like\": 298.74,\n" +
                        "    \"temp_min\": 297.56,\n" +
                        "    \"temp_max\": 300.05,\n" +
                        "    \"pressure\": 1015,\n" +
                        "    \"humidity\": 64,\n" +
                        "    \"sea_level\": 1015,\n" +
                        "    \"grnd_level\": 933\n" +
                        "  },\n" +
                        "  \"visibility\": 10000,\n" +
                        "  \"wind\": {\n" +
                        "    \"speed\": 0.62,\n" +
                        "    \"deg\": 349,\n" +
                        "    \"gust\": 1.18\n" +
                        "  },\n" +
                        "  \"rain\": {\n" +
                        "    \"1h\": 3.16\n" +
                        "  },\n" +
                        "  \"clouds\": {\n" +
                        "    \"all\": 100\n" +
                        "  },\n" +
                        "  \"dt\": 1661870592,\n" +
                        "  \"sys\": {\n" +
                        "    \"type\": 2,\n" +
                        "    \"id\": 2075663,\n" +
                        "    \"country\": \"IT\",\n" +
                        "    \"sunrise\": 1661834187,\n" +
                        "    \"sunset\": 1661882248\n" +
                        "  },\n" +
                        "  \"timezone\": 7200,\n" +
                        "  \"id\": 3163858,\n" +
                        "  \"name\": \"Zocca\",\n" +
                        "  \"cod\": 200\n" +
                        "}   "
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt:Long = jsonObj.getLong("dt")
                val updatedAtText = "Updated at: "+SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.ENGLISH).format(Date(updatedAt*1000))
                val temp = main.getString("temp")+" K"
                val tempMin = "Min Temp "+main.getString("temp_min")+" K"
                val tempMax = "Max Temp "+main.getString("temp_max")+" K"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windSpeed = wind.getLong("speed")
                val weatherDescription = weather.getString("description")
                val address = jsonObj.getString("name")+", "+sys.getString("country")


                findViewById<TextView>(R.id.adress).text = address
                findViewById<TextView>(R.id.updated_at).text = updatedAtText
                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.temp_max).text = tempMax
                findViewById<TextView>(R.id.temp_min).text = tempMin
                findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                findViewById<TextView>(R.id.wind).text = windSpeed.toString()
                findViewById<TextView>(R.id.pressure).text = pressure
                findViewById<TextView>(R.id.humidity).text = humidity


                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility =  View.VISIBLE



            }
            catch (e: Exception){
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errortext).visibility = View.VISIBLE
            }
        }
    }
}