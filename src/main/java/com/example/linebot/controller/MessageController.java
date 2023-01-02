package com.example.linebot.controller;

import com.example.linebot.model.request.MessageToReq;
import com.example.linebot.model.request.UserReq;
import com.example.linebot.model.respone.CommonRes;
import com.example.linebot.services.LineApi;
import com.example.linebot.services.MessageService;
import com.linecorp.bot.model.response.BotApiResponse;
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
@RequestMapping("/message")
@Tag(name = "訊息")
public class MessageController {

    private final Logger log = LoggerFactory.getLogger(MessageController.class);

    LineApi lineApi;
    MessageService messageService;

    public MessageController(LineApi lineApi, MessageService messageService) {
        this.lineApi = lineApi;
        this.messageService = messageService;
    }

    @PostMapping("/findMessageByUserId")
    @Operation(summary = "透過用戶ID取得用戶傳送的文字訊息", description = "只需輸入用戶ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "999", description = "Failed",
                    content = {@Content()}),
            @ApiResponse(responseCode = "000", description = "Success",
                    content = {@Content()})
    })
    public ResponseEntity findMessageByUserId(@RequestBody UserReq req) {
        String userId = req.getUserId();
        try {
            List<String> texts = messageService.findAllByUserId(userId);
            ResponseEntity<CommonRes> res = new ResponseEntity<CommonRes>(
                    new CommonRes<List>().SucceesCommonRes(texts),
                    HttpStatus.OK);
            return res;
        } catch (Exception e) {
            log.error("findMessageByUserId Failed", e);
            return new ResponseEntity<CommonRes>(new CommonRes().ErrorCommonRes(), HttpStatus.OK);
        }
    }

    @PostMapping("/pushMessageTo")
    @Operation(summary = "透過用戶ID發送給用戶文字訊息", description = "輸入用戶ID及訊息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "999", description = "Failed",
                    content = {@Content()}),
            @ApiResponse(responseCode = "000", description = "Success",
                    content = {@Content()})
    })
    public ResponseEntity pushMessageTo(@RequestBody MessageToReq req) {
        String to = req.getTo();
        String message = req.getMessage();
        try {
            BotApiResponse botApiResponse = lineApi.pushMessageToUser(to, message);
            ResponseEntity<CommonRes> res = new ResponseEntity<CommonRes>(
                    new CommonRes<BotApiResponse>().SucceesCommonRes(botApiResponse),
                    HttpStatus.OK);
            return res;
        } catch (Exception e) {
            log.error("pushMessageTo Failed", e);
            return new ResponseEntity<CommonRes>(new CommonRes().ErrorCommonRes(), HttpStatus.OK);
        }
    }

}
