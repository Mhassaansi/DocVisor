package com.fictivestudios.docsvisor.helper

import java.util.regex.Pattern

class Validations {

    companion object
    {

        fun CharSequence.isValidPassword(): Boolean {
            val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
            val pattern = Pattern.compile(passwordPattern)
            val matcher = pattern.matcher(this)
            return matcher.matches()
        }

        fun isValidEmail(str: String): Boolean{
            return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
        }
    }
}