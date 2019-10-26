package at.ict4d.ict4dnews.utils

import java.util.Random
import java.util.UUID
import org.threeten.bp.LocalDateTime

const val NUMBER_LIMIT: Int = 100

fun generateUUID() = UUID.randomUUID().toString()

fun generateRandomURL() = "https://www.example.com/${generateUUID()}"

fun generateRandomNumber(numberLimit: Int = NUMBER_LIMIT): Int = Random().nextInt(numberLimit) + 1

fun generatePastLocalDateTime(
    days: Long = generateRandomNumber(10000).toLong(),
    hours: Long = generateRandomNumber(10000).toLong(),
    minutes: Long = generateRandomNumber(10000).toLong()
): LocalDateTime {
    return LocalDateTime.now().minusDays(days).minusHours(hours).minusMinutes(minutes)
}
