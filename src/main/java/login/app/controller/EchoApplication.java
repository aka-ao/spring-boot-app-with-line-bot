/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package login.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.AccountLinkEvent;
import com.linecorp.bot.model.event.CallbackRequest;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.IssueLinkTokenResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import login.app.service.LineRequestHeaderVerifyService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.springframework.data.repository.init.ResourceReader.Type.JSON;

@RestController
@RequestMapping("/callback")
public class EchoApplication {
    private final Logger log = LoggerFactory.getLogger(EchoApplication.class);

    @Autowired
    LineRequestHeaderVerifyService service;

    @PostMapping
    public ResponseEntity<Object> receiveLineEvent(@RequestBody String eventJson, @RequestHeader(name="x-line-signature", required=true) String xLineSignature) {
        ResponseEntity res = null;
        try {
            if(service.verifyLineRequestHeader(eventJson, xLineSignature)) {
                log.info("****** signature verify: true");
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(eventJson);

                log.info(node.path("events").toString());

                TextMessage message = new TextMessage("test");
                ReplyMessage replyMessage = new ReplyMessage(node.get("events").get(0).get("replyToken").asText(), message);
                res = new ResponseEntity(replyMessage, HttpStatus.OK);
            } else {
                log.info("****** signature verify: false");
                res = new ResponseEntity("bad request", HttpStatus.BAD_REQUEST);
            }
        } catch (JsonProcessingException|NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            log.warn(e.getClass().getName(), e);
            res = new ResponseEntity("bad request", HttpStatus.BAD_REQUEST);
        }

        return res;
    }
}
