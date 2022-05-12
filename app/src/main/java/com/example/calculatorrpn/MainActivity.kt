package com.example.calculatorrpn

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    private lateinit var stackSizeView : TextView
    private lateinit var currentNumberView : TextView
    private lateinit var stackNumber1 : TextView
    private lateinit var stackNumber2 : TextView
    private lateinit var stackNumber3 : TextView
    private lateinit var stackNumber4 : TextView
    private lateinit var displayLayout : LinearLayout

    private val stack = mutableListOf<Double>()
    private val defaultPrecision : Int = 8
    private val defaultBackgroundColor : String = "#FFFFFF"
    private lateinit var currentNumber : String
    private var precision : Int = defaultPrecision
    private var backgroundColor : String = defaultBackgroundColor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setStackSizeView()
        setStackNumbers()
        setStartingCurrentNumber()

        setNumberOnClickListeners()
        setOperationsListeners()
        setOtherKeysOnClickListeners()

        displayLayout = findViewById(R.id.displayLayout)
    }

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            if (intent != null) {
                precision = intent.getIntExtra("precision", defaultPrecision)
                backgroundColor = intent.getStringExtra("colorCode").toString()
                displayLayout.setBackgroundColor(Color.parseColor(backgroundColor))
                updateStack()
            }
        }
    }

    private fun setStackSizeView() {
        stackSizeView = findViewById(R.id.stackSizeView)
        stackSizeView.text = "0"
    }

    private fun setStackNumbers() {
        stackNumber1 = findViewById(R.id.stackNumber1)
        stackNumber2 = findViewById(R.id.stackNumber2)
        stackNumber3 = findViewById(R.id.stackNumber3)
        stackNumber4 = findViewById(R.id.stackNumber4)
    }

    private fun setStartingCurrentNumber() {
        currentNumber = "0"
        currentNumberView = findViewById(R.id.currentNumberView)
        currentNumberView.text = currentNumber
    }

    private fun onClickNumber(digit : Int) {
        if(currentNumber.length == 1 && currentNumber[0] == '0'){
            currentNumber = digit.toString()
        } else {
            currentNumber += digit.toString()
        }
        currentNumberView.text = currentNumber
    }

    private fun setOperationsListeners() {
        plus.setOnClickListener() {
            val n = stack.size
            if(n >= 2) {
                stack[n-2] = stack[n-2] + stack[n-1]
                stack.removeLast()
                updateStack()
            }
        }

        substract.setOnClickListener() {
            val n = stack.size
            if(n >= 2) {
                stack[n-2] = stack[n-2] - stack[n-1]
                stack.removeLast()
                updateStack()
            }
        }

        multiply.setOnClickListener() {
            val n = stack.size
            if(n >= 2) {
                stack[n-2] = stack[n-2] * stack[n-1]
                stack.removeLast()
                updateStack()
            }
        }

        divide.setOnClickListener() {
            val n = stack.size
            if(n >= 2) {
                stack[n-2] = stack[n-2] / stack[n-1]
                stack.removeLast()
                updateStack()
            }
        }

        power.setOnClickListener() {
            val n = stack.size
            if(n >= 2) {
                stack[n-2] = stack[n-2].pow(stack[n-1])
                stack.removeLast()
                updateStack()
            }
        }

        root.setOnClickListener() {
            val n = stack.size
            if(n >= 1) {
                stack[n-1] = sqrt(stack[n-1])
                updateStack()
            }
        }

        drop.setOnClickListener() {
            val n = stack.size
            if(n >= 1) {
                stack.removeLast()
                updateStack()
            }
        }

        swap.setOnClickListener() {
            val n = stack.size
            if(n >= 2) {
                val temp = stack[n-2]
                stack[n-2] = stack[n-1]
                stack[n-1] = temp
                updateStack()
            }
        }
    }

    private fun updateStack() {
        val n = stack.size

        if(n >= 4) {
            stackNumber4.text = String.format("%.${precision}f", stack[n-4])
        } else {
            stackNumber4.text = ""
        }
        if(n >= 3) {
            stackNumber3.text = String.format("%.${precision}f", stack[n-3])
        } else {
            stackNumber3.text = ""
        }
        if(n >= 2) {
            stackNumber2.text = String.format("%.${precision}f", stack[n-2])
        } else {
            stackNumber2.text = ""
        }
        if(n >= 1) {
            stackNumber1.text = String.format("%.${precision}f", stack[n-1])
        } else {
            stackNumber1.text = ""
        }
        stackSizeView.text = stack.size.toString()
    }

    private fun setOtherKeysOnClickListeners() {
        setting.setOnClickListener() {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("precision", precision)
            intent.putExtra("colorCode", backgroundColor)
            startForResult.launch(intent)
        }

        negation.setOnClickListener() {
            currentNumber = if(currentNumber[0] == '-') {
                currentNumber.drop(1)
            } else {
                "-$currentNumber"
            }
            currentNumberView.text = currentNumber
        }

        dot.setOnClickListener() {
            if(!currentNumber.contains('.')) {
                currentNumber += '.'
                currentNumberView.text = currentNumber
            }
        }

        ac.setOnClickListener() {
            setStartingCurrentNumber()
            stack.clear()
            updateStack()
        }

        undo.setOnClickListener() {
            currentNumber = if(currentNumber[0] == '-') {
                if(currentNumber.length == 2) {
                    "0"
                } else {
                    currentNumber.dropLast(1)
                }
            } else {
                if(currentNumber.length == 1){
                    "0"
                } else {
                    currentNumber.dropLast(1)
                }
            }
            currentNumberView.text = currentNumber
        }

        enter.setOnClickListener() {
            stack.add(currentNumber.toDouble())
            setStartingCurrentNumber()
            updateStack()
        }
    }

    private fun setNumberOnClickListeners() {
        no0.setOnClickListener(){
            onClickNumber(0)
        }

        no1.setOnClickListener(){
            onClickNumber(1)
        }

        no2.setOnClickListener(){
            onClickNumber(2)
        }

        no3.setOnClickListener(){
            onClickNumber(3)
        }

        no4.setOnClickListener(){
            onClickNumber(4)
        }

        no5.setOnClickListener(){
            onClickNumber(5)
        }

        no6.setOnClickListener(){
            onClickNumber(6)
        }

        no7.setOnClickListener(){
            onClickNumber(7)
        }

        no8.setOnClickListener(){
            onClickNumber(8)
        }

        no9.setOnClickListener(){
            onClickNumber(9)
        }
    }
}