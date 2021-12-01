package login.app.controller;

import login.app.service.LineAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
public class HelloApiController {

    @Autowired
    LineAuthService authService;

    @GetMapping("/liff/getLineId")
    public ResponseEntity<String> getLineId(@RequestHeader("Authorization")String token) throws URISyntaxException {
        String lineId = authService.authAccessToken(token.substring(7));
        return new ResponseEntity<String>(lineId, HttpStatus.OK);
    }
}
