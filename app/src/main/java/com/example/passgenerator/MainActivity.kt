package com.example.passgenerator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.passgenerator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var checkedThemeGlobal = false
    var length = 10
    var checkedUpper = true
    var checkedLower = true
    var checkedDigits = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        pref.apply {
            val checked = getBoolean("checked", false)
            checkedThemeGlobal = checked
        }

        binding.switchUpper.isChecked = true
        binding.switchLower.isChecked = true
        binding.switchDigits.isChecked = true
        // Set them on startup
        setTheme()
    }


    fun theme(view: View?) {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()
        var checked = binding.darkTheme.isChecked
        editor.putBoolean("checked", checked).apply()
        checkedThemeGlobal = checked
        setTheme()
    }

    // Set theme
    fun setTheme() {
        binding.darkTheme.isChecked = checkedThemeGlobal
        if (checkedThemeGlobal) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun randomPass(view: View?) {
        setAllParameters()

        var allowedChars = ('0'..'9') + ('A'..'Z')


        fun getRandomString(length: Int): String {

            if (checkedUpper && !checkedLower && !checkedDigits) {
                var allowedChars = ('A'..'Z')
                return sequenceChars(allowedChars)

            } else if (!checkedUpper && checkedLower && !checkedDigits) {
                var allowedChars = ('a'..'z')
                return sequenceChars(allowedChars)


            } else if (!checkedUpper && !checkedLower && checkedDigits) {
                var allowedChars = ('0'..'9')
                return sequenceChars(allowedChars)

            } else if (checkedUpper && checkedLower && checkedDigits) {
                var allowedChars = ('0'..'9') + ('A'..'Z') + ('a'..'z')
                return sequenceCharList(allowedChars)


            } else if (checkedUpper && checkedLower && !checkedDigits) {
                var allowedChars = ('A'..'Z') + ('a'..'z')
                return sequenceCharList(allowedChars)


            } else if (!checkedUpper && checkedLower && checkedDigits) {
                var allowedChars = ('a'..'z') + ('0'..'9')
                return sequenceCharList(allowedChars)


            } else if (checkedUpper && !checkedLower && checkedDigits) {
                var allowedChars = ('0'..'9') + ('A'..'Z')
                return sequenceCharList(allowedChars)
            }

            return " "
        }

        var passValue = getRandomString(length)
        setPassTextValue(passValue)
    }

    fun sequenceChars(allowedChars: CharRange): String {
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun sequenceCharList(allowedChars: List<Char>): String {
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun setPassTextValue(pass: String) {
        binding.passwordText.text = convertPassLenght(pass)
    }

    fun convertPassLenght(pass: String): String {
        if (pass.length > 10) {
            return "${pass.substring(0, 10)}..."
        } else {
            return pass
        }
    }

    fun setAllParameters() {
        length = binding.passwordLenght.text.toString().toInt()
        checkedUpper = binding.switchUpper.isChecked
        checkedLower = binding.switchLower.isChecked
        checkedDigits = binding.switchDigits.isChecked
    }
}