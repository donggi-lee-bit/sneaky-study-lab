package donggi.example.account.application.port.`in`

interface SendMoneyUseCase {
    fun sendMoney(command: SendMoneyCommand): Boolean
}
