package com.example.calculatorapp

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.calculatorapp.databinding.ActivityMainBinding
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var lastNumeric: Boolean = false
    var lastDot: Boolean = false
    var stateError: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.InputTextView.showSoftInputOnFocus = false

    }

    fun onOperator(view: View) {
        try{
            if (lastNumeric && !stateError) {
                binding.InputTextView.append((view as Button).text)
                lastNumeric = false
                lastDot = false
            }
        }catch (e : Exception){}
    }

    fun onDigit(view: View) {
        if (stateError) {
            binding.InputTextView.text = (view as Button).text as Editable?
            stateError = false
        } else {
            // If not, already there is a valid expression so append to it
            binding.InputTextView.append((view as Button).text)
        }
        lastNumeric = true
    }

    fun onDecimal(view: View) {
        if (lastNumeric && !stateError && !lastDot) {
            binding.InputTextView.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    fun onBackspace(view: View) {
        var cursorPosition = binding.InputTextView.selectionStart
        var textSize = binding.InputTextView.length()

        when {
            cursorPosition != 0 && textSize != 0 -> {
                val selection: Editable = binding.InputTextView.text
                selection.replace(cursorPosition - 1, cursorPosition, "")
                binding.InputTextView.setSelection(cursorPosition - 1)
                if (cursorPosition != 0 && binding.InputTextView.text.isNotBlank()){
                    lastNumeric = true
                    lastDot = false
                }
                else{
                    lastNumeric = false
                    lastDot = false
                }
            }
        }
    }

    fun onClear(view: View) {
        binding.InputTextView.setText("")
        binding.ResultTextView.text = ""
    }

    fun onCEClick(view: View) {
        var cursorPosition = binding.InputTextView.selectionEnd
        var textSize = binding.InputTextView.length()

        when {
            cursorPosition != 0 && textSize != 0 -> {
                val selection: Editable = binding.InputTextView.text
                selection.replace(cursorPosition, cursorPosition, "")
                binding.InputTextView.setSelection(cursorPosition - 1)
            }
        }
    }

    fun onEqual(view: View) {
        if (lastNumeric && !stateError) {
            try {
                val txt = binding.InputTextView.text.toString()
                val expression = ExpressionBuilder(txt).build()
                val result = expression.evaluate()
                val longResult = result.toLong()

                when(result){
                    longResult.toDouble() -> {
                        binding.ResultTextView.text = longResult.toString()
                    }
                    else -> binding.ResultTextView.text = result.toString()
                }
            } catch (ex: Exception) {
            }
        }
    }
}
