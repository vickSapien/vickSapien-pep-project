package Controller;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;



/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    
    
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::registerAccountHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.get("/messages", this::getAllMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessageByUserHandler);
        app.post("/messages", this::postMessageHandler);
        app.post("/login", this::loginHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIDHandler); 

        return app;
    }

    private void registerAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.registerAccount(account);
        if(addedAccount!=null){
            ctx.json(mapper.writeValueAsString(addedAccount));
        }else{
            ctx.status(400);
        }
    }
    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account passwordAccount = accountService.getAccountbyPassword(account.getPassword());
        Account usernameAccount = accountService.getAccountbyUsername(account.getUsername());

        if(passwordAccount != null && usernameAccount != null){
            ctx.json(accountService.accountLogin(account));
        }else{
            ctx.status(401);
        }
    }

    private void postMessageHandler(Context ctx) throws JsonMappingException, JsonProcessingException  {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body().toString(), Message.class);
        
        Message addedMessage = messageService.postMessage(message);
        if(addedMessage!=null){
            ctx.json(mapper.writeValueAsString(addedMessage));
        }else{
            ctx.status(400);
        }
    }


    private void getAllMessageHandler(Context ctx) throws JsonProcessingException {
        List<Message> messages = messageService.getAllMessage();
        ctx.json(messages);
        
    }

    private void getMessageByIDHandler(Context ctx)   {
        int messageId = Integer.parseInt(Objects.requireNonNull(ctx.pathParam("message_id")));
        Message message = messageService.getMessageByID(messageId);

        if (message == null) {
            ctx.json(ctx.body());
        } else {
            ctx.json(message);
        }
    
    }

    private void deleteMessageByIDHandler(Context ctx) throws JsonProcessingException {
        int messageId = Integer.parseInt(Objects.requireNonNull(ctx.pathParam("message_id")));
        Message message = messageService.getMessageByID(messageId);

        if (message == null) {
            ctx.json(ctx.body());
        } else {
            ctx.json(messageService.deleteMessage(message));
        }
        
    }

    private void updateMessageByIDHandler(Context ctx) throws JsonProcessingException {
        
        int messageId = Integer.parseInt(Objects.requireNonNull(ctx.pathParam("message_id"))); 
        
        ObjectMapper mapper = new ObjectMapper();
        Message updatedMessageText = mapper.readValue(ctx.body(), Message.class);
        
        Message oldMessage = messageService.getMessageByID(messageId);
        
    
        if(oldMessage!=null){
            oldMessage.setMessage_text(updatedMessageText.getMessage_text());
            Message addedMessage = messageService.updateMessageByID(oldMessage);
            if (addedMessage!=null) {
                ctx.json(mapper.writeValueAsString(addedMessage));
            }
            else{
                ctx.status(400);
            }
        }
        else{
            ctx.status(400);
        }
        
    }

    private void getAllMessageByUserHandler(Context ctx) throws JsonProcessingException {

        int messageId = Integer.parseInt(Objects.requireNonNull(ctx.pathParam("account_id")));
        List<Message> messages = messageService.getAllMessageByUser(messageId);

        if (messages == null) {
            ctx.json(ctx.body());
        } else {
            ctx.json(messages);
        }
    }

}