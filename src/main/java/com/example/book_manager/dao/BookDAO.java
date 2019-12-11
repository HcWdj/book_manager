package com.example.book_manager.dao;


import java.util.List;

import com.example.book_manager.model.Book;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface BookDAO {

  String table_name = " book ";
  String insert_field = " name, author, price ";
  String select_field = " id, status, " + insert_field;

  @Select({"select", select_field, "from", table_name})
  List<Book> selectAll();

  @Insert({"insert into", table_name, "(", insert_field,") values (#{name},#{author},#{price})"})
  int addBook(Book book);

  @Update({"update ", table_name, " set status=#{status} where id=#{id}"})
  void updateBookStatus(@Param("id") int id, @Param("status") int status);
}
