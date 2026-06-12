package br.com.lucolimac.xuxubank.ui.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Applies a phone mask (XX) XXXXX-XXXX as the user types.
 */
class PhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 11) text.text.substring(0..10) else text.text
        var out = ""
        for (i in trimmed.indices) {
            if (i == 0) out += "("
            out += trimmed[i]
            if (i == 1) out += ") "
            if (i == 6) out += "-"
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val transformedOffset = when {
                    offset <= 0 -> 0
                    offset <= 1 -> offset + 1
                    offset <= 6 -> offset + 3
                    offset <= 11 -> offset + 4
                    else -> 15
                }
                return transformedOffset.coerceAtMost(out.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                val originalOffset = when {
                    offset <= 1 -> 0
                    offset <= 2 -> offset - 1
                    offset <= 5 -> 2
                    offset <= 10 -> offset - 3
                    offset <= 11 -> 7
                    offset <= 15 -> offset - 4
                    else -> 11
                }
                return originalOffset.coerceAtMost(text.text.length)
            }
        }

        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}
