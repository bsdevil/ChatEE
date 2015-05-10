package Messages;

import Users.User;
import com.google.appengine.repackaged.com.google.gson.annotations.Expose;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Message {
    @Expose
    @XmlElement
    private String body;
    @Expose
    @XmlElement
    private Date date;
    @Expose
    @XmlElement
    private String senderName;
    @Expose
    @XmlElement
    private Boolean priv=false;
    private User sender;
    @Expose
    @XmlElement
    private ArrayList<User> recipients;

    private Message() {
    }

    public Message(String body, User sender, ArrayList<User>... recipients) {
        this.body = body;
        this.sender = sender;
        this.senderName = sender.getName();
        date=new Date();
        if ((recipients.length>0)&&(recipients[0].size()>0)){
            this.recipients=recipients[0];
            priv=true;
        }
    }

    public User getSender() {
        return sender;
    }

    public ArrayList<User> getRecepients() {
        return recipients;
    }

    @Override
    public String toString() {
        return "Message{" +
                "body='" + body + '\'' +
                ", date=" + date +
                ", senderName='" + senderName + '\'' +
                ", priv=" + priv +
                ", sender=" + sender +
                ", recipients=" + recipients +
                '}';
    }
}
