package com.chagu.splitbill.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTimeUtil {
    companion object {
        //DateTime should be in "2020-04-13 18:00" format
        fun getLocalDateTimeFromString(dateTimeInStr: String): LocalDateTime {
            var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            var dateTime = LocalDateTime.parse(dateTimeInStr, formatter)
            return dateTime
        }
    }
}