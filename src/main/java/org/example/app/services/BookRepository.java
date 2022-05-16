package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Repository
public class BookRepository<T> implements  ProjectRepository<Book>, ApplicationContextAware {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final List<Book> repo = new ArrayList<>();
    private ApplicationContext applicationContext;
    @Override
    public List<Book> retreiveAll() {
        return new ArrayList<Book>(repo);
    }

    @Override
    public void store(Book book) {
        book.setId(applicationContext.getBean(IdProvider.class).provideId(book));
        logger.info("store new book: "+book);
        repo.add(book);
    }

    @Override
    public boolean removeItemById(String bookIdToRemove) {
        for (Book book : retreiveAll()) {
            if (book.getId().equals(bookIdToRemove)) {
                logger.info("remove book completed: " + book);
                return repo.remove(book);
            }
        }
        logger.info("remove book not completed");
        return false;
    }

    @Override
    public void removeByRegex(String queryRegex) {
        logger.info("remove books by regex: "+queryRegex);
        if (queryRegex != null && !queryRegex.equals("")) {
            Pattern pattern = Pattern.compile(queryRegex);
            repo.removeIf(book -> ((pattern.matcher(book.getAuthor()).find()) || (pattern.matcher(book.getTitle()).find()) || (book.getSize() != null && pattern.matcher(book.getSize().toString()).find())));
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void defaultInit(){
        logger.info("provide INIT in repo book");
    }

    private void defaultDestroy(){
        logger.info("provide DESTROY in repo book");
    }
}
