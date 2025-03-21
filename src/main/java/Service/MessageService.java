package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public Message getMessageByID(int message_id){
        return messageDAO.getMessageByID(message_id);
    }

    public List<Message> getAllMessageByUser(int account_id){
        if (account_id < 0) {
            return null;
        }
        return messageDAO.getAllMessageByUser(account_id);
    }

    public List<Message> getAllMessage(){
        return messageDAO.getAllMessage();
    }

    public Message postMessage(Message message){
        if (message.getMessage_text().isBlank() || message.getMessage_text().isBlank() || message.equals(null)) {
            return null;
        }
        return messageDAO.postMessage(message);
    }
    public Message updateMessageByID(Message message){
        if (message.message_text.isBlank() || message.message_text.length() > 255 ) {
            return null;
        }
        return messageDAO.updateMessageByID(message);
    }
    public Message deleteMessage(Message message){
        if (message.equals(null)) {
            return null;
        }
        return messageDAO.deleteMessage(message);
    }

}
