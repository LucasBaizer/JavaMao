package com.mao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Debug {
	private static final SimpleDateFormat timestampFormat = new SimpleDateFormat("HH:mm:ss");

	private static String getPrefix() {
		return timestampFormat.format(new Timestamp(new Date().getTime())) + " ["
				+ (Network.isServer() ? "SERVER" : "CLIENT") + "] ";
	}

	public static void log(String msg) {
		System.out.println(getPrefix() + "DEBUG: " + msg);
	}

	public static void warn(String msg) {
		System.out.println(getPrefix() + "WARNING: " + msg);
	}

	public static void error(String msg) {
		System.err.println(getPrefix() + "SEVERE: " + msg);
	}

	public static void error(String msg, Throwable err) {
		System.err.println(getPrefix() + msg);
		err.printStackTrace();
	}
}
