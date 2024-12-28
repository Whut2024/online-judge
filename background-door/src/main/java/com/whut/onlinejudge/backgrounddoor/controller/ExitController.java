package com.whut.onlinejudge.backgrounddoor.controller;

import com.whut.onlinejudge.backgrounddoor.common.BaseResponse;
import com.whut.onlinejudge.backgrounddoor.common.ResultUtils;
import com.whut.onlinejudge.backgrounddoor.mail.MailTemplate;
import com.whut.onlinejudge.backgrounddoor.utils.UserHolder;
import com.whut.onlinejudge.common.model.entity.User;
import com.whut.onlinejudge.common.model.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

/**
 * @author liuqiao
 * @since 2024-12-28
 */
@RestController
@RequestMapping("/exit14514")
@Slf4j
@AllArgsConstructor
public class ExitController {

    private final MailTemplate mailTemplate;


    @GetMapping("/exit")
    public BaseResponse<Boolean> over() {
        final User user = UserHolder.get();
        if (!UserRoleEnum.isAdmin(user)) {
            return ResultUtils.success(Boolean.FALSE);
        }

        log.error("an admin whose id is {} executes exiting program", user.getId());

        try {
            mailTemplate.notice("OJ DOOR SERVICE IS STOPPING", "an admin whose id is " + user.getId() + " executes exiting program");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.exit(0);
        }).start();
        return ResultUtils.success(Boolean.TRUE);
    }
}
