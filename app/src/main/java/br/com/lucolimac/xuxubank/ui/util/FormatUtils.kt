package br.com.lucolimac.xuxubank.ui.util

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

/**
 * Utility for data formatting across the app.
 */
object FormatUtils {
    /**
     * Formats a BigDecimal value into a Brazilian Real currency format.
     */
    fun formatMonetary(value: BigDecimal): String {
        return NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"))
            .format(value)
    }

    /**
     * Formats a numeric string into a Brazilian Real currency format.
     * Handles both dot and comma as decimal separators.
     */
    fun formatMonetary(value: String): String {
        val cleanString = value.replace(".", "").replace(",", ".")
        val bigDecimalValue = cleanString.toBigDecimalOrNull() ?: BigDecimal.ZERO
        return formatMonetary(bigDecimalValue)
    }
}
