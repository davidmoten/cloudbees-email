package com.github.davidmoten.flashq;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

public class Parameters {

	public static String toString(HttpServletRequest req) {
		StringBuilder s = new StringBuilder();
		@SuppressWarnings("unchecked")
		Enumeration<String> en = req.getParameterNames();
		while (en.hasMoreElements()) {
			String key = en.nextElement();
			String[] values = req.getParameterValues(key);
			for (String value : values) {
				if (s.length() > 0)
					s.append("||");
				s.append(key);
				s.append("=");
				s.append(value);
			}
		}
		return s.toString();
	}

}
