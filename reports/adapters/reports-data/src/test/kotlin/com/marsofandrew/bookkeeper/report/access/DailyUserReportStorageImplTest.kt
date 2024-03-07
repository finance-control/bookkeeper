package com.marsofandrew.bookkeeper.report.access

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.DailyUserReport
import com.marsofandrew.bookkeeper.report.user.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DailyUserReportStorageImplTest {

    private val reportsByUserId: MutableMap<NumericId<User>, List<DailyUserReport>> = mutableMapOf()

    private lateinit var dailyUserReportStorageImpl: DailyUserReportStorageImpl

    @BeforeEach
    fun setup() {
        reportsByUserId.clear()
        dailyUserReportStorageImpl = DailyUserReportStorageImpl(reportsByUserId)
    }

    @Test
    fun `createOrUpdate when report is absent then adds that report`() {

    }
}