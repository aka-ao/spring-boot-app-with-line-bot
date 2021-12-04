package login.app.service;

import login.app.entity.LineAuthResponse;
import login.app.entity.LineProfile;
import login.app.mapper.LineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class LineAuthService {
    @Autowired
    LineMapper lineMapper;

    private RestTemplate restTemplate = new RestTemplate();

    public String authAccessToken(String accessToken) throws URISyntaxException {
        ResponseEntity<LineAuthResponse> auth
                = restTemplate.getForEntity(URI.create("https://api.line.me/oauth2/v2.1/verify?access_token=" + accessToken), LineAuthResponse.class);

        if (auth.getStatusCodeValue() == 200 && auth.getBody().getExpiresIn() > 0 ) {
            HttpHeaders headers= new HttpHeaders();
            headers.setBearerAuth(accessToken);

            RequestEntity requestEntity
                    = RequestEntity
                    .get(new URI("https://api.line.me/v2/profile"))
                    .headers(headers)
                    .build();

            String lineId = restTemplate.exchange(requestEntity, LineProfile.class).getBody().getUserId();

            System.out.println(lineId);
            return lineMapper.getUserNameByLineId(lineId);
        }
        return "";
    }
}
