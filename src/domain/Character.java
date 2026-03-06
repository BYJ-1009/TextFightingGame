package domain;

//我方游戏人物
public class Character {
    public String name;
    public int HP;
    public int maxHP;
    public int attack;
    public int defense;

    public Character() {
    }

    //刚创建人物的时候，血量是满的
    public Character(String name, int HP, int attack, int defense) {
        this.name = name;
        this.HP = HP;
        this.maxHP = HP;
        this.attack = attack;
        this.defense = defense;
    }

    //判断是否存活
    public boolean isAlive() {
        return this.HP > 0;
    }

    //恢复血量
    public void heal(int amount) {
        this.HP += amount;
        if (this.HP > this.maxHP) {
            this.HP = this.maxHP;
        }
    }

    //受到伤害
    public void sufferDamage(int damage) {
        this.HP -= damage;
        if (this.HP < 0) {
            this.HP = 0;
        }
    }

    //展示人物属性
    public String show() {
        return "[攻击力：" + this.attack + "][防御力：" + this.defense + "]";
    }
}
