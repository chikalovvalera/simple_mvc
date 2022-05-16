package org.example.web.dto;

import lombok.Data;

import javax.validation.constraints.Digits;

@Data
public class Book {
    private String id;
    private String author;
    private String title;
    @Digits(integer = 4, fraction = 0)
    private Integer size;

    public boolean isCorrect(){
        return (author != null && author.length() >0) || (title != null && title.length() > 0) || (size != null);
    }
}
