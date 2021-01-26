package edu.xjtu.social.controller;

import edu.xjtu.social.dao.FollowDao;
import edu.xjtu.social.dao.RecommendDao;
import edu.xjtu.social.dao.ShareDao;
import edu.xjtu.social.dao.UserDao;
import edu.xjtu.social.domain.node.Share;
import edu.xjtu.social.domain.util.ResponseInfo;
import edu.xjtu.social.domain.node.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    UserDao userDao;
    @Autowired
    FollowDao followDao;
    @Autowired
    ShareDao shareDao;
    @Autowired
    RecommendDao recommendDao;
    public static HashSet<User> subList(ArrayList<User> list, int length,String account){
        HashSet<User> res=new HashSet<>();
        int range=Math.min(list.size(),length);
        for(int i=0;i<range;i++){
            if(list.get(i).getAccount().equals(account)) continue;
            res.add(list.get(i));
        }
        return res;
    }
    @GetMapping("/")
    public String index(Map<String,Object> paramMap,HttpServletRequest request){
        HttpSession session=request.getSession();
        User user=(User) session.getAttribute("user");
        if(user==null){
            return "login";
        }else{
            return "redirect:/content";
        }
    }
    @PostMapping("/login")
    public String index(@RequestParam("account") String account,
                        @RequestParam("password") String password,
                        Map<String,Object> paramMap,
                        HttpServletRequest request){
        User user=userDao.checkUser(account,password);
        if(user==null){
            paramMap.put("msg","用户名或者密码错误");
            return "login";
        }else{
            HttpSession session=request.getSession();
            session.setAttribute("user",user);
            return "redirect:content";
        }
    }
    @GetMapping("/content")
    public String content(HttpServletRequest request,Map<String,Object> paramMap){
        HttpSession session=request.getSession();
        User user=(User) session.getAttribute("user");
        if(user==null)
            return "login";
        Long following_num=followDao.howManyIFollow(user.getAccount());
        Long follower_num=followDao.howManyPeopleFollowMe(user.getAccount());
        // 读取动态内容
        ArrayList<Share> shares=shareDao.getFriendShares(user.getAccount());
        // 读取推荐用户列表
        ArrayList<User> byfriend=(ArrayList<User>) recommendDao.byFriend(user.getAccount());
        ArrayList<User> byshare=(ArrayList<User>) recommendDao.byshare(user.getAccount());
        ArrayList<User> byhobby=(ArrayList<User>) recommendDao.byHobby(user.getAccount());

        paramMap.put("byfriend",subList(byfriend,3,user.getAccount()));
        paramMap.put("byshare",subList(byshare,3,user.getAccount()));
        paramMap.put("byhobby",subList(byhobby,3,user.getAccount()));

        paramMap.put("shares",shares);
        paramMap.put("user",user);
        paramMap.put("myfollowing",following_num);
        paramMap.put("follower",follower_num);
        return "content";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session=request.getSession();
        session.setAttribute("user",null);
        return "login";
    }
    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/user/getuser")
    @ResponseBody
    public ResponseInfo getUser(@RequestParam("account") String account){
        User user=userDao.getUserByAccount(account);
        if(user!=null){
            return new ResponseInfo("1",true,user);
        }
        return new ResponseInfo("0",true,null);
    }

    @GetMapping("/user/all")
    @ResponseBody
    public ResponseInfo getAllUser(){
        ArrayList<User> users=(ArrayList<User>) userDao.getAllUser();
        return new ResponseInfo(Integer.toString(users.size()),true,users);
    }

    @PostMapping("/user/adduser")
    public String addUser(@RequestParam("account") String account,
                                @RequestParam("password") String password,
                                @RequestParam("age") Integer age,
                                @RequestParam("gender") String gender,
                                @RequestParam("email") String email,
                                @RequestParam("address") String address,
                                @RequestParam("nickname") String nickname,
                          Map<String,Object> map){
        User user=userDao.getUserByAccount(account);
        if(user!=null){
            map.put("msg","该账号已经存在，请登录");
            return "login";
        }
        user=userDao.adduser(account,password,nickname,age,gender,email,address);
        if(user!=null) {
            return "redirect:/content";
        }
        map.put("msg","注册失败，请联系管理员");
        return "register";
    }

    @PostMapping("/user/deleteuser")
    @ResponseBody
    public ResponseInfo deleteUser(@RequestParam("account") String account){
       User user=userDao.deleteUserByAccount(account);
       return new ResponseInfo("删除成功",true,user);
    }

    @PostMapping("/user/fixinfo")
    @ResponseBody
    public ResponseInfo fixInfo(@RequestParam("account") String account,@RequestParam("nickname") String nickname,
                                @RequestParam("age") Integer age,@RequestParam("email") String email,
                                @RequestParam("address") String address){
        Long num=userDao.fixInfo(account,nickname,age,address,email);
        return new ResponseInfo(num>0?"success":"fail",num>0,num);
    }

    @PostMapping("/user/fixpass")
    @ResponseBody
    public ResponseInfo fixPass(@RequestParam("account") String account,@RequestParam("newpass") String password){
        Long num=userDao.fixPass(account,password);
        return new ResponseInfo(num>0?"success":"fail",num>0,num);
    }

    @PostMapping("/user/fiximg")
    @ResponseBody
    public ResponseInfo fixImg(@RequestParam("account") String account,@RequestParam("imgurl") String imgurl){
        Long num=userDao.fiximg(account,imgurl);
        return new ResponseInfo(num>0?"success":"fail",num>0,num);
    }
}
