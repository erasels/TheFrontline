package theFrontline.characters.characterInfo.frontline.SG;

import theFrontline.util.UC;

public class M1897 extends SGInfo {
    public static final String ID = M1897.class.getSimpleName();
    public static final int MAX_HP = 5;
    private static final int HP_HEAL = 2;

    public M1897() {
        super(ID, MAX_HP);
        magicNumber = HP_HEAL;
    }

    @Override
    public void onVictory() {
        UC.pc().heal(getMN(), this);
    }

    @Override
    public void setFlavorStats() {
        offence = 2;
        defence = 2;
        utility = 2;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.BASIC;
    }

    @Override
    public String getDescription() {
        return characterStrings.TEXT[0] + getMN() + characterStrings.TEXT[1];
    }
}
