package org.test.bookpub;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.test.bookpub.repository.BookRepository;

public class StartupRunner implements CommandLineRunner {

	protected final Log LOGGER = LogFactory.getLog(getClass());
	@Autowired
	private DataSource ds;
	@Autowired
	BookRepository bookRepository;

	@Override
	public void run(String... arg0) throws Exception {
		// TODO Auto-generated method stub
		LOGGER.info("Hello");
		LOGGER.info("Datasource: " + ds.toString());
		LOGGER.info("Number of books: " + bookRepository.count());
	}
	@Scheduled (initialDelay = 1000, fixedRate = 10000)
	public void run() {
		// TODO Auto-generated method stub
		LOGGER.info("Number of books: " + bookRepository.count());
	}

}
