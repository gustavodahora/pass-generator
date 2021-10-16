package com.example.passgenerator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.example.passgenerator.databinding.ActivityMainBinding
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var checkedThemeGlobal = false

    private var length = 10

    private var checkedUpper = true
    private var checkedLower = true
    private var checkedDigits = true
    private var checkedSpecial = true
    private var characters = "~!@#\$%^&;*+-/.,\\{}[]();:|?=\"`"

    private var strong = true
    private var clean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val pref = getSharedPreferences(
            "dev.gustavodahora.pass_generator", Context.MODE_PRIVATE
        )
        pref.apply {
            val checked = getBoolean("checked", false)
            checkedThemeGlobal = checked
        }

        binding.switchUpper.isChecked = true
        binding.switchLower.isChecked = true
        binding.switchDigits.isChecked = true
        binding.switchSpecial.isChecked = true

        binding.special.setText(characters)

        // Set them on startup
        setTheme()
        setStrong()
        binding.forceUse.isChecked = true
        binding.forceUseLabel.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.yellow
            )
        )

        // Set the onclick function
        binding.btnGenerate.setOnClickListener { randomPass() }
        binding.switchTheme.setOnClickListener { theme() }
        binding.btnCopy.setOnClickListener { copy() }
        binding.btnClear.setOnClickListener { clear() }
        binding.switchUpper.setOnClickListener {
            strongPassword()
            itemLabelChange()
        }
        binding.switchDigits.setOnClickListener {
            strongPassword()
            itemLabelChange()
        }
        binding.switchSpecial.setOnClickListener {
            strongPassword()
            itemLabelChange()
        }
        binding.switchLower.setOnClickListener {
            strongPassword()
            itemLabelChange()
        }
        binding.forceUse.setOnClickListener { strongPassword() }
        binding.restoreSpecial.setOnClickListener { resetSpecialCharacters() }

    }


    private fun theme() {
        val pref = getSharedPreferences(
            "dev.gustavodahora.pass_generator", Context.MODE_PRIVATE
        )
        val editor = pref.edit()
        val checked = binding.switchTheme.isChecked
        editor.putBoolean("checked", checked).apply()
        checkedThemeGlobal = checked
        setTheme()
    }

    // Set theme
    private fun setTheme() {
        binding.switchTheme.isChecked = checkedThemeGlobal
        if (checkedThemeGlobal) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun randomPass() {
        setAllParameters()

        val passValue = getRandomString()
        setPassTextValue(passValue)
    }

    private fun getRandomValue(allowedChars: String): String {
        clean = false
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun setPassTextValue(pass: String) {
        // 1 letra maiuscula e minuscula
        // 1 digito
        // 2 caracteres especiais

        if (characters != "~!@#\$%^&;*+-/.,\\{}[]();:|?=\"`") {
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
                val chama = "~!@#\$%^&;*+-/.,\\{}[]();:|?=\"`"
                for (i in chama) {
                    if (i == it) {
                        passCharacters = true
                    }
                }
            }
            if (passUpper && passLower && passDigits && passCharacters) {
                binding.passwordText.text = convertPassLenght(pass)
            } else {
                val passValue = getRandomString()
                setPassTextValue(passValue)
            }
        } else {
            binding.passwordText.text = convertPassLenght(pass)
        }
    }

    private fun convertPassLenght(pass: String): String {
        return if (pass.length > 10) {
            "${pass.substring(0, 10)}..."
        } else {
            pass
        }
    }

    private fun setAllParameters() {
        length = binding.passLength.text.toString().toInt()
        checkedUpper = binding.switchUpper.isChecked
        checkedLower = binding.switchLower.isChecked
        checkedDigits = binding.switchDigits.isChecked
        checkedSpecial = binding.switchSpecial.isChecked
    }

    private fun getSpecialCharacter() {
        characters = binding.special.text.toString()
    }

    private fun resetSpecialCharacters() {
        if (characters != "~!@#\$%^&;*+-/.,\\{}[]();:|?=\"`") {
            characters = "~!@#\$%^&;*+-/.,\\{}[]();:|?=\"`"
        }
        binding.special.setText(characters)
    }

    private fun strongPassword() {
        setStrong()

        val allChecked = checkedUpper && checkedLower && checkedDigits && checkedSpecial

        if (!allChecked) {
            setStrongFalse()
        }
    }

    private fun setStrong() {
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
            binding.forceUseLabel.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.yellow
                )
            )

            val snackbar = Snackbar.make(
                findViewById(R.id.scr_view),
                getString(R.string.remove_strong_to_continue),
                Snackbar.LENGTH_LONG
            )
            snackbar.setBackgroundTint(
                ContextCompat.getColor(
                    applicationContext,
                    android.R.color.black
                )
            )
            snackbar.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    android.R.color.white
                )
            )
            snackbar.show()
            itemLabelChange()

        } else {
            binding.forceUseLabel.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.gray
                )
            )
            itemLabelChange()
        }
    }

    private fun itemLabelChange() {
        checkedUpper = binding.switchUpper.isChecked
        checkedLower = binding.switchLower.isChecked
        checkedDigits = binding.switchDigits.isChecked
        checkedSpecial = binding.switchSpecial.isChecked
        if (!checkedUpper) {
            binding.switchUppercaseLabel.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.gray
                )
            )
        } else {
            binding.switchUppercaseLabel.setTextColor(
                MaterialColors.getColor(binding.switchUppercaseLabel, R.attr.colorOnSecondary)
            )
        }
        if (!checkedLower) {
            binding.lettersLowercaseLabel.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.gray
                )
            )
        } else {
            binding.lettersLowercaseLabel.setTextColor(
                MaterialColors.getColor(binding.switchUppercaseLabel, R.attr.colorOnSecondary)
            )
        }
        if (!checkedDigits) {
            binding.digitsLabel.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.gray
                )
            )
        } else {
            binding.digitsLabel.setTextColor(
                MaterialColors.getColor(binding.switchUppercaseLabel, R.attr.colorOnSecondary)
            )
        }
        if (!checkedSpecial) {
            binding.special.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.gray
                )
            )
        } else {
            binding.special.setTextColor(
                MaterialColors.getColor(binding.switchUppercaseLabel, R.attr.colorOnSecondary)
            )
        }
    }

    private fun setStrongFalse() {
        binding.forceUse.isChecked = false

        binding.forceUseLabel.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.gray
            )
        )
    }

    private fun copy() {
        val textField = binding.passwordText.text.toString()
        if (textField != "Press >>") {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", textField)
            clipboardManager.setPrimaryClip(clip)
            Toast.makeText(
                applicationContext, "$textField " + getString(R.string.copy_to_clipboard),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun clear() {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("clear", " ")
        clipboardManager.setPrimaryClip(clip)

        if (clean) {
            val snackbar = Snackbar.make(
                findViewById(R.id.scr_view),
                getString(R.string.already_is_clean),
                Snackbar.LENGTH_LONG
            )
            snackbar.setBackgroundTint(
                ContextCompat.getColor(
                    applicationContext,
                    android.R.color.black
                )
            )
            snackbar.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    android.R.color.white
                )
            )
            snackbar.show()
        } else {
            val snackbar = Snackbar.make(
                findViewById(R.id.scr_view),
                getString(R.string.cleaned), Snackbar.LENGTH_LONG
            )
            snackbar.setBackgroundTint(
                ContextCompat.getColor(
                    applicationContext,
                    android.R.color.black
                )
            )
            snackbar.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    android.R.color.white
                )
            )
            snackbar.show()
            binding.passwordText.text = getString(R.string.passTemporary)
        }
        clean = true
    }

    private fun getRandomString(): String {
        characters = ""

        val upperCase: String = if (checkedUpper) {
            "ABCDEFGHIJKLMNOPQRSTUVXZ"
        } else {
            ""
        }

        val lowerCase: String = if (checkedLower) {
            "abcdefghijklmnopqrstuvxz"
        } else {
            ""
        }

        val digits: String = if (checkedDigits) {
            "0123456789"
        } else {
            ""
        }

        if (checkedSpecial) {
            getSpecialCharacter()
        } else {
            characters = ""
        }

        val finalCharacters = upperCase + lowerCase + digits + characters

        return if (finalCharacters != "") {
            getRandomValue(finalCharacters)
        } else {
            "..."
        }

    }

    @ColorInt
    fun Context.getColorFromAttr(
        @AttrRes attrColor: Int,
        typedValue: TypedValue = TypedValue(),
        resolveRefs: Boolean = true
    ): Int {
        theme.resolveAttribute(attrColor, typedValue, resolveRefs)
        return typedValue.data
    }
}

