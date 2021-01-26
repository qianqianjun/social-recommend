package edu.xjtu.social.controller;

import edu.xjtu.social.dao.FollowDao;
import edu.xjtu.social.dao.ShareDao;
import edu.xjtu.social.domain.node.Share;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Controller
public class ShareController {
    @Autowired
    ShareDao shareDao;
    @Autowired
    FollowDao followDao;

    @PostMapping("/share/add")
    @ResponseBody
    public ResponseInfo addShare(
            @RequestParam("account") String account, @RequestParam("publisher") String publisher,
            @RequestParam("publisherimg") String publisherimg, @RequestParam("title") String title,
            @RequestParam("content") String content,@RequestParam("related_hobby") String related_hobby,
            @RequestParam("hobbyid") Long hobbyid,
            @RequestParam("imgurl") String imgurl, @RequestParam("address") String address){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=format.format(new Date());
        Share share=shareDao.publishShare(account,publisher,publisherimg,title,content,related_hobby,hobbyid,imgurl,address,time);
        return new ResponseInfo(share!=null?"success":"fail",share!=null,share);
    }

    @PostMapping("/share/delete")
    @ResponseBody
    public ResponseInfo deleteShare(@RequestParam("account") String account,
                                    @RequestParam("shareid") Long shareid){
        Integer affect_rows=shareDao.deleteShareById(account,shareid);
        return new ResponseInfo(affect_rows>0?"success":"fail",affect_rows>0,affect_rows);
    }
    @GetMapping("/share/getbyaccount")
    @ResponseBody
    /**\
     * 这个接口需要更改，动态应该包含点赞数量和评论情况
     */
    public ResponseInfo getShareByAccount(@RequestParam("account") String account){
        ArrayList<Share> shares=(ArrayList<Share>) shareDao.getShareByAccount(account);
        return new ResponseInfo("success",true,shares);
    }

    @GetMapping("/share/publish")
    public String publishShare(HttpServletRequest request,Map<String,Object> map){
        HttpSession session=request.getSession();
        User user=(User) session.getAttribute("user");
        if(user==null)
            return "login";
        map.put("user",user);
        map.put("index","发现动态");
        map.put("title","发布动态");
        return "addshare";
    }

    @GetMapping("/share/friend")
    public String getShareByFriend(HttpServletRequest request, Map<String,Object> map){
        HttpSession session=request.getSession();
        User user=(User) session.getAttribute("user");
        if(user==null)
            return "login";
        Long following_num=followDao.howManyIFollow(user.getAccount());
        Long follower_num=followDao.howManyPeopleFollowMe(user.getAccount());
        session.setAttribute("follower",follower_num);
        session.setAttribute("myfollowing",following_num);

        // 读取动态内容
        ArrayList<Share> shares=shareDao.getFriendShares(user.getAccount());
        map.put("shares",shares);
        map.put("user",user);
        map.put("myfollowing",following_num);
        map.put("follower",follower_num);
        map.put("index","发现动态");
        map.put("title","好友动态");
        return "sharelist";
    }

    @GetMapping("/share/recommend")
    public String getShareByHobby(HttpServletRequest request, Map<String,Object> map){
        HttpSession session=request.getSession();
        User user=(User) session.getAttribute("user");
        if(user==null)
            return "login";
        Long following_num=followDao.howManyIFollow(user.getAccount());
        Long follower_num=followDao.howManyPeopleFollowMe(user.getAccount());
        session.setAttribute("follower",follower_num);
        session.setAttribute("myfollowing",following_num);

        // 读取动态内容
        ArrayList<Share> shares=shareDao.recommendByHobby(user.getAccount());
        map.put("shares",shares);
        map.put("user",user);
        map.put("myfollowing",following_num);
        map.put("follower",follower_num);
        map.put("index","发现动态");
        map.put("title","好友动态");
        return "sharelist";
    }
}
