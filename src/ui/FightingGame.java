package ui;

import domain.EnemyCharacter;
import domain.HeroCharacter;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class FightingGame {
    //启动游戏
    public void gameStart(String username) {
        //显示游戏开始的标题
        System.out.println("———————————————————————————————————");
        System.out.println("🎮" + username + "欢迎来到文字格斗游戏🎮");
        System.out.println("———————————————————————————————————");
        //创建玩家角色（名字+属性分配）
        HeroCharacter player = createPlayerCharacter(username);
        //显示角色属性
        System.out.println("角色创建成功！");
        System.out.println("🌟角色名称：" + player.name);
        System.out.println("🌟初始属性：" + "[生命值：" + player.HP + "/" + player.maxHP + "]" + player.show());
        System.out.println("🌟拥有技能：" + player.showSkill());

        //创建敌方角色
        ArrayList<EnemyCharacter> enemyList = new ArrayList<>();
        enemyList.add(new EnemyCharacter("初级战士", 95, 14, 7, "猛击（130%伤害）"));
        enemyList.add(new EnemyCharacter("敏捷刺客", 80, 13, 6, "快速攻击（2次80%伤害）"));
        enemyList.add(new EnemyCharacter("重装坦克", 130, 11, 10, "防御姿态（下回合所受伤害减半）"));
        enemyList.add(new EnemyCharacter("神秘法师", 70, 17, 5, "火球术（170%伤害）"));

        //准备战斗
        int count = 1;//战斗场数
        int wins = 0;//战斗胜利数
        //游戏当中，玩家依次与多个敌人战斗，直到玩家生命值为0，游戏才会结束
        while (player.isAlive()) {
            //重置敌人属性，敌人属性每场HP+5，ATK+2，DEF+1，第二场开始增加
            if (count > 1) {
                for (int i = 0; i < enemyList.size(); i++) {
                    EnemyCharacter c = enemyList.get(i);
                    c.maxHP += 5;
                    c.HP = c.maxHP;
                    c.attack += 2;
                    c.defense += 1;
                    c.defending = false;
                }
            }
            //随机选择敌人
            Random r = new Random();
            int index = r.nextInt(enemyList.size());
            EnemyCharacter enemy = enemyList.get(index);
            //开始战斗
            System.out.println("===================================");
            System.out.println("         ⚔️第" + count + "场战斗开始！ ");
            System.out.println("===================================");
            System.out.print("对手：");
            System.out.println(enemy.name + "[生命值：" + enemy.HP + "/" + enemy.maxHP + "]" + enemy.show());
            int round = 1;//每场战斗的战斗回合数
            while (player.isAlive()) {
                System.out.println("———————————————————————————————————");
                System.out.println("⚔️第" + round + "回合：");
                if (round > 1) {
                    player.recoverMP(10);
                    System.out.println("🌟恢复蓝量：" + 10);
                }
                //双方血条
                System.out.print(getBloodBar(player.name, player.HP, player.maxHP) + "  ");
                System.out.println(player.show());
                System.out.print(getBloodBar(enemy.name, enemy.HP, enemy.maxHP) + "  ");
                System.out.println(enemy.show());
                //玩家回合
                playerTurn(player, enemy);
                //判断敌方血量是否为0
                if (!enemy.isAlive()) {
                    System.out.println("🎉恭喜！你击败了 " + enemy.name + " ！");
                    wins++;
                    break;
                }
                //敌方回合
                enemyTurn(enemy, player);
                //判断玩家血量是否为0
                if (!player.isAlive()) {
                    System.out.println("☠️很遗憾，你被 " + enemy.name + " 击败了！");
                    break;
                }
                //玩家没有被击败，开启下一回合
                round++;
            }
            //跟一个敌人的战斗结束
            if (player.isAlive()) {
                int healHP = (int) (player.maxHP * 0.4);
                player.heal(healHP);
                int healMP = (int) (player.maxMP * 0.5);
                player.recoverMP(healMP);
                System.out.println("❤️战斗结束！你恢复了 " + healHP + " 点生命值，" + healMP + " 点蓝量");
                System.out.println("🏆当前胜场：" + wins);
                System.out.println("===================================");
            }
            //每胜利3场获得属性提升
            if (player.isAlive() && wins > 0 && wins % 3 == 0) {
                System.out.println("✨恭喜！你已连胜 " + wins + " 场，获得了属性提升！");
                System.out.println("✨最大生命值+20，攻击力+3，防御力+2，最大蓝量+15");
                player.maxHP += 20;
                player.attack += 3;
                player.defense += 2;
                player.maxMP += 15;
                System.out.println("🌟当前属性：" + "[生命值：" + player.HP + "/" + player.maxHP + "]" + player.show());
            }
            int flag = 0;
            while (player.isAlive()) {
                if (flag != 0) {
                    break;
                }
                System.out.print("继续下一场战斗？（y/n）：");
                Scanner sc = new Scanner(System.in);
                String choice = sc.next();
                if (choice.equalsIgnoreCase("y")) {
                    flag = 1;
                    count++;
                } else if (choice.equalsIgnoreCase("n")) {
                    flag = 2;
                } else {
                    System.out.println("没有这个选项，请重新输入。");
                }
            }
            if (flag == 2 || !player.isAlive()) {
                break;
            }
        }
        System.out.println("===================================");
        System.out.println("🎮游戏结束！");
        System.out.println("🏆总胜场：" + wins);
        System.out.println("感谢游玩文字版格斗游戏！期待下次再见！");
        System.out.println("===================================");
        System.exit(0);
    }

    //创建玩家角色
    public HeroCharacter createPlayerCharacter(String username) {
        System.out.println("创建您的角色：");
        System.out.println("角色名称：" + username);
        //属性分配
        int points = 10;
        System.out.println("请分配属性点（共10点）：");
        System.out.println("1.生命值（初始值80 HP，每点+5 HP）");
        System.out.println("2.攻击力（初始值12 ATK，每点+1 ATK）");
        System.out.println("3.防御力（初始值6 DEF，每点+1 DEF）");
        String[] attributes = {"生命值", "攻击力", "防御力"};
        int[] values = new int[3];
        for (int i = 0; i < attributes.length; i++) {
            System.out.print("分配点数到 " + attributes[i] + "（剩余点数：" + points + "）：");
            Scanner sc = new Scanner(System.in);
            values[i] = sc.nextInt();
            if (values[i] < 0) {
                System.out.println("无效输入，默认分配0点。");
                values[i] = 0;
            } else if (values[i] > points) {
                System.out.println("属性点不足，剩余属性点全部分配到" + attributes[i] + "。");
                values[i] = points;
            }
            points -= values[i];
        }

        //创建玩家角色的对象
        HeroCharacter player = new HeroCharacter(username, 80 + values[0] * 5, 12 + values[1], 6 + values[2], 80);
        player.skillList.add("普通攻击（无消耗，造成基础伤害）");
        player.skillList.add("强力一击（消耗25蓝量，造成180%攻击力的伤害）");
        player.skillList.add("生命回复（消耗20蓝量，随机恢复12-25点HP）");
        return player;
    }

    //获取血条
    public String getBloodBar(String name, int HP, int maxHP) {
        int barLength = 20;
        int filled = (int) ((HP * 1.0 / maxHP) * barLength);
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("：[");
        for (int i = 1; i <= filled; i++) {
            sb.append("■");
        }
        for (int i = 1; i <= barLength - filled; i++) {
            sb.append("□");
        }
        sb.append("] ").append(HP).append("/").append(maxHP).append(" HP");
        return sb.toString();
    }

    //玩家回合
    public void playerTurn(HeroCharacter player, EnemyCharacter enemy) {
        System.out.println("=====你的回合=====");
        System.out.println("1.普通攻击（无消耗，造成基础伤害）");
        System.out.println("2.强力一击（消耗25蓝量，造成180%攻击力的伤害）");
        System.out.println("3.生命回复（消耗20蓝量，随机恢复12-25点HP）");
        int flag = 0;
        while (true) {
            if (flag == 1) {
                break;
            }
            System.out.print("选择行动（1-3）：");
            Scanner sc = new Scanner(System.in);
            String choice = sc.next();
            switch (choice) {
                case "1":
                    flag = 1;
                    int damage1 = calculateDamage(player.attack, enemy.defense);
                    System.out.println("💥你对 " + enemy.name + " 使用了普通攻击，造成了 " + damage1 + " 点伤害。");
                    enemy.sufferDamage(damage1);
                    break;
                case "2":
                    if (player.MP >= 25) {
                        flag = 1;
                        player.consumeMP(25);
                        int damage2 = calculateDamage((int) (player.attack * 1.8), enemy.defense);
                        System.out.println("💪你对 " + enemy.name + " 使用了强力一击，造成了 " + damage2 + " 点伤害！");
                        enemy.sufferDamage(damage2);
                    } else {
                        System.out.println("蓝量不足，攻击失败，请重新选择。");
                    }
                    break;
                case "3":
                    if (player.MP >= 20) {
                        flag = 1;
                        player.consumeMP(20);
                        Random r = new Random();
                        int healHP = r.nextInt(12, 26);
                        System.out.println("❤️你使用了生命回复，恢复了 " + healHP + " 点生命值！");
                        player.heal(healHP);
                    } else {
                        System.out.println("蓝量不足，恢复失败，请重新选择。");
                    }
                    break;
                default:
                    System.out.println("没有这个操作，请重新选择。");
                    break;
            }
        }
    }

    //敌方回合
    private void enemyTurn(EnemyCharacter enemy, HeroCharacter player) {
        System.out.println("=====" + enemy.name + "的回合=====");
        String action = "普通攻击";
        Random r = new Random();
        int num = r.nextInt(10);//0-4普通攻击，5-9技能攻击
        if (num >= 5) {
            action = enemy.skill;
        }
        switch (action) {
            case "普通攻击":
                int damage1 = calculateDamage(enemy.attack, player.defense);
                System.out.println("💥" + enemy.name + " 对你使用了普通攻击，造成了 " + damage1 + " 点伤害。");
                player.sufferDamage(damage1);
                break;
            case "猛击（130%伤害）":
                int damage2 = calculateDamage((int) (enemy.attack * 1.3), player.defense);
                System.out.println("💥" + enemy.name + " 对你使用了猛击，造成了 " + damage2 + " 点伤害。");
                player.sufferDamage(damage2);
                break;
            case "快速攻击（2次80%伤害）":
                int damage3 = 0;
                for (int i = 0; i < 2; i++) {
                    int temp = calculateDamage((int) (enemy.attack * 0.8), player.defense);
                    damage3 += temp;
                }
                System.out.println("🗡️" + enemy.name + " 对你使用了快速攻击，造成了 " + damage3 + " 点伤害。");
                player.sufferDamage(damage3);
                break;
            case "防御姿态（下回合所受伤害减半）":
                System.out.println("🛡️" + enemy.name + " 使用了防御姿态，其下回合所受伤害减半。");
                enemy.defending = true;
                break;
            case "火球术（170%伤害）":
                int damage4 = calculateDamage((int) (enemy.attack * 1.7), player.defense);
                System.out.println("🔥" + enemy.name + " 对你使用了火球术，造成了 " + damage4 + " 点伤害。");
                player.sufferDamage(damage4);
                break;
        }
    }

    //计算伤害
    public int calculateDamage(int attack, int defense) {
        int damage = attack - defense;
        if (damage < 1) {
            damage = 1;
        }
        return damage;
    }
}
