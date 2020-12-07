package com.example.currencyconverter.Api

import com.google.gson.annotations.SerializedName

class CurrencyJson {

    @SerializedName("success")
    var success: Boolean = false
        get() = field

        set(value) { field = value  }

    @SerializedName("timestamp")
    var timestamp: Int = 0
        get() = field

        set(value) { field = value  }

    @SerializedName("base")
    var base: String = "EUR"
        get() = field

        set(value) { field = value  }

    @SerializedName("date")
    var date: String = ""
        get() = field

        set(value) { field = value  }

    @SerializedName("rates")
    var rates: Rates? = null
        get() = field

        set(value) { field = value  }
}