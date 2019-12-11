package com.example.book_manager.utils;


import com.example.book_manager.model.User;

/**
 * Created by nowcoder on 2018/08/07 下午3:58
 */
public class ConcurrentUtils {

  private static ThreadLocal<User> host = new ThreadLocal<>();

  public static User getHost(){
    return host.get();
  }

  public static void setHost(User user){
    host.set(user);
  }

}
