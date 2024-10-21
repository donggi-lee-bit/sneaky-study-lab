package donggi.example.account.application.service

import donggi.example.account.application.port.`in`.SendMoneyCommand
import donggi.example.account.application.port.`in`.SendMoneyUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/** 송금하기 유스케이스 */
@Service
@Transactional
class SendMoneyService : SendMoneyUseCase {

    override fun sendMoney(command: SendMoneyCommand): Boolean {
        // TODO: 비즈니스 규칙 검증
        // TODO: 모델 상태 조작
        // TODO: 출력 값 반환
        return true
    }
}
