package com.easyfield.utils

import android.content.Context
import android.text.format.DateFormat
import java.util.*
import java.util.regex.Pattern

object Validator {
    private const val HEX_PATTERN = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"

    private val patternNoSpecialChar = Pattern.compile("[a-zA-Z0-9]*")

    private val EMAIL_PATTERN =
        ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")

    private val NUMBER_PATTERN = "\\d+(?:\\.\\d+)?"


    fun isValidVehicleNumber(vehicleNumber: String): Boolean {
        val regex = "^[A-Z]{2}\\d{2}[A-Z]{1,2}\\d{4}$".toRegex()
        return vehicleNumber.matches(regex)
    }

    fun isValidInteger(value: String): Boolean {
        val pattern = Pattern.compile(NUMBER_PATTERN)
        val matcher = pattern.matcher(value)
        return matcher.matches()
    }

    fun validateEmailNotNull(email: String): Boolean {
        if (email.trim({ it <= ' ' }).isEmpty())
            return false
        return true
    }

    fun validateEmail(email: String): Boolean {
        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)
        return matcher.matches()
        //return true;
    }

    fun validatePasswordNotNull(password: String): Boolean {
        if (password.trim({ it <= ' ' }).isEmpty())
            return false
        return true
    }

    fun validateBirthdateNotNull(birthdate: String): Boolean {
        if (birthdate.trim({ it <= ' ' }).isEmpty())
            return false

        return true
    }

    fun validateGenderNotNull(gender: String): Boolean {
        if (gender.trim({ it <= ' ' }).isEmpty())
            return false
        return true
    }

    fun validateNameNotNull(name: String): Boolean {
        if (name.trim({ it <= ' ' }).isEmpty())
            return false
        return true
    }

    fun validateAddressNotNull(address: String): Boolean {
        if (address.trim({ it <= ' ' }).isEmpty())
            return false
        return true
    }

    fun validateAddress(address: String): Boolean {
        return false
    }

    fun getFormattedDate(context: Context, smsTimeInMilis: Long): String {
        val smsTime = Calendar.getInstance(Locale.getDefault())
        smsTime.timeInMillis = smsTimeInMilis

        val now = Calendar.getInstance(Locale.getDefault())

        val timeFormatString = "h:mm aa"
        val dateTimeFormatString = "MMMM dd, h:mm aa"
        val HOURS = (60 * 60 * 60).toLong()
        return if (now.get(Calendar.DATE) === smsTime.get(Calendar.DATE)) {
            "Today " + DateFormat.format(timeFormatString, smsTime)
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) === 1) {
            "Yesterday " + DateFormat.format(timeFormatString, smsTime)
        } else if (now.get(Calendar.YEAR) === smsTime.get(Calendar.YEAR)) {
            DateFormat.format(dateTimeFormatString, smsTime).toString()
        } else {
            DateFormat.format("MMMM dd yyyy, h:mm aa", smsTime).toString()
        }
    }

    fun getSmsTodayYestFromMilli(msgTimeMillis: Long): String {

        val messageTime = Calendar.getInstance()
        messageTime.timeInMillis = msgTimeMillis
        // get Currunt time
        val now = Calendar.getInstance()

        val strTimeFormat = "h:mm aa"
        //final String strDateFormate = "dd/MM/yyyy h:mm aa"
        val strDateFormat = "MMM dd HH:mm"

        if (now.get(Calendar.DATE) == messageTime.get(Calendar.DATE) &&
            ((now.get(Calendar.MONTH) == messageTime.get(Calendar.MONTH))) &&
            ((now.get(Calendar.YEAR) == messageTime.get(Calendar.YEAR)))
        ) {

            return "" + DateFormat.format(strTimeFormat, messageTime)

        } else if (((now.get(Calendar.DATE) - messageTime.get(Calendar.DATE)) == 1) &&
            ((now.get(Calendar.MONTH) == messageTime.get(Calendar.MONTH))) &&
            ((now.get(Calendar.YEAR) == messageTime.get(Calendar.YEAR)))
        ) {
            return "Yesterday " + DateFormat.format(strTimeFormat, messageTime)
        } else {
            return "" + DateFormat.format(strDateFormat, messageTime)
        }
    }

    fun getHeaderDate(msgTimeMillis: Long): String {

        val messageTime = Calendar.getInstance()
        messageTime.timeInMillis = msgTimeMillis
        // get Currunt time
        val now = Calendar.getInstance()

        val strDateFormat = "MMM dd yyyy"

        if (now.get(Calendar.DATE) == messageTime.get(Calendar.DATE) &&
            ((now.get(Calendar.MONTH) == messageTime.get(Calendar.MONTH))) &&
            ((now.get(Calendar.YEAR) == messageTime.get(Calendar.YEAR)))
        ) {

            return "Today"

        } else if (((now.get(Calendar.DATE) - messageTime.get(Calendar.DATE)) == 1) &&
            ((now.get(Calendar.MONTH) == messageTime.get(Calendar.MONTH))) &&
            ((now.get(Calendar.YEAR) == messageTime.get(Calendar.YEAR)))
        ) {
            return "Yesterday"
        } else {
            return "" + DateFormat.format(strDateFormat, messageTime)
        }
    }


    /**
     * Validate hex with regular expression
     * @param hex hex for validation
     * @return true valid hex, false invalid hex
     */
    fun validateHexColor(hex: String?): Boolean {
        return if (!hex.isNullOrEmpty()) {
            val pattern = Pattern.compile(HEX_PATTERN)
            val matcher = pattern.matcher(hex)
            matcher.matches()
        } else
            false
    }

    /**
     * phone number validator
     * */
    fun validCellPhone(number: String?): Boolean {
        return number?.length!! > 0
    }

    /**
     * OTP validator
     * */
    fun validOTP(number: String?): Boolean {
        return number?.length == 4
    }

    fun isNameValid(userName: String?): Boolean {
        return (userName != null && userName.length <= 25)
    }

    fun isAgeValid(age: String?): Boolean {
        return age.isNullOrEmpty()
    }

    fun isValidCard(cardNumber: String): Boolean {
        var sum = 0
        var alternate = false
        for (i in cardNumber.length - 1 downTo 0) {
            var n = cardNumber.substring(i, i + 1).toInt()
            if (alternate) {
                n *= 2
                if (n > 9) {
                    n = n % 10 + 1
                }
            }
            sum += n
            alternate = !alternate
        }
        return sum % 10 == 0
    }

}