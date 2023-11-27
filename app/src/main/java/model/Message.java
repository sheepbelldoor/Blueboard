package model;

import java.util.Date;
import java.util.List;

public class Message {
    // Fields
    private String id;
    private String receiverId;
    private String senderId;
    private String title;
    private String content;
    private Date date;
    private List<String> replies;
    private List<String> files;
    private boolean isRead;

    // Constructor
    protected Message() {}
    protected Message(String id, String receiverId, String senderId, String title,
                    String content, Date date, List<String> replies,
                    List<String> files, boolean isRead) {
        this.id = id;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.title = title;
        this.content = content;
        this.date = date;
        this.replies = replies;
        this.files = files;
        this.isRead = isRead;
    }

    // Named Constructor
    public static Message makeMessage() {
        // named constructor
        // TODO: implement
        return new Message(null, null, null, null, null, null, null, null, false);
    }
    public static Message makeMessage(String id, String receiverId, String senderId, String title,
                                      String content, Date date, List<String> replies,
                                      List<String> files, boolean isRead) {
        return new Message(id, receiverId, senderId, title, content, date, replies, files, isRead);
    }
    // Methods
    public void sendMsg(List<String> receivers) {
        // 메시지 보내기, DB에 저장해두기. 여러 명에게 보낼 수 있음.
        // 보낼 때 수신자들이 푸시 알림을 받도록.
        // TODO: implement
    }

    public void deleteMsg(boolean fromReceived) {
        // 받은 메시지에서 없앨지, 보낸 메시지에서 없앨지를 parameter로 받으면, 해당 user의 list에서 없애도록 할 수 있을 듯.
        // TODO: implement
    }

    public void addFile(String fileName) {
        // 첨부파일 추가
        // TODO: implement
    }

    public void setRead() {
        // 읽은 메시지로 표시
        // TODO: implement
        this.isRead = true;
    }

    public void saveFileToLocal(String fileName) {
        // 첨부파일 로컬 장치에 저장
        // TODO: implement
    }

    // getter
    public String getId() {
        return id;
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

    public List<String> getReplies() {
        return replies;
    }

    public List<String> getFiles() {
        return files;
    }

    public boolean getIsRead() {
        return isRead;
    }
}

