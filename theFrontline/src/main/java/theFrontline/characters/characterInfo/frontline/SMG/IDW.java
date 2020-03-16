package theFrontline.characters.characterInfo.frontline.SMG;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import theFrontline.cards.SMG.IDW.Cathletics;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.util.UC;

import java.util.ArrayList;

public class IDW extends SMGInfo {
    public static final String ID = IDW.class.getSimpleName();
    public static final int MAX_HP = 5;
    private static final int DEX_AMT = 3;

    public IDW() {
        super(ID, MAX_HP);
        magicNumber = DEX_AMT;
    }

    @Override
    public void onRetreat(AbstractCharacterInfo nextChar) {
        if(nextChar.isGFL(Type.AR)) {
            UC.doPow(UC.p(), new DexterityPower(UC.p(), getMN()));
            UC.doPow(UC.p(), new LoseDexterityPower(UC.p(), getMN()));
        }
    }

    @Override
    public ArrayList<AbstractCard> getStarterDeck() {
        ArrayList<AbstractCard> tmp = super.getStarterDeck();
        tmp.add(CardLibrary.getCard(Cathletics.ID).makeCopy());
        return tmp;
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
