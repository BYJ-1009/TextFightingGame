package domain;

//敌方游戏人物
public class EnemyCharacter extends Character {
    public String skill;
    public boolean defending;//表示当前游戏人物是否拥有减少伤害的状态

    public EnemyCharacter() {
        super();
    }

    public EnemyCharacter(String name, int HP, int attack, int defense, String skill) {
        super(name, HP, attack, defense);
        this.skill = skill;
        this.defending = false;
    }

    @Override
    public void sufferDamage(int damage) {
        if (this.defending) {
            damage = (damage / 2) > 1 ? damage / 2 : 1;
            this.defending = false;
        }
        super.sufferDamage(damage);
    }

    @Override
    public String show() {
        return super.show() + "[技能：" + skill + "]";
    }
}
