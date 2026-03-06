package domain;

import java.util.Random;

//用户类
public class User {
    //id，用户名，密码，状态
    private String id;
    private String username;
    private String password;
    private boolean status;//true当前账户可用，false当前账户不可用

    //空参构造
    public User() {
        this.id = createId();
        this.status = true;
    }

    //带参构造
    public User(String username, String password) {
        this.id = createId();
        this.username = username;
        this.password = password;
        this.status = true;
    }

    //get和set方法
    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    //id是用户无法设置的，是自动生成的，格式为：user+5位数字的随机数
    public String createId() {
        StringBuilder sb = new StringBuilder("user");
        Random r = new Random();
        sb.append(r.nextInt(10000, 100000));
        return sb.toString();
    }
}
