package org.dolan.datastructures;

import java.util.Calendar;
import java.util.Date;

/**
 * The Class TimeStamp.
 * This class is used to model a time stamp.
 * Used in ILine to store the time in which the line was produced.
 */
public class TimeStamp implements Comparable<TimeStamp> {
	
	/** The hour. */
	public final int hour;
	
	/** The minute. */
	public final int minute;
	
	/** The second. */
	public final int second;
	
	/** The millisecond. */
	public final int millisecond;

	/**
	 * Instantiates a new time stamp.
	 *
	 * @param hour the hour
	 * @param minute the minute
	 * @param second the second
	 * @param millisecond the millisecond
	 */
	public TimeStamp(int hour, int minute, int second, int millisecond) {
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.millisecond = millisecond;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second) + "," + String.format("%02d", millisecond);
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(TimeStamp ts) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, this.hour);
		cal.set(Calendar.MINUTE, this.minute);
		cal.set(Calendar.SECOND, this.second);
		cal.set(Calendar.MILLISECOND, this.millisecond);
		Date d = cal.getTime();
		
		Calendar tsCal = Calendar.getInstance();
		tsCal.set(Calendar.HOUR_OF_DAY, ts.hour);
		tsCal.set(Calendar.MINUTE, ts.minute);
		tsCal.set(Calendar.SECOND, ts.second);
		tsCal.set(Calendar.MILLISECOND, ts.millisecond);
		Date tsD = tsCal.getTime();
		
		return d.compareTo(tsD);
	}
}
