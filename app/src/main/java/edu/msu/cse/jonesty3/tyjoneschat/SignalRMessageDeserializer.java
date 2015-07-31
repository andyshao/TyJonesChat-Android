package edu.msu.cse.jonesty3.tyjoneschat;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;

/**
 * Created by Ty on 7/30/2015.
 */
public class SignalRMessageDeserializer implements JsonDeserializer<SignalRMessage> {
   @Override
   public SignalRMessage deserialize( JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext ) throws JsonParseException {
      final JsonObject jsonObject = jsonElement.getAsJsonObject();

      final JsonArray jsonAArray = jsonObject.get( "A" ).getAsJsonArray();
      JsonParser parser = new JsonParser();
      final JsonElement jsonChatMessageElement = parser.parse( jsonAArray.get( 0 ).getAsString() );
      final JsonObject jsonChatMessage = jsonChatMessageElement.getAsJsonObject();
      final String name = jsonChatMessage.get( "Name" ).getAsString();
      final String message = jsonChatMessage.get( "Message" ).getAsString();
      final ChatMessage chatMessage = new ChatMessage( name, message );

      final String hub = jsonObject.get( "H" ).getAsString();

      final String method = jsonObject.get( "M" ).getAsString();

      final SignalRMessage signalRMessage = new SignalRMessage();
      signalRMessage.setChatMessage( chatMessage );
      signalRMessage.setHub( hub );
      signalRMessage.setMethod( method );

      return signalRMessage;
   }
}
