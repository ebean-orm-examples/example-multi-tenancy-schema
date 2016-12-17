package org.example.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Content extends BaseDomain {

  String title;
  String byline;
  String body;
  
  @JoinColumn(name="author",referencedColumnName="id")
  @ManyToOne
  public Author author;

  public String toString() {
    return "id:" + getId() + " title:" + getTitle();
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getByline() {
    return byline;
  }

  public void setByline(String byline) {
    this.byline = byline;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

public Author getAuthor() {
	return author;
}

public void setAuthor(Author author) {
	this.author = author;
}
  
}
