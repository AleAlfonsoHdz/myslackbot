package com.ale.myslackbot;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.users.UsersLookupByEmailResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MySlackBot {
  private static final String TOKEN = System.getenv("SLACK_BOT_TOKEN");
  private static final MethodsClient client = Slack.getInstance().methods(TOKEN);
  private static final Logger logger = LoggerFactory.getLogger(MySlackBot.class);

  static void publishMessage(String id, String text) {
    try {
      ChatPostMessageResponse result = client.chatPostMessage(r -> r
          .channel(id)
          .text(text)
      );
      logger.debug("result {}", result);
      logger.info("Message sent succesfully!");
    } catch (IOException | SlackApiException e) {
      logger.error("error: {}", e.getMessage(), e);
    }
  }

  public static String getUserIdByEmail(String email){
    String userId = "";
    try {
      UsersLookupByEmailResponse result = client.usersLookupByEmail(r -> r
          .email(email));
      logger.debug("result {}", result);
      userId = result.getUser().getId();
    } catch (IOException | SlackApiException e) {
      logger.error("error: {}", e.getMessage(), e);
    }
    return userId;

  }


  public static void main(String[] args){
    String email = args[0];
    String msg = args[1];
    String userId = getUserIdByEmail(email);

    publishMessage(userId, msg);

  }
}