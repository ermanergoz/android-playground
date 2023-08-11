package com.example.tipcalculator

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultTextView.visibility = View.INVISIBLE

        calculateButton.setOnClickListener {
            //this is a button listener
            //whenever this button is clicked, this block will run

            val bill: Double
            if (!billEditText.text.toString().isEmpty()) {
                bill = billEditText.text.toString().toDouble()
            } else {
                bill = 0.0
            }

            val tipPercentage: Double
            if (!tipPercentageEditText.text.toString().isEmpty()) {
                tipPercentage = tipPercentageEditText.text.toString().toDouble()
            } else {
                tipPercentage = 0.0
            }

            val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)
            /*this thing above is to hide the keyboard after we typed everything and clicked on calculate*/

            resultTextView.visibility = View.VISIBLE
            resultTextView.text =
                "Tip: \$" + String.format("%.2f", ((bill * tipPercentage) / 100)) + " Total: \$" + String.format(
                    "%.2f"/*this is to round the number so the number will be shorter with 2 digits after the coma*/,
                    (((bill * tipPercentage) / 100) + bill)
                )
        }
    }
}