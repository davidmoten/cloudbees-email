package com.github.davidmoten.flashq;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 3525431883928567774L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (req.getParameter("uid") == null)
			throw new ServletException("uid parameter not specified");
		String userId = req.getParameter("uid").trim();
		resp.setContentType("text/plain");
		Util.setNoCache(resp);
		resp.getWriter().write(
				"uid=" + URLEncoder.encode(userId, "UTF-8") + "&status=1");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
