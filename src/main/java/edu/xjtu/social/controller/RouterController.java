package edu.xjtu.social.controller;
import edu.xjtu.social.dao.FollowDao;
import edu.xjtu.social.domain.node.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 用于返回界面信息
 * write by qianqianjun
 */
@Controller
public class RouterController {
    @Autowired
    FollowDao followDao;
    @GetMapping("/user/personinfo")
    public String personInfo(HttpServletRequest request, Map<String,Object> map){
        HttpSession session=request.getSession();
        User user=(User) session.getAttribute("user");
        if(user==null){
            return "login";
        }
        map.put("user",user);
        Integer following_num=followDao.getMyFollowing(user.getAccount()).size();
        Integer follower_num=followDao.getPeopleWhoFollowMe(user.getAccount()).size();
        map.put("myfollowing",following_num);
        map.put("follower",follower_num);
        return "personinfo";
    }
}
