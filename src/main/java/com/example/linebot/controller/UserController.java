package com.example.linebot.controller;

import com.example.linebot.model.entity.UserProfileDao;
import com.example.linebot.model.repository.UserProflieRepository;
import com.example.linebot.model.request.UserReq;
import com.example.linebot.model.respone.CommonRes;
import com.example.linebot.model.respone.UserProfileRes;
import com.example.linebot.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin("*")
@RequestMapping("/user")
@Tag(name = "用戶")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    UserProflieRepository userProflieRepository;
    UserService userService;

    public UserController(UserProflieRepository userProflieRepository, UserService userService) {
        this.userProflieRepository = userProflieRepository;
        this.userService = userService;
    }

    @PostMapping("/getUserPorfile")
    @Operation(summary = "透過用戶ID取得用戶資訊", description = "只需輸入用戶ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "999", description = "Failed",
                    content = {@Content()}),
            @ApiResponse(responseCode = "000", description = "Success",
                    content = {@Content()})
    })
    public ResponseEntity getUserPorfileByID(@RequestBody UserReq req) {
        String userId = req.getUserId();
        try {
            UserProfileDao userProfileDao = userProflieRepository
                    .findById(userId).get();
            ResponseEntity<CommonRes> res = new ResponseEntity<CommonRes>(
                    new CommonRes<UserProfileRes>().SucceesCommonRes(new UserProfileRes(userProfileDao)),
                    HttpStatus.OK);
            return res;
        } catch (Exception e) {
            log.error("getUserPorfileByID Failed", e);
            return new ResponseEntity<CommonRes>(new CommonRes().ErrorCommonRes(), HttpStatus.OK);
        }
    }

    @PostMapping("/findUserIDByName")
    @Operation(summary = "透過用戶名稱取得用戶ID", description = "只需輸入用戶名稱")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "999", description = "Failed",
                    content = {@Content()}),
            @ApiResponse(responseCode = "000", description = "Success",
                    content = {@Content()})
    })
    public ResponseEntity findUserIDByName(@RequestBody UserReq req) {
        String displayName = req.getDisplayName();
        try {
            List<String> ids = userService.findUserIdByName(displayName);
            ResponseEntity<CommonRes> res = new ResponseEntity<CommonRes>(
                    new CommonRes<List>().SucceesCommonRes(ids),
                    HttpStatus.OK);
            return res;
        } catch (Exception e) {
            log.error("findUserIDByName Failed", e);
            return new ResponseEntity<CommonRes>(new CommonRes().ErrorCommonRes(), HttpStatus.OK);
        }
    }

}
