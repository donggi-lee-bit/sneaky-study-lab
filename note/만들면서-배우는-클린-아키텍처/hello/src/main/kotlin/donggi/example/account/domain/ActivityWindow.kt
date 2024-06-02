package donggi.example.account.domain

import java.time.LocalDateTime
import java.util.*

class ActivityWindow {

    private val activities: MutableList<Activity>

    val startTimestamp: LocalDateTime
        /**
         * The timestamp of the first activity within this window.
         */
        get() = activities.stream()
            .min(Comparator.comparing(Activity::timestamp))
            .orElseThrow { IllegalStateException() }
            .timestamp

    val endTimestamp: LocalDateTime
        get() = activities.stream()
            .max(Comparator.comparing(Activity::timestamp))
            .orElseThrow { IllegalStateException() }
            .timestamp

    fun calculateBalance(accountId: Account.AccountId): Money {
        val depositBalance: Money = activities
            .filter { activity: -> activity.targetAccountId == accountId }
            .map(Activity::money)
            .reduce(Money.ZERO, Money::add)

        val withdrawalBalance: Money = activities
            .filter { activity -> activity.sourceAccountId == accountId }
            .map(Activity::money)
            .reduce(Money.ZERO, Money::add)

        return Money.add(depositBalance, withdrawalBalance.negate())
    }

    constructor(activities: MutableList<Activity>) {
        this.activities = activities
    }

    constructor(activities: Activity) {
        this.activities = ArrayList<E>(Arrays.asList<Array<Activity>>(*activities))
    }

    fun getActivities(): List<Activity> {
        return Collections.unmodifiableList(this.activities)
    }

    fun addActivity(activity: Activity) {
        activities.add(activity)
    }
}
