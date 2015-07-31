package edu.msu.cse.jonesty3.tyjoneschat;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.ErrorCallback;
import microsoft.aspnet.signalr.client.MessageReceivedHandler;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;


public class MainActivity extends ActionBarActivity {

   private HubProxy _hub;
   private HubConnection _connection;
   private final GsonBuilder _gsonBuilder = new GsonBuilder();

   @Override
   protected void onCreate( Bundle savedInstanceState ) {
      super.onCreate( savedInstanceState );
      setContentView( R.layout.activity_main );
      Platform.loadPlatformComponent( new AndroidPlatformComponent() );

      _gsonBuilder.registerTypeAdapter( SignalRMessage.class, new SignalRMessageDeserializer() );

      _connection = new HubConnection( "http://tyjoneschat.azurewebsites.net/" );
      _hub = _connection.createHubProxy( "TyJonesChatHub" );

      _connection.error( new ErrorCallback() {
         @Override
         public void onError( Throwable throwable ) {
            Log.i( "TyJonesChat", "Error " + throwable.getMessage() );
         }
      } );

      _connection.connected( new Runnable() {
         @Override
         public void run() {
            Log.i( "TyJonesChat", "CONNECTED" );
         }
      } );

      _connection.closed( new Runnable() {
         @Override
         public void run() {
            Log.i( "TyJonesChat", "CLOSED" );
         }
      } );

      _connection.start().done( new Action<Void>() {
         @Override
         public void run( Void aVoid ) throws Exception {
            Log.i( "TyJonesChat", "Done connecting..." );
         }
      } );

      _connection.received( new MessageReceivedHandler() {
         @Override
         public void onMessageReceived( JsonElement jsonElement ) {
            final JsonElement messageJson = jsonElement;
            runOnUiThread( new Runnable() {
               @Override
               public void run() {
                  if ( messageJson.getAsJsonObject().get( "A" ) != null ) {
                     try {
                        Gson gson = _gsonBuilder.create();
                        SignalRMessage signalRMessage = gson.fromJson( messageJson, SignalRMessage.class );
                        ChatMessage chatMessage = signalRMessage.getChatMessage();

                        TextView textView = (TextView) findViewById( R.id.messagesTextView );
                        textView.setText( textView.getText() + "\n" + chatMessage.getName() + ": " + chatMessage.getMessage() );
                     } catch ( JsonSyntaxException e ) {
                        Log.i( "TyJonesChat", "Error parsing message: " + e.getMessage() );
                     }
                  }
               }
            } );
         }
      } );
   }

   @Override
   protected void onDestroy() {
      _connection.stop();
      super.onDestroy();
   }

   public void sendMessage( View view ) {
      EditText editText = (EditText) findViewById( R.id.messageText );

      String message = editText.getText().toString();
      _hub.invoke( "Send", Build.DEVICE, message );
      editText.setText( "" );
   }
}
