package Rooms;

import Messages.Message;
import Messages.MessageList;
import Users.User;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.GsonBuilder;
import com.google.appengine.repackaged.com.google.gson.annotations.Expose;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

//@TODO установить ограничение на количество сообщений в комнате и удаление, отдельным потоком
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Room {
    @Expose
    @XmlElement
    private String name;
    private List<Message> messageList;
    @Expose
    @XmlElement
    private List<User> userList;

    private Room() {
    }

    public Room(String name, RoomList... rl) {
        RoomList roomList;
        if (rl.length == 0) roomList = RoomList.getInstance();
        else roomList = rl[0];
        this.name = name;
        messageList = new ArrayList<Message>();
        userList = new ArrayList<User>();
        RoomAdd(name, roomList);
    }

    private synchronized void RoomAdd(String name, RoomList rl) {
        if (!rl.isContains(name)) {
            rl.add(this);
        }

    }

    public synchronized void addMessage(Message message) {
        messageList.add(message);
    }


    public void deleteUser(User user) {
        userList.remove(user);
    }

    public void addUser(User user) {
        userList.add(user);
    }

    public boolean isContain(String name) {
        if (getUserByName(name) == null) return false;
        return true;
    }

    public boolean isContain(User user) {
        return userList.contains(user);
    }

    public User getUserByName(String name) {
        for (User u : userList) {
            if (u.getName().equals(name)) return u;
        }
        return null;
    }

    public User getUserBySession(String sessionID) {
        for (User u : userList) {
            if (u.getSessionID().equals(sessionID)) return u;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public MessageList getMessageList(User user) {
        MessageList result = new MessageList();
        for (Message m : messageList) {        //добавляем в результирующий список сообщение, если списка получателя нет
            if (m.getRecepients() == null) {
                result.add(m);
            } else if ((m.getRecepients().contains(user))||(m.getSender().equals(user))) {   //или список содержит данного пользователя, или является отправителем
                result.add(m);
            }
        }
        return result;
    }

    public MessageList getMessageList(User user, int fromN) {
        MessageList result = getMessageList(user);
        int end = result.getMessages().size();
        Message[] arrayMessage = result.toArray();
        List<Message> lastMessage = new ArrayList<Message>();
        for (int i = fromN; i < end; i++) {
            lastMessage.add(arrayMessage[i]);
        }
        result.setMessages(lastMessage);
        return result;
    }

    public void writeToStreamMessageList(OutputStream os, String contentType, User user, int fromMessage) {
        DataOutputStream out = new DataOutputStream(os);
        if (contentType.equals("JSON")) {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            String json = gson.toJson(getMessageList(user, fromMessage));
            try {
                OutputStreamWriter osw=new OutputStreamWriter(out,"UTF-8");
                osw.write(json);
                osw.flush();
                osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(MessageList.class);
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.marshal(getMessageList(user, fromMessage), out);
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }

    }

    public void writeToStreamUsersList(OutputStream os, String contentType) {

        if (contentType.equals("JSON")) {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            String json = gson.toJson(this);

            try {
                OutputStreamWriter osw = new OutputStreamWriter(os,"UTF-8");
                osw.write(json);
                osw.flush();
                osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(Room.class);
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.marshal(this, os);

            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }

    }

}
