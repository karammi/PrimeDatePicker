package com.aminography.primedatepicker.calendarview.callback

import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primedatepicker.monthview.PrimeMonthView

/**
 * @author aminography
 */
interface IMonthViewHolderCallback {

    val minDateCalendar: BaseCalendar?

    val maxDateCalendar: BaseCalendar?

    val pickType: PrimeMonthView.PickType?

    val pickedStartRangeDay: BaseCalendar?

    val pickedEndRangeDay: BaseCalendar?

    val pickedSingleDay: BaseCalendar?

    fun onDayClick(day: BaseCalendar)
}