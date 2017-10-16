package org.test.bookpub.controllers;

import java.beans.PropertyEditorSupport;

import org.springframework.util.StringUtils;

public class IsbnEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		if (StringUtils.hasText(text)) {
			setValue(new Isbn(text));
		} else {
			setValue(null);
		}
	}

	@Override
	public String getAsText() {
		// TODO Auto-generated method stub
		Isbn isbn = (Isbn) getValue();
		if (isbn != null) {
			return isbn.getIsbn();
		}

		return "";
	}

	
	
}
