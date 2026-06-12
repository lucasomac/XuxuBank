package br.com.lucolimac.xuxubank.ui.util

/**
 * Utility for user input validation.
 */
object ValidationUtils {
    /**
     * Standard regex for email validation.
     */
    private val EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()

    /**
     * Validates if a string is a valid email.
     */
    fun isValidEmail(email: String): Boolean = email.matches(EMAIL_REGEX)

    /**
     * Validates if a string is a valid person's name (letters, spaces, and common punctuation).
     */
    fun isValidName(name: String): Boolean {
        if (name.isBlank()) return false
        // Regex for name: allows letters (including accents), spaces, hyphens and apostrophes
        val nameRegex = "^[\\p{L} '\\-]+$".toRegex()
        return name.matches(nameRegex)
    }

    /**
     * Validates if a string is a valid phone number (digits only, length 10 or 11).
     */
    fun isValidPhone(phone: String): Boolean {
        val digits = phone.filter { it.isDigit() }
        return digits.length == 10 || digits.length == 11
    }
}
