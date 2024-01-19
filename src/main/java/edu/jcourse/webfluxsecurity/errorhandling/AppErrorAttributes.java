package edu.jcourse.webfluxsecurity.errorhandling;

import edu.jcourse.webfluxsecurity.exception.ApiException;
import edu.jcourse.webfluxsecurity.exception.AuthException;
import edu.jcourse.webfluxsecurity.exception.UnauthorizedException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class AppErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        var errorAttributes = super.getErrorAttributes(request, ErrorAttributeOptions.defaults());
        var error = getError(request);

        var errorList = new ArrayList<Map<String, Object>>();

        HttpStatus status;
        if (error instanceof AuthException || error instanceof UnauthorizedException
                || error instanceof ExpiredJwtException || error instanceof SignatureException || error instanceof MalformedJwtException) {
            status = HttpStatus.UNAUTHORIZED;
            var errorMap = new LinkedHashMap<String, Object>();
            errorMap.put("code", ((ApiException) error).getErrorCode());
            errorMap.put("message", error.getMessage());
            errorList.add(errorMap);
        } else if (error instanceof ApiException er) {
            status = HttpStatus.BAD_REQUEST;
            var errorMap = new LinkedHashMap<String, Object>();
            errorMap.put("code", er.getErrorCode());
            errorMap.put("message", error.getMessage());
            errorList.add(errorMap);
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            var message = error.getMessage();
            if (message == null)
                message = error.getClass().getName();

            var errorMap = new LinkedHashMap<String, Object>();
            errorMap.put("code", "INTERNAL_ERROR");
            errorMap.put("message", message);
            errorList.add(errorMap);
        }

        var errors = new HashMap<String, Object>();
        errors.put("errors", errorList);
        errorAttributes.put("status", status.value());
        errorAttributes.put("errors", errors);

        return errorAttributes;
    }
}