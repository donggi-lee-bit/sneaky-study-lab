package donggi.example.account.domain

import java.time.LocalDateTime
import java.util.*

class Account {
    private val id: AccountId? = null
    private val baselineBalance: Money? = null
    private val activityWindow: ActivityWindow? = null

    fun getId(): Optional<AccountId> {
        return Optional.ofNullable(this.id)
    }

    fun calculateBalance(): Money {
        return Money.add(
            this.baselineBalance,
            activityWindow.calculateBalance(this.id))
    }

    fun withdraw(money: Money, targetAccountId: AccountId?): Boolean {
        if (!mayWithdraw(money)) {
            return false
        }

        val withdrawal: Activity = Activity(
            this.id,
            this.id,
            targetAccountId,
            LocalDateTime.now(),
            money)
        activityWindow.addActivity(withdrawal)
        return true
    }

    private fun mayWithdraw(money: Money): Boolean {
        return Money.add(
            this.calculateBalance(),
            money.negate())
            .isPositiveOrZero()
    }

    fun deposit(money: Money?, sourceAccountId: AccountId?): Boolean {
        val deposit: Activity = Activity(
            this.id,
            sourceAccountId,
            this.id,
            LocalDateTime.now(),
            money)
        activityWindow.addActivity(deposit)
        return true
    }

    class AccountId {
        private val value: Long? = null
    }

    companion object {
        fun withoutId(
            baselineBalance: Money?,
            activityWindow: ActivityWindow?
        ): Account {
            return Account(null, baselineBalance, activityWindow)
        }

        fun withId(
            accountId: AccountId?,
            baselineBalance: Money?,
            activityWindow: ActivityWindow?
        ): Account {
            return Account(accountId, baselineBalance, activityWindow)
        }
    }
}
