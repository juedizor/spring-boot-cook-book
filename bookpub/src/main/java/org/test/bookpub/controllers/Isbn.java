package org.test.bookpub.controllers;

public class Isbn {

	private String isbn;

	public Isbn(String isbn) {
		super();
		this.isbn = isbn;
	}

	public Isbn() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the isbn
	 */
	public String getIsbn() {
		return isbn;
	}

	/**
	 * @param isbn
	 *            the isbn to set
	 */
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

}
