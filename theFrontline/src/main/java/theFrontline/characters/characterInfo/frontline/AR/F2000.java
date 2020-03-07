package theFrontline.characters.characterInfo.frontline.AR;

import com.megacrit.cardcrawl.powers.StrengthPower;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.util.UC;

public class F2000 extends ARInfo {
    public static final String ID = F2000.class.getSimpleName();
    public static final int MAX_HP = 5;
    private static final int STR_AMT = 1;

    public F2000() {
        super(ID, MAX_HP);
        magicNumber = STR_AMT;
    }

    @Override
    public void onRetreat(AbstractCharacterInfo nextChar) {
        if(nextChar.isGFL(Type.SMG)) {
            UC.doPow(UC.p(), new StrengthPower(UC.p(), getMN()));
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
        return characterStrings.TEXT[0];
    }
}
