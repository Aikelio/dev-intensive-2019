package ru.skillbranch.devintensive.extensions

import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time

    time +=when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return this
}

fun numeralRu(value: Int, single: String, few: String, many: String): String
{
    return when {
        value % 10 == 0 -> many
        value in 11..19 -> many
        value % 10 == 1 -> single
        value % 10 in 1..4 -> few
        else -> many
    }
}

fun Date.humanizeDiff() : String {
    val diffMilsec: Long = Date().time - this.time
    return when {
        diffMilsec > 360 * DAY -> "более года назад"
        diffMilsec < -360 * DAY -> "более чем через год"
        diffMilsec > 26 * HOUR -> {
            val days: Int = (diffMilsec.toFloat() / DAY.toFloat()).roundToInt()
            "$days ${numeralRu(days, "день", "дня", "дней")} назад"
        }
        diffMilsec < -26 * HOUR -> {
            val days: Int = (diffMilsec.toFloat() / DAY.toFloat()).roundToInt()
            "через ${-days} ${numeralRu(-days, "день", "дня", "дней")}"
        }
        diffMilsec > 22 * HOUR -> "день назад"
        diffMilsec < -22 * HOUR -> "через день"
        diffMilsec > 75 * MINUTE -> {
            val hours: Int = (diffMilsec.toFloat() / HOUR.toFloat()).roundToInt()
            "$hours ${numeralRu(hours, "час", "часа", "часов")} назад"
        }
        diffMilsec < -75 * MINUTE -> {
            val hours: Int = (diffMilsec.toFloat() / HOUR.toFloat()).roundToInt()
            "через ${-hours} ${numeralRu(-hours, "час", "часа", "часов")}"
        }
        diffMilsec > 45 * MINUTE -> "час назад"
        diffMilsec < -45 * MINUTE -> "через час"
        diffMilsec > 75 * SECOND -> {
            val minutes: Int = (diffMilsec.toFloat() / MINUTE.toFloat()).roundToInt()
            "$minutes ${numeralRu(minutes, "минута", "минуты", "минут")} назад"
        }
        diffMilsec < -75 * SECOND -> {
            val minutes: Int = (diffMilsec.toFloat() / MINUTE.toFloat()).roundToInt()
            "через ${-minutes} ${numeralRu(-minutes, "минута", "минуты", "минут")}"
        }
        diffMilsec > 45 * SECOND -> "минуту назад"
        diffMilsec < -45 * SECOND -> "через минуту"
        diffMilsec > 1 * SECOND -> "несколько секунд назад"
        diffMilsec < -1 * SECOND -> "через несколько секунд"
        else -> "только что"
    }
}
enum class TimeUnits {
    SECOND {
        override fun plural(value: Int): String {
            return "$value ${numeralRu(value, "секунду", "секунды", "секунд")}"
        }
    },
    MINUTE {
        override fun plural(value: Int): String {
            return "$value ${numeralRu(value, "минуту", "минуты", "минут")}"
        }
    },
    HOUR {
        override fun plural(value: Int): String {
            return "$value ${numeralRu(value, "час", "часа", "часов")}"
        }
    },
    DAY {
        override fun plural(value: Int): String {
            return "$value ${numeralRu(value, "день", "дня", "дней")}"
        }
    };
    abstract fun plural(value: Int): String
}