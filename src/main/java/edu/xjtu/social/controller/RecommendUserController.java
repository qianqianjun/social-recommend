package edu.xjtu.social.controller;
import edu.xjtu.social.dao.FollowDao;
import edu.xjtu.social.dao.RecommendDao;
import edu.xjtu.social.dao.UserDao;
import edu.xjtu.social.domain.node.User;
import edu.xjtu.social.domain.util.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

/**
 * write by qianqianjun
 * 用于实现向用户推荐朋友的功能
 */
@Controller
public class RecommendUserController {
    public static boolean contains(ArrayList<User> arr, User target){
        for (User user : arr) {
            if (user.getId().equals(target.getId()))
                return true;
        }
        return false;
    }
    @Autowired
    UserDao userDao;
    @Autowired
    FollowDao followDao;
    @Autowired
    RecommendDao recommendDao;
    @GetMapping("/recommend/user/byfriend")
    public String recommendFriendsByFriend(HttpServletRequest request, Map<String,Object> map){
        HttpSession session=request.getSession();
        User user =(User) session.getAttribute("user");
        if(user==null){
            return "login";
        }
        ArrayList<User> users=(ArrayList<User>) recommendDao.byFriend(user.getAccount());
        ArrayList<User> myfollowing=(ArrayList<User>) followDao.getMyFollowing(user.getAccount());
        HashSet<User> res=new HashSet<>();
        for (User value : users) {
            if (value.getAccount().equals(user.getAccount()))
                continue;
            if (contains(myfollowing, value)) continue;
            res.add(value);
        }
        Integer following_num=followDao.getMyFollowing(user.getAccount()).size();
        Integer follower_num=followDao.getPeopleWhoFollowMe(user.getAccount()).size();
        map.put("myfollowing",following_num);
        map.put("follower",follower_num);
        map.put("recommends",res);
        map.put("user",user);
        map.put("index","朋友推荐");
        map.put("title","可能认识的人");
        return "userlist";
    }
    @GetMapping("/recommend/user/byshare")
    public String recommendFriendsByShare(HttpServletRequest request, Map<String,Object> map) {
        HttpSession session=request.getSession();
        User user =(User) session.getAttribute("user");
        if(user==null){
            return "login";
        }
        ArrayList<User> users=(ArrayList<User>) recommendDao.byshare(user.getAccount());
        ArrayList<User> myfollowing=(ArrayList<User>) followDao.getMyFollowing(user.getAccount());
        HashSet<User> res=new HashSet<>();
        for (User value : users) {
            if (value.getAccount().equals(user.getAccount()))
                continue;
            if (contains(myfollowing, value)) continue;
            res.add(value);
        }
        Integer following_num=followDao.getMyFollowing(user.getAccount()).size();
        Integer follower_num=followDao.getPeopleWhoFollowMe(user.getAccount()).size();
        map.put("myfollowing",following_num);
        map.put("follower",follower_num);
        map.put("recommends",res);
        map.put("user",user);
        map.put("index","朋友推荐");
        map.put("title","最常互动的人");
        return "userlist";
    }

    @GetMapping("/recommend/user/byhobby")
    public String recommendFriendsByHobby(HttpServletRequest request, Map<String,Object> map){
        HttpSession session=request.getSession();
        User user =(User) session.getAttribute("user");
        if(user==null){
            return "login";
        }
        ArrayList<User> users=(ArrayList<User>) recommendDao.byHobby(user.getAccount());
        ArrayList<User> myfollowing=(ArrayList<User>) followDao.getMyFollowing(user.getAccount());
        HashSet<User> res=new HashSet<>();
        for (User value : users) {
            if (value.getAccount().equals(user.getAccount()))
                continue;
            if (contains(myfollowing, value)) continue;
            res.add(value);
        }
        Integer following_num=followDao.getMyFollowing(user.getAccount()).size();
        Integer follower_num=followDao.getPeopleWhoFollowMe(user.getAccount()).size();
        map.put("myfollowing",following_num);
        map.put("follower",follower_num);
        map.put("recommends",res);
        map.put("user",user);
        map.put("index","朋友推荐");
        map.put("title","趣味相投的人");
        map.put("reverse",false);
        return "userlist";
    }
}
