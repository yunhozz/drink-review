package drinkreview.global.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null){
            return "세션이 존재하지 않습니다";
        }

        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name={}, value={}", name, session.getAttribute(name)));
        log.info("sessionId={}", session.getId());
        log.info("maxInactiveInterval={}", session.getMaxInactiveInterval());
        log.info("creationTime={}", new Date(session.getCreationTime()));
        log.info("lastAccessedTime={}", new Date(session.getLastAccessedTime()));
        log.info("isNew={}", session.isNew());

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie == null) {
                return "쿠키가 존재하지 않습니다";
            } else {
                log.info("cookieName={}", cookie.getName());
                log.info("cookieValue={}", cookie.getValue());
                log.info("cookieMaxAge={}", cookie.getMaxAge());
            }
        }

        return "세션 출력";
    }
}
