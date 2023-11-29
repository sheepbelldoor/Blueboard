package model;

import java.util.Date;
import java.util.List;

public class Alarm {
    // Fields
    private String id;
    private String type;
    private String receiverId;
    private String senderId;
    private String title;
    private String content;
    private Date date;

    // Constructor
    private Alarm() {}
    private Alarm(String id, String type, String receiverId, String senderId,
                  String title, String content, Date date) {
        this.id = id;
        this.type = type;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    // Named Constructor
    public static Alarm makeAlarm() {
        // named constructor
        // TODO: implement
        return new Alarm(null, null, null, null, null, null, null);
    }

    // Methods (If additional methods are needed for Alarm, add them here)
    public static Alarm makeAlarm(String id, String type, String receiverId, String senderId,
                                  String title, String content, Date date){
        return new Alarm(id, type, receiverId, senderId, title, content, date);

    }
    // getter
    // id -> lectureId로 사용
    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }
}

