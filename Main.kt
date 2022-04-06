package converter

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

fun main() {
    loop@ while (true) {
        print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ")
        val bases = readln()
        if (bases == "/exit") return
        val (sourceBaseString, targetBaseString) = bases.split(" ")
        val sourceBase = sourceBaseString.toBigInteger()
        val targetBase = targetBaseString.toBigInteger()
        while (true) {
            print("Enter number in base $sourceBase to convert to base $targetBase (To go back type /back) ")
            val numberInput = readln()
            if (numberInput == "/back") continue@loop
            else if (numberInput.contains(".")) {
                val (numberToConvertInteger, numberToConvertDecimal) = numberInput.split(".")
                val integerResult = fromDecimalInteger(targetBase, toDecimalInteger(sourceBase, numberToConvertInteger))
                val fractionalResult = fromDecimalDecimal(
                    targetBase.toBigDecimal(),
                    toDecimalDecimal(sourceBase.toBigDecimal(), numberToConvertDecimal)
                )
                println("Conversion result: $integerResult.${fractionalResult}")
            } else {
                println("Conversion result: ${fromDecimalInteger(targetBase, toDecimalInteger(sourceBase, numberInput))}")
            }
        }
    }
}

fun charToDigit(ch: Char): Int {
    return when (ch) {
        in '0'..'9' -> ch.digitToInt()
        else -> (ch - 87).code
    }
}

fun digitToChar(digit: Int): Char {
    return when (digit) {
        in 0..9 -> digit.digitToChar()
        else -> (digit + 87).toChar()
    }
}

fun toDecimalInteger(sB: BigInteger, number: String): BigInteger {
    var result = BigInteger.ZERO
    for (i in number.indices) {
        result += charToDigit(number[number.lastIndex - i]).toBigInteger() * sB.pow(i)
    }
    return result
}

fun fromDecimalInteger(tB: BigInteger, number: BigInteger): String {
    if (tB == BigInteger.TEN) return number.toString()
    var bigNumber = number
    var result = ""
    if (bigNumber == BigInteger.ZERO) return "0"
    while (bigNumber != BigInteger.ZERO) {
        if (tB < BigInteger.TEN) {
            result = (bigNumber % tB).toString() + result
            bigNumber /= tB
        } else {
            result = digitToChar((bigNumber % tB).toInt()).toString() + result
            bigNumber /= tB
        }
    }
    return result

}

fun toDecimalDecimal(sb: BigDecimal, number: String): BigDecimal {
    var result = BigDecimal.ZERO
    for (i in number.indices) {
        val divider = charToDigit(number[i]).toBigDecimal()
        val divisor = sb.pow(i + 1)
        result += divider.setScale(6) / divisor
    }
    return result
}

fun fromDecimalDecimal(tB: BigDecimal, number: BigDecimal): String {
    //if (tB == BigDecimal.TEN) return number.toString()
    if (number.compareTo(BigDecimal.ZERO) == 0) return "00000"
    var result = ""
    var bigNumber = number
    var fractionalPart: BigDecimal
    for(i in 0 until 5) {
        bigNumber *= tB
        fractionalPart = bigNumber.remainder(BigDecimal.ONE)
        result += digitToChar(bigNumber.setScale(0, RoundingMode.DOWN).toInt())
        bigNumber = fractionalPart
    }
    return result
}