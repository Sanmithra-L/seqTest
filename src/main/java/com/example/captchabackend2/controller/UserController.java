package com.example.captchabackend2.controller;

import cn.apiclub.captcha.Captcha;
import com.example.captchabackend2.entity.CaptchaData;
import com.example.captchabackend2.entity.UserInfo;
import com.example.captchabackend2.entity.UserNotes;
import com.example.captchabackend2.service.CaptchaGenerator;
import com.example.captchabackend2.service.CustomUserDetailService;
import com.example.captchabackend2.service.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@CrossOrigin("*")
@RestController
public class UserController {
    private String permanentCaptcha;
    @Autowired
    NotesService notesService;
    @Autowired
    CustomUserDetailService customUserDetailService;
    CaptchaGenerator captchaGenerator;
    @GetMapping("/generateCaptcha")
    public String generateCaptcha(){
        CaptchaData captchaData=new CaptchaData();
        Captcha captcha= CaptchaGenerator.generateCaptcha(120,40);
        captchaData.setHiddenCaptcha(captcha.getAnswer());
        permanentCaptcha =captcha.getAnswer();
        captchaData.setCaptcha("");
        captchaData.setRealCaptcha(CaptchaGenerator.encodeCaptchatoBinary(captcha));
        return CaptchaGenerator.encodeCaptchatoBinary(captcha);
    }
    @PostMapping("/signup/register")
    public String createUser(@RequestBody UserInfo userInfo){
        if(userInfo.getCaptcha().equals(permanentCaptcha)){
            customUserDetailService.saveUserDetails(userInfo);
            UserInfo temp= customUserDetailService.loadIdbyUsername(userInfo.getUsername());
            return temp.getId().toString();
        }
        else {
             String temp=customUserDetailService.loadUserbyName(userInfo.getUsername());
             if(temp!=null){
                 return "User already exists...";
             }
             else {
                 return null;
             }

        }
    }
    @PostMapping("/addNote")
    public String addNote(@RequestBody UserNotes userNotes){
         notesService.addNote(userNotes);
         return "Note added successfully";
    }
    @GetMapping("/findNote/{note_id}")
    public UserNotes findNote(@PathVariable Integer note_id){
        return notesService.findNote(note_id);
    }
    @PostMapping("/updateNote/{note_id}")
    public String updateNote(@RequestBody UserNotes userNote,@PathVariable Integer note_id){
       return notesService.updateNote(userNote,note_id);

    }
    @DeleteMapping("/deleteNote/{note_id}")
    public String deleteNote(@PathVariable Integer note_id){
        return notesService.deleteNote(note_id);
    }
    @GetMapping("/getNotes/{user_id}")
    public List<UserNotes> findUserNotesbyUserId(@PathVariable Integer user_id){
        return notesService.findUserNotesbyUserId(user_id);
    }
    @GetMapping("/login/{username}/{password}")
    public String loginUser(@PathVariable(name = "username") String username,@PathVariable(name = "password") String password){
        return customUserDetailService.loginUser(username,password);
    }
    @PostMapping("/setComplete/{note_id}")
    public String setCompleteNote(@PathVariable Integer note_id){
        notesService.setCompletedNote(note_id);
        return "Note set to completed";
    }
}
