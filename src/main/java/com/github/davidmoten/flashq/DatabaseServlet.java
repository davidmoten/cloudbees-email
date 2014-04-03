package com.github.davidmoten.flashq;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.functions.Action1;

import com.github.davidmoten.rx.jdbc.ConnectionProvider;
import com.github.davidmoten.rx.jdbc.Database;

public class DatabaseServlet extends HttpServlet {

	private static final long serialVersionUID = -4137504003076597022L;

	private static final Logger log = LoggerFactory
			.getLogger(DatabaseServlet.class);
	private Database db;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		db = Database.from(cp("java:comp/env/jdbc/flashq"));
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info("params=" + Parameters.toString(req));
		Util.setNoCache(resp);
		if ("true".equals(req.getParameter("list")))
			list(resp);
		else
			writeToDatabase(req, resp);
	}

	private void list(HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		final PrintWriter out = resp.getWriter();

		db.select("select id, text from flashq.entry order by id")
		// automap each row into an Entry
				.autoMap(Entry.class)
				// print each entry
				.doOnNext(new Action1<Entry>() {
					@Override
					public void call(Entry entry) {
						out.println(entry.getId() + "||" + entry.getText());
					}
				})
				// count the number of entries
				.count()
				// block and return result
				.toBlockingObservable().single();
	}

	private void writeToDatabase(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		String text = Parameters.toString(req);
		int count = db.update("insert into flashq.entry(text) values(?)")
		// set text parameter
				.parameter(text)
				// get num records inserted
				.count()
				// block and return
				.toBlockingObservable().single();
		System.out.println(count + " records inserted");
		resp.setContentType("text/plain");
		resp.getWriter().write("sid=somebody&status=1");
	}

	private static ConnectionProvider cp(final String jndiResource) {
		return new ConnectionProvider() {

			@Override
			public Connection get() {
				return getConnection(jndiResource);
			}

			@Override
			public void close() {
				// do nothing
			}
		};
	}

	private static Connection getConnection(String resource) {
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(resource);
			return ds.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

}
