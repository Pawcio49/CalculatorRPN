package com.example.calculatorrpn

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    private val minPrecision : Int = 0
    private val maxPrecision : Int = 16
    private var precision : Int = 8
    private val defaultColorCode : String = "#FFFFFF"
    private lateinit var colorCode : String

    private lateinit var colorView : TextView
    private lateinit var redNumber : EditText
    private lateinit var greenNumber : EditText
    private lateinit var blueNumber : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        setPrecision()
        setBackgroundColor()
    }

    private fun setPrecision() {
        val precisionPicker : NumberPicker = findViewById(R.id.precisionPicker)
        precisionPicker.minValue = minPrecision
        precisionPicker.maxValue = maxPrecision
        precisionPicker. wrapSelectorWheel = true

        if (intent != null) {
            precision = intent.getIntExtra("precision", 8)
            precisionPicker.value = precision
        }

        precisionPicker.setOnValueChangedListener() {
                _, _, newPrecision ->
            precision = newPrecision
        }
    }

    private fun setBackgroundColor() {
        colorView = findViewById(R.id.colorView)
        redNumber = findViewById(R.id.redNumber)
        greenNumber = findViewById(R.id.greenNumber)
        blueNumber = findViewById(R.id.blueNumber)

        redNumber.addTextChangedListener(colorListener())
        greenNumber.addTextChangedListener(colorListener())
        blueNumber.addTextChangedListener(colorListener())

        colorCode = intent.getStringExtra("colorCode").toString()
        colorView.setBackgroundColor(Color.parseColor(colorCode))

        getRGB()
    }

    private fun colorListener(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                onColorChanged()
            }

            override fun afterTextChanged(p0: Editable?) {}
        }
    }

    private fun onColorChanged() {
        val red = getHexadecimalColor(redNumber)
        val green = getHexadecimalColor(greenNumber)
        val blue = getHexadecimalColor(blueNumber)

        colorCode = "#$red$green$blue"

        if(colorCode.length == 7){
            colorView.text = ""
        } else {
            colorCode = defaultColorCode
            colorView.text = "Set the color values from 0 to 255"
        }

        colorView.setBackgroundColor(Color.parseColor(colorCode))
    }

    private fun getHexadecimalColor(colorNumber : EditText): String {
        var color : String
        if(colorNumber.text.toString() != ""){
            color = Integer.toHexString(Integer.parseInt(colorNumber.text.toString()))
            if(color.length == 1){
                color = "0$color"
            }
        } else {
            color = "00"
        }
        return color
    }

    private fun getRGB(){
        val red = colorCode.substring(1,3).toInt(16)
        val green = colorCode.substring(3,5).toInt(16)
        val blue = colorCode.substring(5,7).toInt(16)

        redNumber.setText(red.toString())
        blueNumber.setText(blue.toString())
        greenNumber.setText(green.toString())
    }

    private fun onBack() {
        val data = Intent()
        data.putExtra("precision", precision)
        data.putExtra("colorCode", colorCode)

        setResult(Activity.RESULT_OK, data)
        finish()
    }

    override fun onBackPressed() {
        onBack()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBack()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}