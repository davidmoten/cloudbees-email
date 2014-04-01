package com.github.davidmoten.email;

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
			s.append(key);
			s.append("=");
			// assume one value per key
			if (values.length > 0)
				s.append(values[0]);
			s.append("||");
		}
		return s.toString();
	}

}
