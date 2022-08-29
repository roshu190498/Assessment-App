package com.example.assesmentapp.calculator.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModel
import com.example.assesmentapp.base.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import javax.inject.Inject
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

@HiltViewModel
class CalculatorViewModel @Inject constructor()  : ViewModel(){
    private var openParenthesis = 0

    private var dotUsed = false

    private var equalClicked = false
    private var lastExpression = ""

    private var EXCEPTION = -1
    private var IS_NUMBER = 0
    private var IS_OPERAND = 1
    private var IS_OPEN_PARENTHESIS = 2
    private var IS_CLOSE_PARENTHESIS = 3
    private var IS_DOT = 4

    var scriptEngine: ScriptEngine? = null

    init {
        scriptEngine = ScriptEngineManager().getEngineByName("rhino")
    }


    public fun calculate(context : Context, input: String) : String {
        var result = ""
        try {
            var temp = input
            if (equalClicked) {
                temp = input + lastExpression
            } else {
                saveLastExpression(context, input)
            }
            result = scriptEngine!!.eval(
                temp.replace("%".toRegex(), "/100").replace("x".toRegex(), "*")
                    .replace("[^\\x00-\\x7F]".toRegex(), "/")
            ).toString()
            val decimal = BigDecimal(result)
            result = decimal.setScale(8, BigDecimal.ROUND_HALF_UP).toPlainString()
            equalClicked = true
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
        if (result == "Infinity") {
            context.showToast("Division by zero is not allowed")
           return input
        } else if (result.contains(".")) {
            result = result.replace("\\.?0*$".toRegex(), "")
            return result
        }

        return result
    }


    private fun saveLastExpression(context: Context, input: String) {
        val lastOfExpression = input[input.length - 1].toString() + ""
        if (input.length > 1) {
            if (lastOfExpression == ")") {
                lastExpression = ")"
                var numberOfCloseParenthesis = 1
                for (i in input.length - 2 downTo 0) {
                    if (numberOfCloseParenthesis > 0) {
                        val last = input[i].toString() + ""
                        if (last == ")") {
                            numberOfCloseParenthesis++
                        } else if (last == "(") {
                            numberOfCloseParenthesis--
                        }
                        lastExpression = last + lastExpression
                    } else if (defineLastCharacter(input[i].toString() + "") == IS_OPERAND) {
                        lastExpression = input[i].toString() + lastExpression
                        break
                    } else {
                        lastExpression = ""
                    }
                }
            } else if (defineLastCharacter(lastOfExpression + "") == IS_NUMBER) {
                lastExpression = lastOfExpression
                for (i in input.length - 2 downTo 0) {
                    val last = input[i].toString() + ""
                    if (defineLastCharacter(last) == IS_NUMBER || defineLastCharacter(
                            last
                        ) == IS_DOT
                    ) {
                        lastExpression = last + lastExpression
                    } else if (defineLastCharacter(last) == IS_OPERAND) {
                        lastExpression = last + lastExpression
                        break
                    }
                    if (i == 0) {
                        lastExpression = ""
                    }
                }
            }
        }
    }


    private fun defineLastCharacter(lastCharacter: String): Int {
        try {
            lastCharacter.toInt()
            return IS_NUMBER
        } catch (e: NumberFormatException) {
        }
        if (lastCharacter == "+" || lastCharacter == "-" || lastCharacter == "x" || lastCharacter == "\u00F7" || lastCharacter == "%") return IS_OPERAND
        if (lastCharacter == "(") return IS_OPEN_PARENTHESIS
        if (lastCharacter == ")") return IS_CLOSE_PARENTHESIS
        return if (lastCharacter == ".") IS_DOT else -1
    }


    fun addNumber(textview : AppCompatTextView,number: String): Boolean {
        var done = false
        val operationLength: Int = textview.text.length
        if (operationLength > 0) {
            val lastCharacter: String =
                textview.text.get(operationLength - 1).toString() + ""
            val lastCharacterState = defineLastCharacter(lastCharacter)
            if (operationLength == 1 && lastCharacterState == IS_NUMBER && lastCharacter == "0") {
                textview.text = number
                done = true
            } else if (lastCharacterState == IS_OPEN_PARENTHESIS) {
                textview.text = textview.text.toString() + number
                done = true
            } else if (lastCharacterState == IS_CLOSE_PARENTHESIS || lastCharacter == "%") {
                textview.text = textview.text.toString() + "x" + number
                done = true
            } else if (lastCharacterState == IS_NUMBER || lastCharacterState == IS_OPERAND || lastCharacterState == IS_DOT) {
                textview.text = textview.text.toString() + number
                done = true
            }
        } else {
            textview.text = textview.text.toString() + number
            done = true
        }
        return done
    }

    public fun addOperand(textView : AppCompatTextView, operand: String): Boolean {
        var done = false
        val operationLength: Int = textView.text.length
        if (operationLength > 0) {
            val lastInput: String =
                textView.text.get(operationLength - 1).toString() + ""
            if (lastInput == "+" || lastInput == "-" || lastInput == "*" || lastInput == "\u00F7" || lastInput == "%") {
                textView.context.showToast("Wrong format")
            } else if (operand == "%" && defineLastCharacter(lastInput) == IS_NUMBER) {
                textView.text=textView.text.toString() + operand
                dotUsed = false
                equalClicked = false
                lastExpression = ""
                done = true
            } else if (operand != "%") {
                textView.text = textView.text.toString() + operand
                dotUsed = false
                equalClicked = false
                lastExpression = ""
                done = true
            }
        } else {
            textView.context.showToast("Wrong Format. Operand Without any numbers?")
        }
        return done
    }


    public fun addParenthesis(textView: AppCompatTextView): Boolean {
        var done = false
        val operationLength: Int = textView.text.length
        if (operationLength == 0) {
            textView.text = textView.text.toString() + "("
            dotUsed = false
            openParenthesis++
            done = true
        } else if (openParenthesis > 0 && operationLength > 0) {
            val lastInput: String =
                textView.text.get(operationLength - 1).toString() + ""
            when (defineLastCharacter(lastInput)) {
                IS_NUMBER -> {
                    textView.text = textView.text.toString() + ")"
                    done = true
                    openParenthesis--
                    dotUsed = false
                }
                IS_OPERAND -> {
                    textView.text = textView.text.toString() + "("
                    done = true
                    openParenthesis++
                    dotUsed = false
                }
                IS_OPEN_PARENTHESIS -> {
                    textView.text = textView.text.toString() + "("
                    done = true
                    openParenthesis++
                    dotUsed = false
                }
                IS_CLOSE_PARENTHESIS -> {
                    textView.text = textView.text.toString() + ")"
                    done = true
                    openParenthesis--
                    dotUsed = false
                }
            }
        } else if (openParenthesis == 0 && operationLength > 0) {
            val lastInput: String =
                textView.text.get(operationLength - 1).toString() + ""
            if (defineLastCharacter(lastInput) == IS_OPERAND) {
                textView.text = textView.text.toString() + "("
                done = true
                dotUsed = false
                openParenthesis++
            } else {
                textView.text= textView.text.toString() + "x("
                done = true
                dotUsed = false
                openParenthesis++
            }
        }
        return done
    }

    public fun addDot(textView: AppCompatTextView): Boolean {
        var done = false
        if (textView.text.length == 0) {
            textView.text = "0."
            dotUsed = true
            done = true
        } else if (dotUsed == true) {
        } else if (defineLastCharacter(
                textView.text.get(textView.text.length - 1)
                    .toString() + ""
            ) == IS_OPERAND
        ) {
            textView.text = (textView.text.toString() + "0.")
            done = true
            dotUsed = true
        } else if (defineLastCharacter(
                textView.getText().get(textView.text.length - 1)
                    .toString() + ""
            ) == IS_NUMBER
        ) {
            textView.text = (textView.text.toString() + ".")
            done = true
            dotUsed = true
        }
        return done
    }

}