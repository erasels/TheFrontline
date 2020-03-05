package theFrontline.characters.characterInfo.frontline.SMG;

import com.megacrit.cardcrawl.powers.DexterityPower;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.util.UC;

public class IDW extends SMGInfo {
    public static final String ID = IDW.class.getSimpleName();
    public static final int MAX_HP = 5;
    private static final int DEX_AMT = 2;

    public IDW() {
        super(ID, MAX_HP);
    }

    @Override
    public void onSwitch(AbstractCharacterInfo nextChar) {
        if(nextChar.isGFL(Type.AR)) {
            UC.doPow(UC.p(), new DexterityPower(UC.p(), DEX_AMT));
        }
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
