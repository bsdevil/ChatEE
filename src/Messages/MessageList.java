package Messages;

import com.google.appengine.repackaged.com.google.gson.annotations.Expose;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class MessageList {

    @Expose
    private List<Message> messages;

    public MessageList() {
        messages=new ArrayList<Message>();
    }
    public void add(Message m){
        messages.add(m);
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<Message> getMessages() {
        return messages;
    }
    public Message[] toArray(){
        Message[] result=new Message[messages.size()];
        int i=0;
        for (Message m:messages){
            result[i++]=m;
        }
        return result;
    }

}
