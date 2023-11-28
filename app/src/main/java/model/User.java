package model;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class User {
    // Fields
    private String id;
    private String accountId;
    private String name;
    private String institution;
    private String major;
    private String email;
    private String profile;
    private List<String> courses;
    private List<String> sentMessages;
    private List<String> receivedMessages;
    private List<String> alarms;
    private int grade;
    private long studentId;
    private int isManager;

    // Constructor
    // Firebase test 위해서 public으로 임시변경
    public User(String id, String accountId, String name, String institution,
                String major, String email, String profile, List<String> courses,
                List<String> sentMessages, List<String> receivedMessages,
                List<String> alarms, int grade, long studentId, int isManager) {
        this.id = id;
        this.accountId = accountId;
        this.name = name;
        this.institution = institution;
        this.major = major;
        this.email = email;
        this.profile = profile;
        this.courses = courses;
        this.sentMessages = sentMessages;
        this.receivedMessages = receivedMessages;
        this.alarms = alarms;
        this.grade = grade;
        this.studentId = studentId;
        this.isManager = isManager;
    }
    private User() {}
    // Named Constructor
    public static User makeUser() {
        // named constructor
        // TODO: implement
        return new User();
    }

    public static User makeUser(String id, String accountId, String name, String institution,
                                String major, String email, String profile, List<String> courses,
                                List<String> sentMessages, List<String> receivedMessages,
                                List<String> alarms, int grade, long studentId, int isManager) {
        List<String> lecture, received, sent;

        if (courses == null)
            lecture = new ArrayList<>();
        else
            lecture = courses;

        if(receivedMessages == null) {
            received = new ArrayList<>();
        }
        else {
            received = receivedMessages;
        }

        if(sentMessages == null) {
            sent = new ArrayList<>();
        }
        else {
            sent = sentMessages;
        }

        return new User(id, accountId, name, institution, major, email, profile, lecture, sent, received, alarms, grade, studentId, isManager);
    }

    // Methods
    public void editInfo() {
        // 정보 수정
        // TODO: implement
    }

    // getter
    public String getId() {
        return id;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", accountId='" + accountId + '\'' +
                ", name='" + name + '\'' +
                ", institution='" + institution + '\'' +
                ", major='" + major + '\'' +
                ", email='" + email + '\'' +
                ", profile='" + profile + '\'' +
                ", courses=" + courses +
                ", sentMessages=" + sentMessages +
                ", receivedMessages=" + receivedMessages +
                ", alarms=" + alarms +
                ", grade=" + grade +
                ", studentId=" + studentId +
                '}';
    }

    public String getInstitution() {
        return institution;
    }

    public String getMajor() {
        return major;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getLectures() { return this.courses; }

    public String getProfile() {
        return profile;
    }

    public List<String> getCourses() {
        return courses;
    }

    public List<String> getSentMessages() {
        return sentMessages;
    }

    public List<String> getReceivedMessages() {
        return receivedMessages;
    }

    public List<String> getAlarms() {
        return alarms;
    }

    public int getGrade() {
        return grade;
    }

    public long getStudentId() {
        return studentId;
    }

    public int getIsManager(){ return this.isManager; }
  
    public void addReceived(String id) {
        if (id == null) {
            Log.d("addReceived: ", "NULL");
        }else {
            this.receivedMessages.add(id);
        }
    }

    public void addSent(String id) {
        if (id == null) {
            Log.d("addSent: ", "NULL");
        }else{
            this.sentMessages.add(id);
        }
    }

    public void addCourses(String id) {
        this.courses.add(id);
    }

    public void setId(String id) { this.id = id; }
}
