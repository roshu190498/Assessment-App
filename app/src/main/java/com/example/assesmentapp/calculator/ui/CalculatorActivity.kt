package com.example.assesmentapp.calculator.ui

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.assesmentapp.R
import com.example.assesmentapp.calculator.viewmodel.CalculatorViewModel
import com.example.assesmentapp.databinding.ActivityCalculatorBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager


@AndroidEntryPoint
class CalculatorActivity : AppCompatActivity() , View.OnClickListener, OnTouchListener{

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

    private val calculatorViewModel : CalculatorViewModel by viewModels()


    var scriptEngine: ScriptEngine? = null
    private lateinit var mDatabinding : ActivityCalculatorBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDatabinding = DataBindingUtil.setContentView(this,R.layout.activity_calculator)

        scriptEngine = ScriptEngineManager().getEngineByName("rhino")
        setOnClickListeners()
        setOnTouchListener()
    }

    private fun setOnTouchListener() {
        mDatabinding.buttonZero.setOnClickListener(this)
        mDatabinding.buttonOne.setOnClickListener(this)
        mDatabinding.buttonTwo.setOnClickListener(this)
        mDatabinding.buttonThree.setOnClickListener(this)
        mDatabinding.buttonFour.setOnClickListener(this)
        mDatabinding.buttonFive.setOnClickListener(this)
        mDatabinding.buttonSix.setOnClickListener(this)
        mDatabinding.buttonSeven.setOnClickListener(this)
        mDatabinding.buttonEight.setOnClickListener(this)
        mDatabinding.buttonNine.setOnClickListener(this)

        mDatabinding.buttonClear.setOnClickListener(this)
        mDatabinding.buttonParentheses.setOnClickListener(this)
        mDatabinding.buttonPercent.setOnClickListener(this)
        mDatabinding.buttonDivision.setOnClickListener(this)
        mDatabinding.buttonMultiplication.setOnClickListener(this)
        mDatabinding.buttonSubtraction.setOnClickListener(this)
        mDatabinding.buttonAddition.setOnClickListener(this)
        mDatabinding.buttonEqual.setOnClickListener(this)
        mDatabinding.buttonDot.setOnClickListener(this)
    }

    private fun setOnClickListeners() {
        mDatabinding.buttonZero.setOnTouchListener(this)
        mDatabinding.buttonOne.setOnTouchListener(this)
        mDatabinding.buttonTwo.setOnTouchListener(this)
        mDatabinding.buttonThree.setOnTouchListener(this)
        mDatabinding.buttonFour.setOnTouchListener(this)
        mDatabinding.buttonFive.setOnTouchListener(this)
        mDatabinding.buttonSix.setOnTouchListener(this)
        mDatabinding.buttonSeven.setOnTouchListener(this)
        mDatabinding.buttonEight.setOnTouchListener(this)
        mDatabinding.buttonNine.setOnTouchListener(this)

        mDatabinding.buttonClear.setOnTouchListener(this)
        mDatabinding.buttonParentheses.setOnTouchListener(this)
        mDatabinding.buttonPercent.setOnTouchListener(this)
        mDatabinding.buttonDivision.setOnTouchListener(this)
        mDatabinding.buttonMultiplication.setOnTouchListener(this)
        mDatabinding.buttonSubtraction.setOnTouchListener(this)
        mDatabinding.buttonAddition.setOnTouchListener(this)
        mDatabinding.buttonEqual.setOnTouchListener(this)
        mDatabinding.buttonDot.setOnTouchListener(this)
    }

    override fun onClick(view: View?) {

        when (view?.id) {
            R.id.button_zero -> if (calculatorViewModel.addNumber(mDatabinding.textViewInputNumbers,"0")) equalClicked = false
            R.id.button_one -> if (calculatorViewModel.addNumber(mDatabinding.textViewInputNumbers,"1")) equalClicked = false
            R.id.button_two -> if (calculatorViewModel.addNumber(mDatabinding.textViewInputNumbers,"2")) equalClicked = false
            R.id.button_three -> if (calculatorViewModel.addNumber(mDatabinding.textViewInputNumbers,"3")) equalClicked = false
            R.id.button_four -> if (calculatorViewModel.addNumber(mDatabinding.textViewInputNumbers,"4")) equalClicked = false
            R.id.button_five -> if (calculatorViewModel.addNumber(mDatabinding.textViewInputNumbers,"5")) equalClicked = false
            R.id.button_six -> if (calculatorViewModel.addNumber(mDatabinding.textViewInputNumbers,"6")) equalClicked = false
            R.id.button_seven -> if (calculatorViewModel.addNumber(mDatabinding.textViewInputNumbers,"7")) equalClicked = false
            R.id.button_eight -> if (calculatorViewModel.addNumber(mDatabinding.textViewInputNumbers,"8")) equalClicked = false
            R.id.button_nine -> if (calculatorViewModel.addNumber(mDatabinding.textViewInputNumbers,"9")) equalClicked = false
            R.id.button_addition -> if (calculatorViewModel.addOperand(mDatabinding.textViewInputNumbers,"+")) equalClicked = false
            R.id.button_subtraction -> if (calculatorViewModel.addOperand(mDatabinding.textViewInputNumbers,"-")) equalClicked = false
            R.id.button_multiplication -> if (calculatorViewModel.addOperand(mDatabinding.textViewInputNumbers,"x")) equalClicked = false
            R.id.button_division -> if (calculatorViewModel.addOperand(mDatabinding.textViewInputNumbers,"\u00F7")) equalClicked = false
            R.id.button_percent -> if (calculatorViewModel.addOperand(mDatabinding.textViewInputNumbers,"%")) equalClicked = false
            R.id.button_dot -> if (calculatorViewModel.addDot(mDatabinding.textViewInputNumbers)) equalClicked = false
            R.id.button_parentheses -> if (calculatorViewModel.addParenthesis(mDatabinding.textViewInputNumbers)) equalClicked = false
            R.id.button_clear -> {
                mDatabinding.textViewInputNumbers.text = ""
                openParenthesis = 0
                dotUsed = false
                equalClicked = false
            }
            R.id.button_equal -> if (mDatabinding.textViewInputNumbers.text
                    .toString() != null && mDatabinding.textViewInputNumbers.text.toString() != ""
            ) {
                mDatabinding.textViewInputNumbers.text =  calculatorViewModel.calculate(this,mDatabinding.textViewInputNumbers.text.toString())
            }
        }


    }

    override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
        when (motionEvent?.action) {
            MotionEvent.ACTION_DOWN -> {
                view?.background?.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP)
                view?.invalidate()
            }
            MotionEvent.ACTION_UP -> {
                view?.background?.clearColorFilter()
                view?.invalidate()
            }
        }
        return false
    }


//    private fun addDot(): Boolean {
//        var done = false
//        if (mDatabinding.textViewInputNumbers.text.length == 0) {
//            mDatabinding.textViewInputNumbers.text = "0."
//            dotUsed = true
//            done = true
//        } else if (dotUsed == true) {
//        } else if (defineLastCharacter(
//                mDatabinding.textViewInputNumbers.text.get(mDatabinding.textViewInputNumbers.text.length - 1)
//                    .toString() + ""
//            ) == IS_OPERAND
//        ) {
//            mDatabinding.textViewInputNumbers.text = (mDatabinding.textViewInputNumbers.text.toString() + "0.")
//            done = true
//            dotUsed = true
//        } else if (defineLastCharacter(
//                mDatabinding.textViewInputNumbers.getText().get(mDatabinding.textViewInputNumbers.text.length - 1)
//                    .toString() + ""
//            ) == IS_NUMBER
//        ) {
//            mDatabinding.textViewInputNumbers.text = (mDatabinding.textViewInputNumbers.text.toString() + ".")
//            done = true
//            dotUsed = true
//        }
//        return done
//    }
//
//    private fun addParenthesis(): Boolean {
//        var done = false
//        val operationLength: Int = mDatabinding.textViewInputNumbers.text.length
//        if (operationLength == 0) {
//            mDatabinding.textViewInputNumbers.text = mDatabinding.textViewInputNumbers.text.toString() + "("
//            dotUsed = false
//            openParenthesis++
//            done = true
//        } else if (openParenthesis > 0 && operationLength > 0) {
//            val lastInput: String =
//                mDatabinding.textViewInputNumbers.text.get(operationLength - 1).toString() + ""
//            when (defineLastCharacter(lastInput)) {
//                IS_NUMBER -> {
//                    mDatabinding.textViewInputNumbers.text = mDatabinding.textViewInputNumbers.text.toString() + ")"
//                    done = true
//                    openParenthesis--
//                    dotUsed = false
//                }
//                IS_OPERAND -> {
//                    mDatabinding.textViewInputNumbers.text = mDatabinding.textViewInputNumbers.text.toString() + "("
//                    done = true
//                    openParenthesis++
//                    dotUsed = false
//                }
//                IS_OPEN_PARENTHESIS -> {
//                    mDatabinding.textViewInputNumbers.text = mDatabinding.textViewInputNumbers.text.toString() + "("
//                    done = true
//                    openParenthesis++
//                    dotUsed = false
//                }
//                IS_CLOSE_PARENTHESIS -> {
//                    mDatabinding.textViewInputNumbers.text = mDatabinding.textViewInputNumbers.text.toString() + ")"
//                    done = true
//                    openParenthesis--
//                    dotUsed = false
//                }
//            }
//        } else if (openParenthesis == 0 && operationLength > 0) {
//            val lastInput: String =
//                mDatabinding.textViewInputNumbers.text.get(operationLength - 1).toString() + ""
//            if (defineLastCharacter(lastInput) == IS_OPERAND) {
//                mDatabinding.textViewInputNumbers.text = mDatabinding.textViewInputNumbers.text.toString() + "("
//                done = true
//                dotUsed = false
//                openParenthesis++
//            } else {
//                mDatabinding.textViewInputNumbers.text= mDatabinding.textViewInputNumbers.text.toString() + "x("
//                done = true
//                dotUsed = false
//                openParenthesis++
//            }
//        }
//        return done
//    }
//
//    private fun addOperand(operand: String): Boolean {
//        var done = false
//        val operationLength: Int = mDatabinding.textViewInputNumbers.text.length
//        if (operationLength > 0) {
//            val lastInput: String =
//                mDatabinding.textViewInputNumbers.text.get(operationLength - 1).toString() + ""
//            if (lastInput == "+" || lastInput == "-" || lastInput == "*" || lastInput == "\u00F7" || lastInput == "%") {
//                Toast.makeText(applicationContext, "Wrong format", Toast.LENGTH_LONG).show()
//            } else if (operand == "%" && defineLastCharacter(lastInput) == IS_NUMBER) {
//                mDatabinding.textViewInputNumbers.text=mDatabinding.textViewInputNumbers.text.toString() + operand
//                dotUsed = false
//                equalClicked = false
//                lastExpression = ""
//                done = true
//            } else if (operand != "%") {
//                mDatabinding.textViewInputNumbers.text = mDatabinding.textViewInputNumbers.text.toString() + operand
//                dotUsed = false
//                equalClicked = false
//                lastExpression = ""
//                done = true
//            }
//        } else {
//            Toast.makeText(
//                applicationContext,
//                "Wrong Format. Operand Without any numbers?",
//                Toast.LENGTH_LONG
//            ).show()
//        }
//        return done
//    }
//
//    private fun addNumber(number: String): Boolean {
//        var done = false
//        val operationLength: Int = mDatabinding.textViewInputNumbers.text.length
//        if (operationLength > 0) {
//            val lastCharacter: String =
//                mDatabinding.textViewInputNumbers.text.get(operationLength - 1).toString() + ""
//            val lastCharacterState = defineLastCharacter(lastCharacter)
//            if (operationLength == 1 && lastCharacterState == IS_NUMBER && lastCharacter == "0") {
//                mDatabinding.textViewInputNumbers.text = number
//                done = true
//            } else if (lastCharacterState == IS_OPEN_PARENTHESIS) {
//                mDatabinding.textViewInputNumbers.text = mDatabinding.textViewInputNumbers.text.toString() + number
//                done = true
//            } else if (lastCharacterState == IS_CLOSE_PARENTHESIS || lastCharacter == "%") {
//                mDatabinding.textViewInputNumbers.text = mDatabinding.textViewInputNumbers.text.toString() + "x" + number
//                done = true
//            } else if (lastCharacterState == IS_NUMBER || lastCharacterState == IS_OPERAND || lastCharacterState == IS_DOT) {
//                mDatabinding.textViewInputNumbers.text = mDatabinding.textViewInputNumbers.text.toString() + number
//                done = true
//            }
//        } else {
//            mDatabinding.textViewInputNumbers.text = mDatabinding.textViewInputNumbers.text.toString() + number
//            done = true
//        }
//        return done
//    }
//
//
//    private fun calculate(input: String) {
//        var result = ""
//        try {
//            var temp = input
//            if (equalClicked) {
//                temp = input + lastExpression
//            } else {
//                saveLastExpression(input)
//            }
//            result = scriptEngine!!.eval(
//                temp.replace("%".toRegex(), "/100").replace("x".toRegex(), "*")
//                    .replace("[^\\x00-\\x7F]".toRegex(), "/")
//            ).toString()
//            val decimal = BigDecimal(result)
//            result = decimal.setScale(8, BigDecimal.ROUND_HALF_UP).toPlainString()
//            equalClicked = true
//        } catch (e: Exception) {
//            Toast.makeText(applicationContext, "Wrong Format", Toast.LENGTH_SHORT).show()
//            return
//        }
//        if (result == "Infinity") {
//            showToast("Division by zero is not allowed")
//            mDatabinding.textViewInputNumbers.text = input
//        } else if (result.contains(".")) {
//            result = result.replace("\\.?0*$".toRegex(), "")
//            mDatabinding.textViewInputNumbers.text  = result
//        }
//    }
//
//    private fun saveLastExpression(input: String) {
//        val lastOfExpression = input[input.length - 1].toString() + ""
//        if (input.length > 1) {
//            if (lastOfExpression == ")") {
//                lastExpression = ")"
//                var numberOfCloseParenthesis = 1
//                for (i in input.length - 2 downTo 0) {
//                    if (numberOfCloseParenthesis > 0) {
//                        val last = input[i].toString() + ""
//                        if (last == ")") {
//                            numberOfCloseParenthesis++
//                        } else if (last == "(") {
//                            numberOfCloseParenthesis--
//                        }
//                        lastExpression = last + lastExpression
//                    } else if (defineLastCharacter(input[i].toString() + "") == IS_OPERAND) {
//                        lastExpression = input[i].toString() + lastExpression
//                        break
//                    } else {
//                        lastExpression = ""
//                    }
//                }
//            } else if (defineLastCharacter(lastOfExpression + "") == IS_NUMBER) {
//                lastExpression = lastOfExpression
//                for (i in input.length - 2 downTo 0) {
//                    val last = input[i].toString() + ""
//                    if (defineLastCharacter(last) == IS_NUMBER || defineLastCharacter(
//                            last
//                        ) == IS_DOT
//                    ) {
//                        lastExpression = last + lastExpression
//                    } else if (defineLastCharacter(last) == IS_OPERAND) {
//                        lastExpression = last + lastExpression
//                        break
//                    }
//                    if (i == 0) {
//                        lastExpression = ""
//                    }
//                }
//            }
//        }
//    }
//
//    private fun defineLastCharacter(lastCharacter: String): Int {
//        try {
//            lastCharacter.toInt()
//            return IS_NUMBER
//        } catch (e: NumberFormatException) {
//        }
//        if (lastCharacter == "+" || lastCharacter == "-" || lastCharacter == "x" || lastCharacter == "\u00F7" || lastCharacter == "%") return IS_OPERAND
//        if (lastCharacter == "(") return IS_OPEN_PARENTHESIS
//        if (lastCharacter == ")") return IS_CLOSE_PARENTHESIS
//        return if (lastCharacter == ".") IS_DOT else -1
//    }


}