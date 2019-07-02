package ru.skillbranch.devintensive.extensions

import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

enum class TimeUnits(val size: Long) {
    SECOND (1000L),
    MINUTE(60 * SECOND.size),
    HOUR(60 * MINUTE.size),
    DAY(24 * HOUR.size)

}

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

val Int.sec get() = this * SECOND
val Int.min get() = this * MINUTE
val Int.hour get() = this * HOUR
val Int.day get() = this * DAY

val Long.asMin get() = this.absoluteValue / MINUTE
val Long.asHour get() = this.absoluteValue / HOUR
val Long.asDay get() = this.absoluteValue / DAY


fun Date.format(pattern:String="HH:mm:ss dd:MM:yy") :String{
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)

}

fun Date.add(value:Int, units: TimeUnits = TimeUnits.SECOND) : Date{
    time += value * units.size
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val diff = ((date.time +200) / 1000 - (time + 200) / 1000) * 1000
    return if (diff >= 0){
        when (diff) {
            in 0.sec..1.sec -> "только что"
            in 1.sec..45.sec -> "несколько секунд назад"
            in 45.sec..75.sec -> "минуту назад"
            in 75.sec..45.min -> "${minutesAsPlurals(diff.asMin)} назад"
            in 45.min..75.min -> "час назад"
            in 75.min..22.hour -> "${hoursAsPlurals(diff.asHour)} назад"
            in 22.hour..26.hour -> "день назад"
            in 26.hour..360.day -> "${daysAsPlurals(diff)} назад"
            else -> "более года назад"

        }
    }else {
        when(diff){

            in (-1).sec..0.sec -> "прямо сейчас"
            in (-45).sec..(-1).sec -> "через несколько секунд"
            in (-75).sec..(-45).sec -> "минуту назад"
            in (-45).min..(-75).sec -> "через ${minutesAsPlurals(diff.asMin)}"
            in (-75).min..(-45).min -> "через час"
            in (-22).hour..(-75).min -> "через ${hoursAsPlurals(diff.asHour)}"
            in (-26).hour..(-22).hour -> "через день"
            in (-360).day..(-26).hour -> "через ${daysAsPlurals(diff.asDay)}"
            else -> "более чем через год"

        }
    }

}

private fun minutesAsPlurals(value: Long) = when (value.asPlurals) {
    Plurals.ONE -> "$value минуту"
    Plurals.FEW -> "$value минуты"
    Plurals.MANY -> "$value минут"

}
private fun hoursAsPlurals(value: Long) = when (value.asPlurals){
    Plurals.ONE -> "$value час"
    Plurals.FEW -> "$value часа"
    Plurals.MANY -> "$value часов"

}

private fun daysAsPlurals(value: Long) = when (value.asPlurals){
    Plurals.ONE -> "$value день"
    Plurals.FEW -> "$value дня"
    Plurals.MANY -> "$value дней"
}
enum class Plurals {
    ONE,
    FEW,
    MANY
}
val Long.asPlurals
    get() =when {
        this % 100L in 5L..20L -> Plurals.MANY
        this % 10L == 1L -> Plurals.ONE
        this % 10L in 2L..4L -> Plurals.FEW
        else -> Plurals.MANY
    }