package com.example.currencyconverter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import com.example.currencyconverter.Adapter.CountryAdapter
import com.example.currencyconverter.Api.ApiRequest
import com.jeevandeshmukh.glidetoastlib.GlideToast
import com.jeevandeshmukh.glidetoastlib.GlideToast.makeToast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.math.RoundingMode
import java.text.DecimalFormat


const val BASE_URL = "http://data.fixer.io/api/"

class MainActivity: AppCompatActivity() {

    lateinit var fromSpinner: AppCompatSpinner
    lateinit var toSpinner: AppCompatSpinner
    lateinit var fromCountryAdapter: CountryAdapter
    lateinit var toCountryAdapter: CountryAdapter
    lateinit var fromField: String
    lateinit var toField: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fromField = ""
        toField = ""
        val outValue = TypedValue()
        applicationContext!!.theme.resolveAttribute(
            android.R.attr.selectableItemBackground,
            outValue,
            true
        )
        signUpBtn.setBackgroundResource(outValue.resourceId)

        fromSpinner = findViewById(R.id.fromSpinner)
        toSpinner = findViewById(R.id.toSpinner)
        var names = resources.getStringArray(R.array.currency)
        fromCountryAdapter = CountryAdapter(names, this)
        fromSpinner.adapter = fromCountryAdapter

        toCountryAdapter = CountryAdapter(names, this)
        toSpinner.adapter = toCountryAdapter

        timeText.paintFlags = timeText.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        fromSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                fromField = names[p2]
                countryFrom.text = fromField
                toValue.text = ""
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        toSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                toField = names[p2]
                countryTo.text = toField
                toValue.text = ""
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }



        convertBtn.setOnClickListener {
            if (fromValue.text!!.isNotEmpty() && fromValue.text.toString() != "0") {
                convertCurrency()
            } else {
                makeToast(
                    this@MainActivity,
                    "Enter number to convert to",
                    GlideToast.LENGTHLONG,
                    GlideToast.INFOTOAST,
                    GlideToast.BOTTOM
                ).show()
            }
        }

    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun convertCurrency() {
        progressBar.visibility = View.VISIBLE
        loading()

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequest::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getCurrency().awaitResponse()
                if (response.isSuccessful) {
                    val data = response.body()!!

                    var  rates = data.getAsJsonObject("rates")
                    var time = data.get("timestamp")
                    var timestamp = time.toString()
                    val toRate = rates.get(toField)
                    val fromRate = rates.get(fromField)

                    withContext(Dispatchers.Main) {
                        countryFrom.text = fromField
                        countryTo.text = toField
                        var result = fromValue.text.toString().toDouble() * (toRate.toString().toDouble() / fromRate.toString().toDouble())
                        val df = DecimalFormat("#.######")
                        df.roundingMode = RoundingMode.CEILING
                        toValue.text = df.format(result)

                        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                        val date = java.util.Date(time.toString().toInt().toLong() * 1000).toString()
//                        sdf.format(date)
//                        var d = date.toString()
                        var myDate = date.substring(11, 16)
                        timeText.text = "Mid-market exchange rate at $myDate UTC"
                        infoIcon.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        convertBtn.isEnabled = true
                        fromValue.isEnabled = true
                        fromSpinner.isClickable = true
                        toSpinner.isClickable = true
                    }
                }
            } catch (e: Exception) {
                e.message
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    makeToast(
                        this@MainActivity,
                        "Something went wrong",
                        GlideToast.LENGTHLONG,
                        GlideToast.FAILTOAST,
                        GlideToast.BOTTOM
                    ).show()
                    progressBar.visibility = View.GONE
                    convertBtn.isEnabled = true
                    fromValue.isEnabled = true
                    fromSpinner.isClickable = true
                    toSpinner.isClickable = true
                }
            }
        }
    }

    private fun loading() {
        convertBtn.isEnabled = false
        fromValue.isEnabled = false
        fromSpinner.isClickable = false
        toSpinner.isClickable = false
    }

}