package ui;

import domain.User;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Login {
    //这个方法表示的是登录注册的主页面，以控制台的形式展示
    public void start() {
        ArrayList<User> users = new ArrayList<>();
        int flag = 0;
        while (true) {
            if (flag == 1) {
                break;
            }
            System.out.println("—————————————————————————————————");
            System.out.println("|    🎮欢迎来到文字格斗游戏🎮    |");
            System.out.println("—————————————————————————————————");
            System.out.println("|  请选择：1.登录  2.注册  3.退出 |");
            Scanner sc = new Scanner(System.in);
            String choice = sc.next();
            switch (choice) {
                case "1":
                    if (login(users)) {
                        flag = 1;
                    }
                    break;
                case "2":
                    register(users);
                    break;
                case "3":
                    System.out.println("再见！欢迎下次游玩~");
                    System.exit(0);
                default:
                    System.out.println("输入有误，请重新输入。");
                    break;
            }
        }
    }

    //登录的操作
    public boolean login(ArrayList<User> users) {
        Scanner sc = new Scanner(System.in);
        System.out.print("请输入用户名：");
        String username = sc.next();
        //判断用户名是否存在
        if (checkUnique(users, username)) {
            System.out.println("用户" + username + "不存在，请先注册。");
            return false;
        }
        int index = findIndex(users, username);
        User u = users.get(index);
        //判断用户是否可用
        if (!u.getStatus()) {
            System.out.println("用户" + username + "已被禁用，请联系客服。");
            return false;
        }
        //密码
        for (int i = 0; i < 3; i++) {
            System.out.print("请输入密码：");
            String password = sc.next();
            //验证码
            while (true) {
                String rightCode = getCode();
                System.out.print("请输入验证码（" + rightCode + "）：");
                String code = sc.next();
                if (!code.equals(rightCode)) {
                    System.out.println("验证码错误，请重新输入。");
                    continue;
                }
                break;
            }
            if (!u.getPassword().equals(password)) {
                if (i == 2) {
                    System.out.println("密码错误，3次机会已用完，当前账户已被禁用，请联系客服或重新注册。");
                    u.setStatus(false);
                    return false;
                } else {
                    System.out.println("密码错误，请重新输入（还剩" + (2 - i) + "次机会）。");
                    continue;
                }
            } else {
                System.out.println("===================================");
                System.out.println("登录成功！游戏启动。");
                FightingGame fg = new FightingGame();
                fg.gameStart(username);
                break;
            }
        }
        return true;
    }

    //注册的操作
    public void register(ArrayList<User> users) {
        User u = new User();
        Scanner sc = new Scanner(System.in);
        //用户名
        while (true) {
            System.out.print("请输入用户名（长度3-16位，只能由字母、数字组成，不能是纯数字）：");
            String username = sc.next();

            //验证用户名格式是否正确
            //1.长度3-16位
            if (!checkLen(username, 3, 16)) {
                System.out.println("用户名长度不符合要求，必须是3-16位，请重新输入。");
                continue;
            }
            //2.只能由字母、数字组成，不能是纯数字
            if (!checkNum(username)) {
                System.out.println("用户名内容不符合要求，只能由字母、数字组成，不能是纯数字，请重新输入。");
                continue;
            }

            //验证用户名是否唯一
            if (!checkUnique(users, username)) {
                System.out.println("用户名已经存在，请重新输入。");
                continue;
            }

            //用户名符合要求
            u.setUsername(username);
            break;
        }
        //密码
        while (true) {
            System.out.print("请输入密码（长度3-8位，只能是字母+数字的组合）：");
            String password1 = sc.next();
            //验证密码格式是否正确
            //1.长度3-16位
            if (!checkLen(password1, 3, 8)) {
                System.out.println("密码长度不符合要求，必须是3-8位，请重新输入。");
                continue;
            }
            //2.只能是字母+数字的组合
            if (!checkCharAndNum(password1)) {
                System.out.println("密码内容不符合要求，只能是字母+数字的组合，请重新输入。");
                continue;
            }

            //校验两次密码是否输入一致
            System.out.print("请再次输入密码：");
            String password2 = sc.next();
            if (!password1.equals(password2)) {
                System.out.println("两次密码输入不一致，请重新输入。");
                continue;
            }

            //密码符合要求
            u.setPassword(password1);
            break;
        }
        //添加用户
        users.add(u);
        System.out.println("用户" + u.getUsername() + "注册成功！id为" + u.getId());
    }

    //判断字符串长度是否在指定范围内
    public boolean checkLen(String str, int minLen, int maxLen) {
        return (str.length() >= minLen && str.length() <= maxLen);
    }

    //获得字符串字母、数字的个数
    public int[] getCharNumAndNumNum(String str) {
        int charNum = 0, numNum = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) >= '0' && str.charAt(i) <= '9') {
                numNum++;
            } else if ((str.charAt(i) >= 'a' && str.charAt(i) <= 'z') || (str.charAt(i) >= 'A' && str.charAt(i) <= 'Z')) {
                charNum++;
            }
        }
        return new int[]{charNum, numNum};
    }

    //判断字符串是否由字母、数字组成，不能是纯数字
    public boolean checkNum(String str) {
        int[] num = getCharNumAndNumNum(str);
        if (num[0] == 0 || num[0] + num[1] != str.length()) {
            return false;
        } else {
            return true;
        }
    }

    //判断用户名是否唯一
    public boolean checkUnique(ArrayList<User> users, String username) {
        for (int i = 0; i < users.size(); i++) {
            User temp = users.get(i);
            if (temp.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }

    //判断字符串是否字母+数字组合
    public boolean checkCharAndNum(String str) {
        int[] num = getCharNumAndNumNum(str);
        if (num[0] == 0 || num[1] == 0 || num[0] + num[1] != str.length()) {
            return false;
        } else {
            return true;
        }
    }

    //获取验证码
    public String getCode() {
        char[] arr = new char[5];
        Random r = new Random();
        //随机生成数字的位置
        int position = r.nextInt(5);
        //随机生成数字
        arr[position] = (char) (r.nextInt(10) + '0');
        for (int i = 0; i < arr.length; i++) {
            if (i != position) {
                //随机生成字母
                int a1 = r.nextInt(65, 91);
                int a2 = r.nextInt(97, 123);
                int temp = r.nextInt(2);
                if (temp == 0) {
                    arr[i] = (char) a1;
                } else {
                    arr[i] = (char) a2;
                }
            }
        }
        //创建字符串
        String str = new String(arr);
        return str;
    }

    //根据用户名查找其在用户列表中的索引
    public int findIndex(ArrayList<User> users, String username) {
        for (int i = 0; i < users.size(); i++) {
            User temp = users.get(i);
            if (temp.getUsername().equals(username)) {
                return i;
            }
        }
        return -1;
    }
}
