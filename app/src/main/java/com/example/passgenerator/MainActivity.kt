package com.example.passgenerator

import android.graphics.PorterDuff
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.passgenerator.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var checkedThemeGlobal = false
    var length = 10
    var checkedUpper = true
    var checkedLower = true
    var checkedDigits = true
    var checkedSpecial = true
//    var checkedForce = true
    var strong = true
    var characters = ""

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
        binding.switchSpecial.isChecked = true

        // Set them on startup
        setTheme()
        setStrong()
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

        fun getRandomString(length: Int): String {

//            if (checkedUpper && !checkedLower && !checkedDigits) {
//                var allowedChars = ('A'..'Z')
//                return sequenceChars(allowedChars)
//
//            }

            var upperCase = ""
            var lowerCase = ""
            var digits = ""
            characters = ""

            if (checkedUpper) {
                upperCase = "ABCDEFGHIJKLMNOPQRSTUVXZ"
            } else {
                upperCase = ""
            }

            if (checkedLower) {
                lowerCase = "abcdefghijklmnopqrstuvxz"
            } else {
                lowerCase = ""
            }

            if (checkedDigits) {
                digits = "0123456789"
            } else {
                digits = ""
            }

            if (checkedSpecial) {
                getSpecialCharacter()
            } else {
                characters = ""
            }

            var finalCharacters = upperCase + lowerCase + digits + characters

            return if (finalCharacters != "") {
                getRandomValue(finalCharacters)
            } else {
                "..."
            }

        }

        var passValue = getRandomString(length)
        setPassTextValue(passValue)

    }

    fun getRandomValue(allowedChars: String): String {
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
        checkedSpecial = binding.switchSpecial.isChecked
    }

    fun getSpecialCharacter() {
        characters = binding.special.getText().toString()
    }

    fun resetSpecialCharacters(v: View?) {
        characters = "~!@#$%^&;*+-/.,\\{}[]();:|?=\"`"
        binding.special.setText(characters)
    }

    fun strongPassword(v: View?) {
        setStrong()

        var allChecked = checkedUpper && checkedLower && checkedDigits && checkedSpecial

        if (!allChecked) {
            setStrongFalse()
        }
    }

    fun setStrong() {
        var strong = binding.forceUse.isChecked



        if (strong) {
            binding.switchUpper.isChecked = true
            binding.switchLower.isChecked = true
            binding.switchDigits.isChecked = true
            binding.switchSpecial.isChecked = true

            checkedUpper = true
            checkedLower = true
            checkedDigits = true
            checkedSpecial = true
            binding.forceUseLabel.setTextColor(resources.getColor(R.color.yellow))
        } else {
            binding.forceUseLabel.setTextColor(resources.getColor(R.color.teal_200))
        }
    }

    fun setStrongFalse() {
        binding.forceUse.isChecked = false
        binding.forceUseLabel.setTextColor(resources.getColor(R.color.teal_200))
    }


}