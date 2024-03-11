package com.marsofandrew.bookkeeper.user.access.id

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.user.User
import java.time.Clock
import java.util.concurrent.atomic.AtomicReference
import kotlin.random.Random

/**
 * Generates unique ids based on time, hostId and sequence number
 * ID has length of 64 bits that will be following:
 *  1st = 0 as Long has to be positive
 *  2nd - 34th (33 bits)= seconds from 2024-03-10T00:00:00Z (it will be enough for 272 years)
 *  35th-39th (5 bits) = Host ID
 *  40th-43th (4 bits) = Thread id, for now always zero
 *  44th-57th (14 bits) = Sequence number
 *  58th-64th (7 bits) = Random number for safety
 */
internal class SnowfallUserIdGenerator(
    private val clock: Clock,
    private val randomGenerator: Random,
    hostId: Byte,
) : UserIdGenerator {

    private val hostNumber: Long = hostId.toLong().shl(25)
    private var timedCounter: AtomicReference<TimedCounter> = AtomicReference(TimedCounter(0))

    init {
        check(hostId in 0..31) { "Host id is greater than 31 or negative" }
    }

    override fun generateId(): NumericId<User> {
        val time = getTime()
        val timePart = time.shl(30)
        val sequencePart = getSequenceNumber(time).shl(7)
        val randomPart = generateRandom()

        return (timePart + hostNumber + sequencePart + randomPart).asId()
    }

    private fun getTime(): Long {
        return clock.instant().epochSecond - User.APP_EPOCH_SECONDS
    }

    private fun generateRandom(): Int = randomGenerator.nextInt(MAX_RANDOM)

    private fun getSequenceNumber(time: Long): Int {
        return timedCounter.updateAndGet { counter ->
            if (counter.time == time) {
                TimedCounter(time, counter.value + 1)
            } else {
                TimedCounter(time)
            }
        }.value
    }

    private data class TimedCounter(
        val time: Long,
        val value: Int = 0
    )

    private companion object {
        const val MAX_RANDOM = 128
    }
}