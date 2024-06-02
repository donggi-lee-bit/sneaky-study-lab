package donggi.example.account.domain

import java.math.BigInteger

data class Money(
    private val amount: BigInteger? = null
) {
    companion object {
        var ZERO: Money = of(0L)

        fun of(value: Long): Money {
            return Money(BigInteger.valueOf(value))
        }

        fun add(a: Money, b: Money): Money {
            return Money(a.amount!!.add(b.amount))
        }

        fun subtract(a: Money, b: Money): Money {
            return Money(a.amount!!.subtract(b.amount))
        }
    }

    val isPositiveOrZero: Boolean
        get() = amount!! >= BigInteger.ZERO

    val isNegative: Boolean
        get() = amount!! < BigInteger.ZERO

    val isPositive: Boolean
        get() = amount!! > BigInteger.ZERO

    fun isGreaterThanOrEqualTo(money: Money): Boolean {
        return amount!! >= money.amount
    }

    fun isGreaterThan(money: Money): Boolean {
        return amount!!.compareTo(money.amount) >= 1
    }

    fun minus(money: Money): Money {
        return Money(amount!!.subtract(money.amount))
    }

    fun plus(money: Money): Money {
        return Money(amount!!.add(money.amount))
    }

    fun negate(): Money {
        return Money(amount!!.negate())
    }
}
