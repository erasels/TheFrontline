package theFrontline.characters.characterInfo.frontline.SMG;

import com.megacrit.cardcrawl.powers.DexterityPower;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.util.UC;

public class IDW extends SMGInfo {
    public static final String ID = IDW.class.getSimpleName();
    public static final int MAX_HP = 5;
    private static final int DEX_AMT = 1;

    public IDW() {
        super(ID, MAX_HP);
        magicNumber = DEX_AMT;
    }

    @Override
    public void onRetreat(AbstractCharacterInfo nextChar) {
        if(nextChar.isGFL(Type.AR)) {
            UC.doPow(UC.p(), new DexterityPower(UC.p(), getMN()));
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
