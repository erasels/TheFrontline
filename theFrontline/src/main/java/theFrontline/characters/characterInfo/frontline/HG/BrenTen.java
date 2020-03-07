package theFrontline.characters.characterInfo.frontline.HG;

import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.util.UC;

public class BrenTen extends HGInfo {
    public static final String ID = BrenTen.class.getSimpleName();
    public static final int MAX_HP = 5;
    private static final int STR_AMT = 4;

    public BrenTen() {
        super(ID, MAX_HP);
        magicNumber = STR_AMT;
    }

    @Override
    public void onSwitch(AbstractCharacterInfo currChar, AbstractCharacterInfo nextChar) {
        if(nextChar.isGFL()) {
            UC.doPow(UC.p(), new VigorPower(UC.p(), getMN()));
        }
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
