package com.example.passgenerator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
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
        binding.forceUse.isChecked = true
        binding.forceUseLabel.setTextColor(resources.getColor(R.color.yellow))
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

        var passValue = getRandomString(length)
        setPassTextValue(passValue)
    }

    fun getRandomValue(allowedChars: String): String {
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun setPassTextValue(pass: String) {
        // Ter pelo menos 1 letra maiuscula e minuscula
        // ter pelo menos 1 digito
        // ter pelo menos 2 caracteres especiais
//

        if (characters != "~!@#$%^&;*+-/.,\\{}[]();:|?=\"`") {
            strong = false
            setStrongFalse()
        }

        if (strong) {
            var passUpper = false
            var passLower = false
            var passDigits = false
            var passCharacters = true
            pass.forEach {
                if (it.isUpperCase()) {
                    passUpper = true
                }
                if (it.isLowerCase()) {
                    passLower = true
                }
                if (it.isDigit()) {
                    passDigits = true
                }
                var chama = "~!@#$%^&;*+-/.,\\{}[]();:|?=\"`"
                for (i in chama) {
                    if (i == it) {
                        passCharacters = true
                    }
                }
            }
            if (passUpper && passLower && passDigits && passCharacters) {
                binding.passwordText.text = convertPassLenght(pass)
            } else {
                var passValue = getRandomString(length)
                setPassTextValue(passValue)
            }
        } else {
            binding.passwordText.text = convertPassLenght(pass)
        }
    }

    fun convertPassLenght(pass: String): String {
        if (pass.length > 10) {
            return "${pass.substring(0, 10)}..."
        } else {
            return pass
        }
    }

    fun setAllParameters() {
        length = binding.passLength.text.toString().toInt()
        checkedUpper = binding.switchUpper.isChecked
        checkedLower = binding.switchLower.isChecked
        checkedDigits = binding.switchDigits.isChecked
        checkedSpecial = binding.switchSpecial.isChecked
    }

    fun getSpecialCharacter() {
        characters = binding.special.getText().toString()
    }

    fun resetSpecialCharacters(v: View?) {
        if (characters != "~!@#$%^&;*+-/.,\\{}[]();:|?=\"`") {
            characters = "~!@#$%^&;*+-/.,\\{}[]();:|?=\"`"
        }
        binding.special.setText(characters)
    }

    fun strongPassword(v: View?) {
        setStrong()

        resetSpecialCharacters(v)

        var allChecked = checkedUpper && checkedLower && checkedDigits && checkedSpecial

//        if (characters != "~!@#$%^&;*+-/.,\\{}[]();:|?=\"`") {
//            checkedSpecial = false
//        }

        if (!allChecked) {
            setStrongFalse()
        }
    }

    fun setStrong() {
        strong = binding.forceUse.isChecked

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

    fun copy(view: View) {
        var textField = binding.passwordText.getText().toString()
        if (textField != "Press >>") {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", textField)
            clipboardManager.setPrimaryClip(clip)
            Toast.makeText(getApplicationContext(), "$textField copiado para o clipboard",
                Toast.LENGTH_SHORT).show()
        }
    }

    fun clear(v: View) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("clear", " ")
        clipboardManager.setPrimaryClip(clip)
        Toast.makeText(getApplicationContext(), "Clean",
            Toast.LENGTH_SHORT).show()
    }

    fun getRandomString(length: Int): String {

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


}