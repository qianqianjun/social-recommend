package edu.xjtu.social.controller;

import edu.xjtu.social.dao.FollowDao;
import edu.xjtu.social.dao.HobbyDao;
import edu.xjtu.social.dao.LikeDao;
import edu.xjtu.social.domain.node.User;
import edu.xjtu.social.domain.util.ResponseInfo;
import edu.xjtu.social.domain.node.Hobby;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Map;

@Controller
public class HobbyController {
    public static boolean contains(ArrayList<Hobby> hobbies,Hobby target){
        for (Hobby hobby : hobbies) {
            if (hobby.getId().equals(target.getId()))
                return true;
        }
        return false;
    }
    @Autowired
    HobbyDao hobbyDao;
    @Autowired
    LikeDao likeDao;
    @Autowired
    FollowDao followDao;

    @GetMapping("/hobby/getall")
    public String getAll(HttpServletRequest request, Map<String,Object> map){
        HttpSession session=request.getSession();
        User user=(User) session.getAttribute("user");
        if(user==null) return "login";
        ArrayList<Hobby> hobbies=(ArrayList<Hobby>) hobbyDao.getAllHobbies();
        ArrayList<Hobby> myhobbies=(ArrayList<Hobby>) hobbyDao.getMyHobby(user.getAccount());
        ArrayList<Hobby> res=new ArrayList<>();
        for (Hobby hobby : hobbies) {
            if (contains(myhobbies, hobby)) continue;
            res.add(hobby);
        }
        Integer following_num=followDao.getMyFollowing(user.getAccount()).size();
        Integer follower_num=followDao.getPeopleWhoFollowMe(user.getAccount()).size();
        map.put("myfollowing",following_num);
        map.put("follower",follower_num);
        map.put("hobbies",res);
        map.put("myhobbies",myhobbies);
        map.put("user",user);
        return "hobbys";
    }

    @PostMapping("/hobby/addhobby")
    @ResponseBody
    public ResponseInfo addHobby(@RequestParam("hname") String hname,
                                 @RequestParam("htype") String htype){
        Hobby hobby=hobbyDao.addHobby(hname,htype);
        if(hobby==null){
            return new ResponseInfo("fail",false,null);
        }
        return new ResponseInfo("sucess",true,hobby);
    }

    @PostMapping("/hobby/delete")
    @ResponseBody
    public ResponseInfo deleteHobby(@RequestParam("id") Long id){
        Hobby hobby=hobbyDao.deleteWithId(id);
        return new ResponseInfo("success",true,hobby);
    }

    @GetMapping("/hobby/search")
    @ResponseBody
    public ResponseInfo search(@RequestParam("hname") String hname){
        ArrayList<Hobby> hobbies=(ArrayList<Hobby>) hobbyDao.searchHobbyByName(".*"+hname+".*");
        return new ResponseInfo("",true,hobbies);
    }

    @PostMapping("/hobby/fix")
    @ResponseBody
    public ResponseInfo fix(@RequestParam("id") Long id, @RequestParam("hname") String hname,
                            @RequestParam("htype") String htype){
        Hobby hobby=hobbyDao.fixHobby(id,hname,htype);
        if(hobby!=null){
            return new ResponseInfo("修改成功",true,hobby);
        }
        return new ResponseInfo("修改失败",false,null);
    }
}
