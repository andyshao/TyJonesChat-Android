package edu.msu.cse.jonesty3.tyjoneschat;

/**
 * Created by Ty on 7/30/2015.
 */
public class SignalRMessage {
   private String Hub;
   private String Method;
   private ChatMessage ChatMessage;

   public ChatMessage getChatMessage() {
      return ChatMessage;
   }

   public void setChatMessage( ChatMessage chatMessage ) {
      ChatMessage = chatMessage;
   }

   public String getHub() {
      return Hub;
   }

   public void setHub( String hub ) {
      Hub = hub;
   }

   public String getMethod() {
      return Method;
   }

   public void setMethod( String method ) {
      Method = method;
   }
}
