package donggi.example.account.application.port.`in`

import donggi.example.account.domain.Account
import donggi.example.account.domain.Money

data class SendMoneyCommand(
    private val sourceAccountId: Account.AccountId,
    private val targetAccountId: Account.AccountId,
    private val money: Money
) {
    init {

    }
}
