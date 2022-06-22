package cz.zcu.fav.kiv.antipatterndetectionapp.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomErrorController.class);


    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {

        String errorPage = "error";

        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (statusCode != null) {
            int code = Integer.parseInt(statusCode.toString());

            if (code == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error/500";
            } else if (code == HttpStatus.NOT_FOUND.value()) {
                return "error/404";
            } else if (code == HttpStatus.UNAUTHORIZED.value()) {
                return "error/403";
            }
        }

        return errorPage;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}
