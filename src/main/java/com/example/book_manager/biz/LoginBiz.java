package com.example.book_manager.biz;

import com.example.book_manager.model.Ticket;
import com.example.book_manager.model.User;
import com.example.book_manager.model.exceptions.LoginRegisterException;
import com.example.book_manager.service.TicketService;
import com.example.book_manager.service.UserService;
import com.example.book_manager.utils.ConcurrentUtils;
import com.example.book_manager.utils.MD5;
import com.example.book_manager.utils.TicketUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LoginBiz {
    @Autowired
    private UserService userService;

    @Autowired
    private TicketService ticketService;

    /**
     * 登陆逻辑，先检查邮箱和密码，然后更新t票
     * @param email
     * @param password
     * @return 返回最新t票
     * @throws Exception 账号密码错误
     */
    public String login(String email,String password) throws Exception{
        User user=userService.getUser(email);
        //登录信息检查
        if(user == null)
            throw new LoginRegisterException("邮箱不存在");
        if(!StringUtils.equals(MD5.next(password),user.getPassword()))
            throw new LoginRegisterException("密码不正确");

        //检查ticket
        Ticket t=ticketService.getTicket(user.getId());
        //如果没有t票，生成一个
        if(t == null){
            t= TicketUtils.next(user.getId());
            ticketService.addTicket(t);
            return t.getTicket();
        }
        //是否过期
        if(t.getExpiredAt().before(new Date())){
            //删除
            ticketService.deleteTicket(t.getId());
        }
        t=TicketUtils.next(user.getId());
        ticketService.addTicket(t);

        ConcurrentUtils.setHost(user);
        return t.getTicket();
    }

    /**
     * 用户退出登陆，只需要删除数据库中用户的t票
     * @param t
     */
    public void logout(String t){
        ticketService.deleteTicket(t);
    }

    /**
     * 注册一个用户，并返回用户t票
     * @param user
     * @return
     * @throws Exception
     */
    public String register(User user) throws Exception{
        //信息检查
        if(userService.getUser(user.getEmail())!=null){
            throw new LoginRegisterException("用户邮箱已存在！");
        }
        //密码加密
        String plain=user.getPassword();
        String md5=MD5.next(plain);
        user.setPassword(md5);
        //数据库添加用户
        userService.addUser(user);
        //生成用户t票
        Ticket ticket=TicketUtils.next(user.getId());
        //数据库添加t票
        ticketService.addTicket(ticket);

        ConcurrentUtils.setHost(user);
        return ticket.getTicket();

    }
}
