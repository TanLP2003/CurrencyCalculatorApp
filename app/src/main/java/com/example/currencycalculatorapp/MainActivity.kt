package com.example.currencycalculatorapp

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var etOne: EditText? = null
    private var etTwo: EditText? = null

    private var spOne: Spinner? = null
    private var spTwo: Spinner? = null

    private var exchangeRates = mapOf(
        "United States - Dollar" to 1.0,
        "Vietnam - Dong" to 25335.0,
        "Europe - Euro" to 0.92,
        "Japan - JPY" to 153.36
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etOne = findViewById(R.id.edtAmount1)
        etTwo = findViewById(R.id.edtAmount2)
        spOne = findViewById(R.id.spinnerCurrency1)
        spTwo = findViewById(R.id.spinnerCurrency2)

        var currencies = exchangeRates.keys.toList()
        var adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spOne?.adapter = adapter
        spTwo?.adapter = adapter

        spOne?.setSelection(currencies.indexOf("United States - Dollar"))
        spTwo?.setSelection(currencies.indexOf("Vietnam - Dong"))

        calcConcurrency(true)

        etOne?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(etOne?.hasFocus() == true) {
                    calcConcurrency(true)
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        etOne?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                etOne?.setTypeface(etOne?.typeface, Typeface.BOLD)
            } else {
                Log.i("etOne","not focus on")
                etOne?.typeface = Typeface.DEFAULT
            }
        }

        etTwo?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(etTwo?.hasFocus() == true) {
                    calcConcurrency(false)
                }
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        etTwo?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                etTwo?.setTypeface(etTwo?.typeface, Typeface.BOLD)
            } else {
                etTwo?.typeface = Typeface.DEFAULT
            }
        }

        spOne?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(etOne?.hasFocus() == true) {
                    calcConcurrency(true)
                } else {
                    calcConcurrency(false)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        spTwo?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(etOne?.hasFocus() == true) {
                    calcConcurrency(true)
                } else {
                    calcConcurrency(false)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }


    private fun calcConcurrency(isFromFirst: Boolean) {
        val currencyOne = spOne?.selectedItem.toString()
        val currencyTwo = spTwo?.selectedItem.toString()

        try {
            if(isFromFirst) {
                var amountOne = etOne?.text.toString().toDoubleOrNull() ?: 0.0
                var amountOneUsd = amountOne / exchangeRates[currencyOne]!!
                var result = amountOneUsd * exchangeRates[currencyTwo]!!

                etTwo?.setText(String.format("%.2f", result))
            }else {
                var amountTwoString = etTwo?.text.toString().replace(",", "")
                var amountTwo = amountTwoString.toDoubleOrNull() ?: 0.0
                var amountTwoUsd = amountTwo / exchangeRates[currencyTwo]!!
                var result = amountTwoUsd * exchangeRates[currencyOne]!!

                etOne?.setText(String.format("%.2f", result))
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error converting concurrency", Toast.LENGTH_LONG).show()
        }
    }
}