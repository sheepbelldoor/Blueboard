package model;

public class Account {
    // Fields
    private String id;
    private String accountId;
    private String accountPw;
    private boolean isManager;
    private int loginFail;

    private Account() {}

    // Constructor
    private Account(String id, String accountId, String accountPw, boolean isManager, int loginFail) {
        this.id = id;
        this.accountId = accountId;
        this.accountPw = accountPw;
        this.isManager = isManager;
        this.loginFail = loginFail;
    }

    // Named Constructor
    public static Account makeAccount(){
        // named constructor
        return new Account();
    }
    public static Account makeAccount(String uid, String aid, String pw) {

        // TODO: implement

        return new Account(uid, aid, pw, false, 0);
    }

    // Methods
    public boolean login() {
        // DB에 id, pw 있는지 확인
        // TODO: implement


        return false; // 로그인 성공 시 true, 실패 시 false 반환
    }

    public boolean logout() {
        // 로그아웃 후, 로그인 창으로 이동
        // TODO: implement
        return false; // 로그아웃 성공 시 true, 실패 시 false 반환
    }

    public void loginLimit() {
        // 로그인 3회 실패 시, 3분 로그인 제한
        // TODO: implement
    }

    public boolean timeCheck() {
        // 로그인 제한된 시간을 확인 후, 일정 시간이 지났는지 확인
        // TODO: implement
        return false; // 일정 시간이 지나지 않았을 경우 true, 지났을 경우 false 반환
    }

    public boolean idAvail() {
        // 중복 확인
        // TODO: implement
        return false; // 사용 가능한 id일 경우 true, 이미 존재하는 id일 경우 false 반환
    }

    public boolean pwAvail() {
        // pw 양식에 맞는지 확인
        // TODO: implement
        return false; // 올바른 pw 형식일 경우 true, 그렇지 않을 경우 false 반환
    }

    // getter
    public String getId() {
        return id;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAccountPw() {
        return accountPw;
    }

    public boolean isManager() {
        return isManager;
    }

    public int getLoginFail() {
        return loginFail;
    }
}

