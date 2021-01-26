package edu.xjtu.social.controller;

import edu.xjtu.social.dao.FollowDao;
import edu.xjtu.social.dao.UserDao;
import edu.xjtu.social.domain.node.User;
import edu.xjtu.social.domain.util.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

@Controller
public class FollowController {
    public static boolean contains(ArrayList<User> arr, User target){
        for (User user : arr) {
            if (user.getId().equals(target.getId()))
                return true;
        }
        return false;
    }
    @Autowired
    FollowDao followDao;
    @Autowired
    UserDao userDao;

    @PostMapping("/follow/followit")
    @ResponseBody
    public ResponseInfo followIt(@RequestParam("follower") String follower,
                                 @RequestParam("following") String following){
        Long id=followDao.createFollow(follower,following);
        if(id!=null){
            return new ResponseInfo("success",true,id);
        }
        return new ResponseInfo("fail",false,null);
    }

    @PostMapping("/follow/unfollow")
    @ResponseBody
    public ResponseInfo unfollow(@RequestParam("follower") String follower,
                                 @RequestParam("following") String following){
        Long num=followDao.deleteRelation(follower,following);
        if(num==0){
            return new ResponseInfo("fail",false,num);
        }
        return new ResponseInfo("success",true,num);
    }

    @GetMapping("/user/follower")
    public String getFans(HttpServletRequest request, Map<String,Object> map) {
        HttpSession session=request.getSession();
        User user =(User) session.getAttribute("user");
        if(user==null){
            return "login";
        }
        ArrayList<User> follower=(ArrayList<User>) followDao.getPeopleWhoFollowMe(user.getAccount());
        ArrayList<User> myfollowing=(ArrayList<User>) followDao.getMyFollowing(user.getAccount());
        HashSet<User> recommends=new HashSet<>();
        HashSet<User> subFollowing=new HashSet<>();
        for (User value : follower) {
            if (contains(myfollowing, value)) {
                subFollowing.add(value);
                continue;
            }
            recommends.add(value);
        }
        Integer following_num=followDao.getMyFollowing(user.getAccount()).size();
        Integer follower_num=followDao.getPeopleWhoFollowMe(user.getAccount()).size();
        map.put("myfollowing",following_num);
        map.put("follower",follower_num);
        map.put("recommends",recommends);
        map.put("mystar",subFollowing);
        map.put("user",user);
        map.put("title","关注我的人");
        map.put("index","首页");
        map.put("hascategory",true);
        return "userlist";
    }

    @GetMapping("/user/myfollowing")
    public String getMyFollowing(HttpServletRequest request, Map<String,Object> map){
        HttpSession session=request.getSession();
        User user =(User) session.getAttribute("user");
        if(user==null){
            return "login";
        }
        ArrayList<User> following=(ArrayList<User>) followDao.getMyFollowing(user.getAccount());
        Integer following_num=followDao.getMyFollowing(user.getAccount()).size();
        Integer follower_num=followDao.getPeopleWhoFollowMe(user.getAccount()).size();
        map.put("myfollowing",following_num);
        map.put("follower",follower_num);
        map.put("recommends",following);
        map.put("user",user);
        map.put("title","我关注的人");
        map.put("index","首页");
        return "myfollowing";
    }
}
