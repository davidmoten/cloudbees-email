package com.github.davidmoten.email;

public class Entry {
	private final long id;
	private final String text;

	public Entry(Long id, String text) {
		this.id = id;
		this.text = text;
	}

	public long getId() {
		return id;
	}

	public String getText() {
		return text;
	}

}
