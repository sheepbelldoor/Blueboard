package utils;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import model.Account;
import model.Alarm;
import model.Announcement;
import model.Assignment;
import model.Comment;
import model.Exam;
import model.Institution;
import model.LearningStatus;
import model.Lecture;
import model.LectureContent;
import model.Material;
import model.Message;
import model.Post;
import model.Reply;
import model.Submission;
import model.User;

public class FirebaseController {
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    public FirebaseController() {
        // Connect to Firebase Firestore
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    // Send User data to Firebase
    // firestore의 데이터 추가는 비동기 작업이라 return 으로 성공 여부 알 수 없음
    public void sendUserData(User user) {
        // Convert User data to Map<String, Object>
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("accountId", user.getAccountId());
        userMap.put("name", user.getName());
        userMap.put("institution", user.getInstitution());
        userMap.put("major", user.getMajor());
        userMap.put("email", user.getEmail());
        userMap.put("profile", user.getProfile());
        userMap.put("courses", user.getCourses());
        userMap.put("sentMessages", user.getSentMessages());
        userMap.put("receivedMessages", user.getReceivedMessages());
        userMap.put("alarms", user.getAlarms());
        userMap.put("grade", user.getGrade());
        userMap.put("studentId", user.getStudentId());
        userMap.put("isManager", user.getIsManager());

        // Add data to "users" collection
        db.collection("users")
                .document(user.getId())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data has been added successfully
                        Log.d("FirebaseController", "User data added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add data
                        Log.e("FirebaseController", "Error adding user data", e);
                    }
                });
    }

    // Send Account data to Firebase
    public void sendAccountData(Account account) {
        // Convert Account data to Map<String, Object>
        Map<String, Object> accountMap = new HashMap<>();
        accountMap.put("id", account.getId());
        accountMap.put("accountId", account.getAccountId());
        accountMap.put("accountPw", account.getAccountPw());
        accountMap.put("isManager", account.isManager());
        accountMap.put("loginFail", account.getLoginFail());

        // Add data to "accounts" collection
        db.collection("accounts")
                .document(account.getId())
                .set(accountMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data has been added successfully
                        Log.d("FirebaseController", "Account data added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add data
                        Log.e("FirebaseController", "Error adding account data", e);
                    }
                });
    }

    // Send Institution data to Firebase
    public void sendInstitutionData(Institution institution) {
        // Convert Institution data to Map<String, Object>
        Map<String, Object> institutionMap = new HashMap<>();
        institutionMap.put("id", institution.getId());
        institutionMap.put("institution", institution.getInstitution());
        institutionMap.put("major", institution.getMajor());

        // Add data to "institutions" collection
        db.collection("institutions")
                .document(institution.getId())
                .set(institutionMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data has been added successfully
                        Log.d("FirebaseController", "Institution data added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add data
                        Log.e("FirebaseController", "Error adding institution data", e);
                    }
                });
    }

    // Send Lecture data to Firebase
    public void sendLectureData(Lecture lecture) {
        // Convert Lecture data to Map<String, Object>
        Map<String, Object> lectureMap = new HashMap<>();
        lectureMap.put("id", lecture.getId());
        lectureMap.put("name", lecture.getName());
        lectureMap.put("weeks", lecture.getWeeks());
        lectureMap.put("managerId", lecture.getManagerId());
        lectureMap.put("managers", lecture.getManagers());
        lectureMap.put("students", lecture.getStudents());
        lectureMap.put("lectureContents", lecture.getLectureContents());

        // Add data to "lectures" collection
        db.collection("lectures")
                .document(lecture.getId())
                .set(lectureMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data has been added successfully
                        Log.d("FirebaseController", "Lecture data added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add data
                        Log.e("FirebaseController", "Error adding lecture data", e);
                    }
                });
    }

    // Send Exam data to Firebase
    public void sendExamData(Exam exam) {
        // Convert Exam data to Map<String, Object>
        Map<String, Object> examMap = new HashMap<>();
        examMap.put("id", exam.getId());
        examMap.put("title", exam.getTitle());
        examMap.put("week", exam.getWeek());
        examMap.put("deadline", exam.getDeadline());
        examMap.put("type", exam.getType());
        examMap.put("files", exam.getFiles());
        examMap.put("detail", exam.getDetail());
        examMap.put("date", exam.getDate());
        examMap.put("location", exam.getLocation());

        // Add data to "exams" collection
        db.collection("exams")
                .document(exam.getId())
                .set(examMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data has been added successfully
                        Log.d("FirebaseController", "Exam data added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add data
                        Log.e("FirebaseController", "Error adding exam data", e);
                    }
                });
    }

    // Send Post data to Firebase
    public void sendPostData(Post post) {
        // Convert Post data to Map<String, Object>
        Map<String, Object> postMap = new HashMap<>();
        postMap.put("id", post.getId());
        postMap.put("title", post.getTitle());
        postMap.put("week", post.getWeek());
        postMap.put("deadline", post.getDeadline());
        postMap.put("type", post.getType());
        postMap.put("files", post.getFiles());
        postMap.put("detail", post.getDetail());
        postMap.put("uploadTime", post.getUploadTime());
        postMap.put("comments", post.getComments());

        // Add data to "posts" collection
        db.collection("posts")
                .document(post.getId())
                .set(postMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data has been added successfully
                        Log.d("FirebaseController", "Post data added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add data
                        Log.e("FirebaseController", "Error adding post data", e);
                    }
                });
    }

    // Send Assignment data to Firebase
    public void sendAssignmentData(Assignment assignment) {
        // Convert Assignment data to Map<String, Object>
        Map<String, Object> assignmentMap = new HashMap<>();
        assignmentMap.put("id", assignment.getId());
        assignmentMap.put("title", assignment.getTitle());
        assignmentMap.put("week", assignment.getWeek());
        assignmentMap.put("deadline", assignment.getDeadline());
        assignmentMap.put("type", assignment.getType());
        assignmentMap.put("files", assignment.getFiles());
        assignmentMap.put("detail", assignment.getDetail());
        assignmentMap.put("uploadTime", assignment.getUploadTime());
        assignmentMap.put("deadline", assignment.getDeadline());
        assignmentMap.put("submissions", assignment.getSubmissions());

        // Add data to "assignments" collection
        db.collection("assignments")
                .document(assignment.getId())
                .set(assignmentMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data has been added successfully
                        Log.d("FirebaseController", "Assignment data added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add data
                        Log.e("FirebaseController", "Error adding assignment data", e);
                    }
                });
    }

    // Send Material data to Firebase
    public void sendMaterialData(Material material) {
        // Convert Material data to Map<String, Object>
        Map<String, Object> materialMap = new HashMap<>();
        materialMap.put("id", material.getId());
        materialMap.put("title", material.getTitle());
        materialMap.put("week", material.getWeek());
        materialMap.put("deadline", material.getDeadline());
        materialMap.put("type", material.getType());
        materialMap.put("files", material.getFiles());
        materialMap.put("detail", material.getDetail());
        materialMap.put("uploadTime", material.getUploadTime());

        // Add data to "materials" collection
        db.collection("materials")
                .document(material.getId())
                .set(materialMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data has been added successfully
                        Log.d("FirebaseController", "Material data added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add data
                        Log.e("FirebaseController", "Error adding material data", e);
                    }
                });
    }

    // Send LearningStatus data to Firebase
    public void sendLearningStatusData(LearningStatus learningStatus) {
        // Convert LearningStatus data to Map<String, Object>
        Map<String, Object> learningStatusMap = new HashMap<>();
        learningStatusMap.put("id", learningStatus.getId());
        learningStatusMap.put("userId", learningStatus.getUserId());
        learningStatusMap.put("courseId", learningStatus.getCourseId());
        learningStatusMap.put("contentId", learningStatus.getContentId());
        learningStatusMap.put("status", learningStatus.getStatus());

        // Add data to "learningStatuses" collection
        db.collection("learningStatuses")
                .document(learningStatus.getId())
                .set(learningStatusMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data has been added successfully
                        Log.d("FirebaseController", "LearningStatus data added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add data
                        Log.e("FirebaseController", "Error adding learningStatus data", e);
                    }
                });
    }

    // Send Message data to Firebase
    public void sendMessageData(Message message) {
        // Convert Message data to Map<String, Object>
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("id", message.getId());
        messageMap.put("receiverId", message.getReceiverId());
        messageMap.put("senderId", message.getSenderId());
        messageMap.put("title", message.getTitle());
        messageMap.put("content", message.getContent());
        messageMap.put("date", message.getDate());
        messageMap.put("replies", message.getReplies());
        messageMap.put("files", message.getFiles());
        messageMap.put("isRead", message.getIsRead());

        // Add data to "messages" collection
        db.collection("messages")
                .document(message.getId())
                .set(messageMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data has been added successfully
                        Log.d("FirebaseController", "Message data added successfully");
                        db.collection("users").document(message.getSenderId())
                                .update("sendMessages", FieldValue.arrayUnion(message.getId()));

                        db.collection("users").document(message.getReceiverId())
                                .update("receivedMessages", FieldValue.arrayUnion(message.getId()));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add data
                        Log.e("FirebaseController", "Error adding message data", e);
                    }
                });
    }

    // Send Reply data to Firebase
    public void sendReplyData(Reply reply) {
        // Convert Reply data to Map<String, Object>
        Map<String, Object> replyMap = new HashMap<>();
        replyMap.put("id", reply.getId());
        replyMap.put("receiverId", reply.getReceiverId());
        replyMap.put("senderId", reply.getSenderId());
        replyMap.put("title", reply.getTitle());
        replyMap.put("content", reply.getContent());
        replyMap.put("date", reply.getDate());
        replyMap.put("replies", reply.getReplies());
        replyMap.put("files", reply.getFiles());
        replyMap.put("isRead", reply.getIsRead());
        replyMap.put("rootMessage", reply.getRootMessage());

        // Add data to "replies" collection
        db.collection("replies")
                .document(reply.getId())
                .set(replyMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data has been added successfully
                        Log.d("FirebaseController", "Reply data added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add data
                        Log.e("FirebaseController", "Error adding reply data", e);
                    }
                });
    }

    // Send Alarm data to Firebase
    public void sendAlarmData(Alarm alarm) {
        // Convert Alarm data to Map<String, Object>
        Map<String, Object> alarmMap = new HashMap<>();
        alarmMap.put("id", alarm.getId());
        alarmMap.put("type", alarm.getType());
        alarmMap.put("receiverId", alarm.getReceiverId());
        alarmMap.put("senderId", alarm.getSenderId());
        alarmMap.put("title", alarm.getTitle());
        alarmMap.put("content", alarm.getContent());
        alarmMap.put("date", alarm.getDate());

        // Add data to "alarms" collection
        db.collection("alarms")
                .document(alarm.getId())
                .set(alarmMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data has been added successfully
                        Log.i("FirebaseController", "Alarm data added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add alarm data
                        Log.e("FirebaseController", "Error adding alarm data", e);
                    }
                });
    }

    // Send Announcement data to Firebase
    public void sendAnnouncementData(Announcement announcement) {
        // Convert Announcement data to Map<String, Object>
        Map<String, Object> announcementMap = new HashMap<>();
        announcementMap.put("id", announcement.getId());
        announcementMap.put("detail", announcement.getDetail());
        announcementMap.put("uploadTime", announcement.getUploadTime());
        announcementMap.put("files", announcement.getFiles());
        announcementMap.put("title", announcement.getTitle());
        announcementMap.put("comments", announcement.getComments());

        // Add data to "announcements" collection
        db.collection("announcements")
                .document(announcement.getId())
                .set(announcementMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data has been added successfully
                        Log.i("FirebaseController", "Announcement data added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add announcement data
                        Log.e("FirebaseController", "Error adding announcement data", e);
                    }
                });
    }

    // Send Comment data to Firebase
    public void sendCommentData(Comment comment) {
        // Convert Comment data to Map<String, Object>
        Map<String, Object> commentMap = new HashMap<>();
        commentMap.put("id", comment.getId());
        commentMap.put("writer", comment.getWriter());
        commentMap.put("detail", comment.getDetail());
        commentMap.put("uploadTime", comment.getUploadTime());
        commentMap.put("files", comment.getFiles());
        commentMap.put("rootPost", comment.getRootPost());
        commentMap.put("comments", comment.getComments());

        // Add data to "comments" collection
        db.collection("comments")
                .document(comment.getId())
                .set(commentMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data has been added successfully
                        Log.i("FirebaseController", "Comment data added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add comment data
                        Log.e("FirebaseController", "Error adding comment data", e);
                    }
                });
    }

    // Send Submission data to Firebase
    public void sendSubmissionData(Submission submission){
        // Convert Submission data to Map<String, Object>
        Map<String, Object> submissionMap = new HashMap<>();
        submissionMap.put("id", submission.getId());
        submissionMap.put("user", submission.getUser());
        submissionMap.put("name", submission.getName());
        submissionMap.put("date", submission.getDate());

        // Add data to "submissions" collection
        db.collection("submissions")
                .document(submission.getId())
                .set(submissionMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data has been added successfully.
                        Log.i("FirebaseController", "Submission data added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e){
                        // Failed to add submission data
                        Log.e("FirebaseController", "Error adding submission data", e);
                    }
                });
    }
    public void sendLectureContentData(LectureContent lecContent) {
        db.collection("lectureContents")
                .document(lecContent.getId()).set(lecContent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("FirebaseController", "LectureContent data added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FirebaseController", "Error adding submission data", e);
                    }
                });
    }

    public void getUserData(String id, MyCallback myCallback) {
        DocumentReference docRef = db.collection("users").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        User user = (User) document.toObject(User.class);
                        myCallback.onSuccess(user);
                    } else {
                        Log.d("GetUserData", "No such Document.");
                    }
                } else {
                    myCallback.onFailure(task.getException());
                }
            }
        });
    }

    public void getAccountData(String id, MyCallback myCallback) {
        DocumentReference docRef = db.collection("accounts").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        Account account = (Account) document.toObject(Account.class);
                        myCallback.onSuccess(account);
                    } else {
                        Log.d("GetAccountData", "No such Document.");
                    }
                } else {
                    myCallback.onFailure(task.getException());
                }
            }
        });
    }

    public void getInstitutionData(String id, MyCallback myCallback) {
        DocumentReference docRef = db.collection("institutions").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        Institution institution = (Institution) document.toObject(Institution.class);
                        myCallback.onSuccess(institution);
                    } else {
                        Log.d("GetInstitutionData", "No such Document.");
                    }
                } else {
                    myCallback.onFailure(task.getException());
                }
            }
        });
    }

    public void getLectureData(String id, MyCallback myCallback) {
        DocumentReference docRef = db.collection("lectures").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        Lecture lecture = (Lecture) document.toObject(Lecture.class);
                        myCallback.onSuccess(lecture);
                    } else {
                        Log.d("GetLectureData", "No such Document.");
                    }
                } else {
                    myCallback.onFailure(task.getException());
                }
            }
        });
    }

    public void getExamData(String id, MyCallback myCallback) {
        DocumentReference docRef = db.collection("exams").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        Exam exam = (Exam) document.toObject(Exam.class);
                        myCallback.onSuccess(exam);
                    } else {
                        Log.d("GetExamData", "No such Document.");
                    }
                } else {
                    myCallback.onFailure(task.getException());
                }
            }
        });
    }

    public void getPostData(String id, MyCallback myCallback) {
        DocumentReference docRef = db.collection("posts").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        Post post = (Post) document.toObject(Post.class);
                        myCallback.onSuccess(post);
                    } else {
                        Log.d("GetPostData", "No such Document.");
                    }
                } else {
                    myCallback.onFailure(task.getException());
                }
            }
        });
    }

    public void getAssignmentData(String id, MyCallback myCallback) {
        DocumentReference docRef = db.collection("assignments").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        Assignment assignment= (Assignment) document.toObject(Assignment.class);
                        myCallback.onSuccess(assignment);
                    } else {
                        Log.d("GetAssignmentData", "No such Document.");
                    }
                } else {
                    myCallback.onFailure(task.getException());
                }
            }
        });
    }

    public void getMaterialData(String id, MyCallback myCallback) {
        DocumentReference docRef = db.collection("materials").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        Material material= (Material) document.toObject(Material.class);
                        myCallback.onSuccess(material);
                    } else {
                        Log.d("GetMaterialData", "No such Document.");
                    }
                } else {
                    myCallback.onFailure(task.getException());
                }
            }
        });
    }

    public void getLearningStatusData(String id, MyCallback myCallback) {
        DocumentReference docRef = db.collection("learningStatuses").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        LearningStatus learningStatus= (LearningStatus) document.toObject(LearningStatus.class);
                        myCallback.onSuccess(learningStatus);
                    } else {
                        Log.d("GetLearningStatusData", "No such Document.");
                    }
                } else {
                    myCallback.onFailure(task.getException());
                }
            }
        });
    }

    public void getMessageData(String id, MyCallback myCallback) {
        DocumentReference docRef = db.collection("messages").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        Message message= (Message) document.toObject(Message.class);
                        myCallback.onSuccess(message);
                    } else {
                        Log.d("GetMessageData", "No such Document.");
                    }
                } else {
                    myCallback.onFailure(task.getException());
                }
            }
        });
    }

    public void getReplyData(String id, MyCallback myCallback) {
        DocumentReference docRef = db.collection("replies").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        Reply reply = (Reply) document.toObject(Reply.class);
                        myCallback.onSuccess(reply);
                    } else {
                        Log.d("GetReplyData", "No such Document.");
                    }
                } else {
                    myCallback.onFailure(task.getException());
                }
            }
        });
    }

    public void getAlarmData(String id, MyCallback myCallback) {
        DocumentReference docRef = db.collection("alarms").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        Alarm alarm = (Alarm) document.toObject(Alarm.class);
                        myCallback.onSuccess(alarm);
                    } else {
                        Log.d("GetAlarmData", "No such Document.");
                    }
                } else {
                    myCallback.onFailure(task.getException());
                }
            }
        });
    }

    public void getAnnouncementData(String id, MyCallback myCallback) {
        DocumentReference docRef = db.collection("announcements").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        Announcement announcement = (Announcement) document.toObject(Announcement.class);
                        myCallback.onSuccess(announcement);
                    } else {
                        Log.d("GetAnnouncementData", "No such Document.");
                    }
                } else {
                    myCallback.onFailure(task.getException());
                }
            }
        });
    }

    public void getCommentData(String id, MyCallback myCallback) {
        DocumentReference docRef = db.collection("comments").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        Comment comment = (Comment) document.toObject(Comment.class);
                        myCallback.onSuccess(comment);
                    } else {
                        Log.d("GetCommentData", "No such Document.");
                    }
                } else {
                    myCallback.onFailure(task.getException());
                }
            }
        });
    }

    public void getSubmissionData(String id, MyCallback myCallback) {
        DocumentReference docRef = db.collection("submissions").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        Submission submission = (Submission) document.toObject(Submission.class);
                        myCallback.onSuccess(submission);
                    } else {
                        Log.d("GetSubmissionData", "No such Document.");
                    }
                } else {
                    myCallback.onFailure(task.getException());
                }
            }
        });
    }

    public void getLectureContentData(String id, MyCallback myCallback) {
        DocumentReference docRef = db.collection("lectureContents").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        LectureContent lectureContent = (LectureContent) document.toObject(LectureContent.class);
                        myCallback.onSuccess(lectureContent);
                    } else {
                        Log.d("GetLectureContentData", "No such Document.");
                    }
                } else {
                    myCallback.onFailure(task.getException());
                }
            }
        });
    }

    public void deleteData(String collection, String document) {
        DocumentReference docRef = db.collection(collection).document(document);
        docRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("deleteData", "Document successfully deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("deleteData", "Error deleting document", e);
                    }
                });
    }

    public void updateData(Object object) {
//        Log.d("Compare", (object.getClass() == User.class) ? "true":"false");
        Class objectClass = object.getClass();
        if (objectClass == Account.class) {
            sendAccountData((Account) object);
        } else if (objectClass == Alarm.class) {
            sendAlarmData((Alarm) object);
        } else if (objectClass == Announcement.class) {
            sendAnnouncementData((Announcement) object);
        } else if (objectClass == Assignment.class) {
            sendAssignmentData((Assignment) object);
        } else if (objectClass == Comment.class) {
            sendCommentData((Comment) object);
        } else if (objectClass == Exam.class) {
            sendExamData((Exam) object);
        } else if (objectClass == Institution.class) {
            sendInstitutionData((Institution) object);
        } else if (objectClass == LearningStatus.class) {
            sendLearningStatusData((LearningStatus) object);
        } else if (objectClass == Lecture.class) {
            sendLectureData((Lecture) object);
        } else if (objectClass == LectureContent.class) {
            sendLectureContentData((LectureContent) object);
        } else if (objectClass == Material.class) {
            sendMaterialData((Material) object);
        } else if (objectClass == Message.class) {
            sendMessageData((Message) object);
        } else if (objectClass == Post.class) {
            sendPostData((Post) object);
        } else if (objectClass == Reply.class) {
            sendReplyData((Reply) object);
        } else if (objectClass == Submission.class) {
            sendSubmissionData((Submission) object);
        } else if (objectClass == User.class) {
            Log.d("updateData", ((User)object).toString());
            sendUserData((User) object);
        } else {
            Log.d("updateData", "No such Object in DB");
        }
    }

    public void uploadFile(File file) {
        // call 하기 전에 fileAvail
        // 확장자 별로 storage 분류
        StorageReference storageRef;
        UploadTask task;
        Uri uri;
        String name, format;

        storageRef = storage.getReference();
        uri = Uri.fromFile(file);

        name = file.getName();

        if (name.contains(".")) {
            format = name.substring(name.lastIndexOf(".") + 1);
        } else {
            format = "etc";
        }

        StorageReference fileRef = storageRef.child(format + "/" + name);
        task = fileRef.putFile(uri);

        task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Upload File", "Success to Upload file.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Upload File", "Fail to Upload file.");
            }
        });
    }

    public void downloadFile(String name) {
        StorageReference fileRef;
        String format;
        File downloadFile, downloadFolder;
        FileDownloadTask task;

        if (name.contains(".")) {
            format = name.substring(name.lastIndexOf(".") + 1);
        } else {
            format = "etc";
        }
        fileRef = storage.getReference().child(format + "/" + name);

        downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        downloadFile = new File(downloadFolder.getPath() + '/' + name);
        task = fileRef.getFile(downloadFile);

        task.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.d("Download File", "Success to Download file.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Download File", "Fail to Download file.");
            }
        });
    }
}
