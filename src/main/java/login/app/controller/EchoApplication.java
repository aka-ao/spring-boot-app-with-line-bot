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

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.AccountLinkEvent;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.IssueLinkTokenResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import login.app.mapper.LineMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.ExecutionException;

@LineMessageHandler
public class EchoApplication {
    private final Logger log = LoggerFactory.getLogger(EchoApplication.class);

    @Value("${line.bot.channel-token}")
    private String token;

    @Value("${server-url}")
    private String serverUrl;

    @Value("${liff-id}")
    private String liffId;

    @Autowired
    private LineMessagingClient lineMessagingClient;

    @Autowired
    private LineMapper mapper;

    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws ExecutionException, InterruptedException {
        Message res;
        switch (event.getMessage().getText()) {
            case "connect":
                String userId = event.getSource().getUserId();
                IssueLinkTokenResponse response = lineMessagingClient.issueLinkToken(userId).join();
                String linkToken = response.getLinkToken();
                String loginUrl = serverUrl + "/login?linkToken=" + linkToken;
                res = new TextMessage(loginUrl);
                break;
            case "liff":
                res = new TextMessage("https://liff.line.me/" + liffId);
                break;
            default:
                res = new TextMessage(event.getMessage().getText());
        }
        return res;
    }

    @EventMapping
    public Message handleAccountLinkEvent(AccountLinkEvent event) {
        String lineId = event.getSource().getUserId();
        String nonce = event.getLink().getNonce();
        mapper.connectUser(lineId, nonce);
        String userName = mapper.getUserNameByLineId(lineId);
        return new TextMessage("userName: " + userName + ", userId(LINE): " + lineId);
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }

}
