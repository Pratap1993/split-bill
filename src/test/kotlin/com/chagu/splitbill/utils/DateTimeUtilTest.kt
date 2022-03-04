package com.chagu.splitbill.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DateTimeUtilTest {

    @Test
    fun `test parcing string to localtime`() {
        val response = DateTimeUtil.getLocalDateTimeFromString("2020-05-22 09:00")
        assertEquals(response.dayOfMonth, 22)
    }
}