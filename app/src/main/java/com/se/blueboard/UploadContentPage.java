package com.se.blueboard;

import static com.se.blueboard.LecturePage.currentLecture;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import model.Announcement;
import model.Assignment;
import model.Exam;
import model.Post;
import utils.FirebaseController;
import utils.PathUtils;
import utils.Utils;

public class UploadContentPage extends AppCompatActivity {
    File attachment = null;
    String fileName = null;
    int week = 1;
    Calendar calendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_content);

        // 주차 설정
        week = Integer.parseInt(getIntent().getExtras().getString("key", "1"));

        // 제목 EditText
        EditText title = (EditText) findViewById(R.id.uploadContent_title);

        // 게시글 유형 Spinner
        Spinner format = (Spinner) findViewById(R.id.uploadContent_format);
        String[] items = {"일반 게시글", "공지사항","과제", "시험"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        format.setAdapter(adapter);

        // 마감일자
        EditText dueDate = (EditText) findViewById(R.id.uploadContent_dueDate);
        dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(UploadContentPage.this, datePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // 내용
        EditText cont = (EditText) findViewById(R.id.uploadContent_content);

        // 첨부파일 Button
        Button fileButton = (Button) findViewById(R.id.uploadContent_file);
        fileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 128);
            }
        });

        // 취소 Button
        Button cancelButton = (Button) findViewById(R.id.uploadContent_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.gotoPage(getApplicationContext(), ContentsPage.class, null);
            }
        });

        // 게시 Button
        FirebaseController controller = new FirebaseController();
        Button postButton = (Button) findViewById(R.id.uploadContent_post);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 제목 format check
                if (title.getText().toString().length() > 50) {
                    Utils.toastTest(getApplicationContext(), "제목은 50자 이내로 하렴");
                    return;
                }
                String pattern = "^[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣|( )]*$";
                if(!Pattern.matches(pattern, title.getText().toString())){
                    Utils.toastTest(getApplicationContext(), "제목 양식이 좀...");
                    return;
                }


                if (format.getSelectedItem().toString().equals("일반 게시글")) {
                    Post post = Post.makePost(title.getText().toString(), title.getText().toString(),
                            week, calendar.getTime(),
                            format.getSelectedItem().toString(), fileName, cont.getText().toString(),
                            new Date(), null, currentLecture.getId());
                    controller.updateData(post);
                }
                else if (format.getSelectedItem().toString().equals("공지사항")) {
                    Announcement announcement = Announcement.makeAnnouncement(title.getText().toString(),
                            cont.getText().toString(), new Date(), fileName, title.getText().toString(), null, currentLecture.getId());
                    controller.updateData(announcement);
                }
                else if (format.getSelectedItem().toString().equals("과제")) {
                    Assignment assignment = Assignment.makeAssignment(title.getText().toString(), title.getText().toString(),
                            week, calendar.getTime(), format.getSelectedItem().toString(),
                            fileName, cont.getText().toString(), new Date(), calendar.getTime(), null, currentLecture.getId());
                    controller.updateData(assignment);
                }
                else /* Exam case */ {
                    Exam exam = Exam.makeExam(title.getText().toString(), title.getText().toString(), week,
                            calendar.getTime(), format.getSelectedItem().toString(), fileName, cont.getText().toString(), new Date(), null, currentLecture.getId());
                    controller.updateData(exam);
                }
                if (attachment != null)
                    controller.uploadFile(attachment);
                Utils.gotoPage(getApplicationContext(), ContentsPage.class, null);
            }
        });
    }

    private void updateLabel() {
        String format = "yyyy/MM/dd";
        SimpleDateFormat tmp = new SimpleDateFormat(format, Locale.KOREA);

        EditText date = (EditText) findViewById(R.id.uploadContent_dueDate);
        date.setText(tmp.format(calendar.getTime()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 128) {
                attachment = new File(PathUtils.getPath(getApplicationContext(), data.getData()));
                fileName = attachment.getName();
                Utils.toastTest(getApplicationContext(), "파일 첨부..!");
            }
        }
    }
}
