package org.test.bookpub.formatters;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.test.bookpub.entity.Book;
import org.test.bookpub.repository.BookRepository;

public class BookFormatter implements Formatter<Book> {

	private BookRepository repository;

	public BookFormatter(BookRepository bookRepository) {
		// TODO Auto-generated constructor stub
		this.repository = bookRepository;
	}

	@Override
	public String print(Book object, Locale locale) {
		// TODO Auto-generated method stub
		return object.getIsbn();
	}

	@Override
	public Book parse(String text, Locale locale) throws ParseException {
		// TODO Auto-generated method stub
		Book book = repository.findBookByIsbn(text);
		return book != null ? book : repository.findOne(Long.valueOf(text));
	}

}
