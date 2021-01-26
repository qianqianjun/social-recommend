package edu.xjtu.social.controller;

import edu.xjtu.social.dao.PraiseDao;
import edu.xjtu.social.domain.util.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PraiseController {
    @Autowired
    PraiseDao praiseDao;

    @PostMapping("/praised/zan")
    @ResponseBody
    public ResponseInfo praiseShare(
            @RequestParam("account") String account,@RequestParam("shareid") Integer shareid) {
        Integer number=praiseDao.isPraised(shareid,account);
        if(number>0) {
            Long praise_number=praiseDao.getPraisedNumber(shareid);
            return new ResponseInfo("check", true, praise_number);
        }
        Long relationshipId=praiseDao.praisedIt(account,shareid);
        Long praise_number=praiseDao.getPraisedNumber(shareid);
        return new ResponseInfo(relationshipId!=null?"success":"fail",relationshipId!=null,praise_number);
    }

    @PostMapping("/praised/unzan")
    @ResponseBody
    public ResponseInfo unPraiseShare(
            @RequestParam("account") String account,@RequestParam("shareid") Integer shareid) {
        Integer affect_rowes=praiseDao.canclepraised(account,shareid);
        Long praise_number=praiseDao.getPraisedNumber(shareid);
        return new ResponseInfo(affect_rowes>0?"success":"fail",affect_rowes>0,praise_number);
    }
}
