package com.kicker.api.utils

import java.time.DayOfWeek.MONDAY
import java.time.DayOfWeek.SUNDAY
import java.time.LocalDate

/**
 * @author Yauheni Efimenko
 */
object DateUtils {

    fun getIntervalDatesOfWeek(weeksAgo: Long): Pair<LocalDate, LocalDate> {
        if (weeksAgo == 0L) {
            return Pair(LocalDate.now().with(MONDAY), LocalDate.now())
        }

        val monday = LocalDate.now().with(MONDAY).minusWeeks(weeksAgo)
        val sunday = LocalDate.now().with(SUNDAY).minusWeeks(weeksAgo)
        return Pair(monday, sunday)
    }

    fun getStartDateOfWeek(weeksAgo: Long): LocalDate = LocalDate.now().with(MONDAY).minusWeeks(weeksAgo)

    fun getIntervalDatesOfWeekDependsOnDay(date: LocalDate, weeksAgo: Long): Pair<LocalDate, LocalDate> {
        val monday = date.with(MONDAY).minusWeeks(weeksAgo)
        val sunday = date.with(SUNDAY).minusWeeks(weeksAgo)
        if (LocalDate.now().isBefore(sunday)) {
            return Pair(monday, LocalDate.now())
        }
        return Pair(monday, sunday)
    }

    fun getStartOfWeekDependsOnDay(date: LocalDate, weeksAgo: Long): LocalDate = date.with(MONDAY).minusWeeks(weeksAgo)

}