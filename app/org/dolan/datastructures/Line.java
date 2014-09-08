package org.dolan.datastructures;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dolan.tools.LogTool;

/**
 * The Class Line.
 * The Line class is an implementation of ILine.
 * It represents a single line in a Debenhams API log file.
 */
public class Line implements ILine {
	
	/** The raw line. */
	private String raw;
	
	/** The thread number. */
	private int threadNo;
	
	/** The time stamp. */
	private TimeStamp timeStamp;
	
	/** The line number. */
	private int lineNumber;

	/**
	 * Instantiates a new line.
	 *
	 * @param raw the line string in full
	 * @param lineNumber the line number
	 */
	public Line(String raw, int lineNumber) {
		LogTool.traceC(this.getClass(), "Creating Line object");
		Objects.requireNonNull(raw);
		if (lineNumber < 0) {
			throw new IllegalArgumentException("Cannot have a negative line number");
		}
		this.raw = raw;
		this.threadNo = findThreadNo(raw);
		this.timeStamp = getTime(raw);
		this.lineNumber = lineNumber;
	}

	/**
	 * Find thread the thread number.
	 *
	 * @param line the line
	 * @return the int
	 */
	private int findThreadNo(String line) {
		LogTool.traceC(this.getClass(), "Finding thread number of line", line);
		Pattern p = Pattern.compile("http-executor-threads - (\\d+)");
		Matcher m = p.matcher(line);

		if (m.find()) {
			try {
				int value = Integer.parseInt(m.group(1));
				LogTool.traceC(this.getClass(), "Thread number is", value);
				return value;
			} catch (NumberFormatException nfe) {
				LogTool.error("The thread number found is not an integer,  something wrong is happening", nfe);
				return -1;
			}
		} else {
			return -1;
		}
	}

	/**
	 * Gets the time.
	 *
	 * @param line the line
	 * @return the time
	 */
	private TimeStamp getTime(String line) {
		LogTool.traceC(this.getClass(), "Finding time from line", line);
		Pattern p = Pattern.compile("(\\d+):(\\d+):(\\d+),(\\d+)");
		Matcher m = p.matcher(line);

		if (m.find()) {
			try {
				int hour = Integer.parseInt(m.group(1));
				int minute = Integer.parseInt(m.group(2));
				int second = Integer.parseInt(m.group(3));
				int millisecond = Integer.parseInt(m.group(4));
				TimeStamp time = new TimeStamp(hour, minute, second, millisecond);
				/*
				 * Date time = new Date(); SimpleDateFormat format = new
				 * SimpleDateFormat("HH:mm:ss,SSS"); try { time =
				 * format.parse(m.group(1)); } catch (ParseException e) { //
				 * TODO Auto-generated catch block e.printStackTrace(); }
				 */
				LogTool.traceC(this.getClass(), "Line has a time of", time);
				return time;
			} catch (NumberFormatException nfe) {
				LogTool.error("The time stamp cannot be parsed.", nfe);
				return null;
			}
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.raw;
	}

	/* (non-Javadoc)
	 * @see org.dolan.datastructures.ILine#getThreadNumber()
	 */
	@Override
	public int getThreadNumber() {
		return this.threadNo;
	}

	/* (non-Javadoc)
	 * @see org.dolan.datastructures.ILine#getTimeStamp()
	 */
	@Override
	public TimeStamp getTimeStamp() {
		return this.timeStamp;
	}
}
