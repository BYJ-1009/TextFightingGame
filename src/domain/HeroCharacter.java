package domain;

import java.util.ArrayList;

public class HeroCharacter extends Character {
    public ArrayList<String> skillList;
    public int MP;//蓝量
    public int maxMP;//最大蓝量

    public HeroCharacter() {
        super();
        skillList = new ArrayList<>();
    }

    public HeroCharacter(String name, int HP, int attack, int defense, int MP) {
        super(name, HP, attack, defense);
        skillList = new ArrayList<>();
        this.MP = MP;
        this.maxMP = MP;
    }

    //展示技能列表
    public String showSkill() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < skillList.size(); i++) {
            sb.append(skillList.get(i));
            if (i != skillList.size() - 1) {
                sb.append("，");
            }
        }
        return sb.toString();
    }

    @Override
    public String show() {
        return super.show() + "[蓝量：" + this.MP + "/" + this.maxMP + "]";
    }

    //消耗蓝量
    public void consumeMP(int amount) {
        this.MP -= amount;
    }

    //恢复蓝量
    public void recoverMP(int amount) {
        this.MP += amount;
        if (this.MP > this.maxMP) {
            this.MP = this.maxMP;
        }
    }
}
