package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.exceptions.BookShelfLoginException;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.example.web.dto.BookIdToRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/books")
@Scope("request")
public class BookShelfController {

    private Logger logger = Logger.getLogger(BookShelfController.class);
    private BookService bookService;

    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/shelf")
    public String books(Model model){
        logger.info(this.toString());
        model.addAttribute("book", new Book() );
        model.addAttribute("bookIdToRemove", new BookIdToRemove() );
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(@Valid Book book, BindingResult bindingResult, Model model){

        if (bindingResult.hasErrors()){
            model.addAttribute("book", book);
            model.addAttribute("bookIdToRemove", new BookIdToRemove() );
            model.addAttribute("bookList", bookService.getAllBooks());
            logger.info("book not saved: "+book.toString());
            return "book_shelf";
        } else {
            bookService.saveBook(book);
            logger.info("book saved: "+book.toString());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/remove")
    public String removeBook(@Valid BookIdToRemove bookIdToRemove, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            logger.info("book not remove: "+bookIdToRemove);
            model.addAttribute("book", new Book() );
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        } else {
            logger.info("book removed: "+bookIdToRemove);
            bookService.removeBookById(bookIdToRemove.getId());
            return "redirect:/books/shelf";
        }
    }


    @PostMapping("/removeByRegex")
    public String removeByRegex(@RequestParam(value = "queryRegex") String queryRegex){
        bookService.removeByRegex(queryRegex);
        return "redirect:/books/shelf";
    }

    @ExceptionHandler(BookShelfLoginException.class)
    public String handleException(Model model, BookShelfLoginException exception){
        model.addAttribute("errorMessage", exception.getMessage());
        return "errors/404";
    }
}
