package com.example.book_manager.service;



import java.util.List;

import com.example.book_manager.dao.BookDAO;
import com.example.book_manager.model.Book;
import com.example.book_manager.model.enums.BookStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by nowcoder on 2018/08/04 下午3:41
 */
@Service
public class BookService {

  @Autowired
  private BookDAO bookDAO;

  public List<Book> getAllBooks() {
    return bookDAO.selectAll();
  }

  public int addBooks(Book book) {
    return bookDAO.addBook(book);
  }

  public void deleteBooks(int id) {
    bookDAO.updateBookStatus(id, BookStatusEnum.DELETE.getValue());
  }

  public void recoverBooks(int id) {
    bookDAO.updateBookStatus(id, BookStatusEnum.NORMAL.getValue());
  }
}
