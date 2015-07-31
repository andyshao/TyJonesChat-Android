package edu.msu.cse.jonesty3.tyjoneschat;

/**
 * Created by Ty on 7/30/2015.
 */
public class ChatMessage {
   private String Name;
   private String Message;

   public ChatMessage( String name, String message ) {
      Name = name;
      Message = message;
   }

   public String getMessage() {
      return Message;
   }

   public String getName() {
      return Name;
   }
}
