package org.test.bookpub;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.test.bookpub.entity.Author;
import org.test.bookpub.entity.Book;
import org.test.bookpub.entity.Publisher;
import org.test.bookpub.repository.AuthorRepository;
import org.test.bookpub.repository.BookRepository;
import org.test.bookpub.repository.PublisherRepository;

public class StartupRunner implements CommandLineRunner {

	protected final Log LOGGER = LogFactory.getLog(getClass());
	@Autowired
	private DataSource ds;
	@Autowired
	BookRepository bookRepository;
	@Autowired
	AuthorRepository authorRepository;
	@Autowired
	PublisherRepository publisherReposiory;

	@Override
	public void run(String... arg0) throws Exception {
		// TODO Auto-generated method stub
		LOGGER.info("Hello");
		LOGGER.info("Datasource: " + ds.toString());
		LOGGER.info("Number of books: " + bookRepository.count());
		Author author = new Author("Julio", "Izquierdo");
		author = authorRepository.save(author);
		Publisher publisher = new Publisher("Packt");
		publisher = publisherReposiory.save(publisher);
		Book book = new Book("978-1-78528-415-1", "Spring Boot Recipe", author, publisher);
		bookRepository.save(book);

	}

	@Scheduled(initialDelay = 1000, fixedRate = 10000)
	public void run() {
		// TODO Auto-generated method stub
		// LOGGER.info("Number of books: " + bookRepository.count());
	}

}
