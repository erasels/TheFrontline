package theFrontline.characters.characterInfo.frontline.MG;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import theFrontline.cards.MG.AAT52.StumblingShot;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.powers.GracePower;
import theFrontline.util.UC;

import java.util.ArrayList;

public class AAT52 extends MGInfo {
    public static final String ID = AAT52.class.getSimpleName();
    public static final int MAX_HP = 5;
    private static final int GRACE_AMT = 4;

    public AAT52() {
        super(ID, MAX_HP);
        magicNumber = GRACE_AMT;
    }

    @Override
    public void atTurnStart() {
        AbstractCharacterInfo ci = UC.pc().getPrevChar();
        if(ci != null && ci.isGFL(Type.SG)) {
            UC.doPow(UC.p(), new GracePower(getMN()));
        }
    }

    @Override
    public ArrayList<AbstractCard> getStarterDeck() {
        ArrayList<AbstractCard> tmp = super.getStarterDeck();
        tmp.add(CardLibrary.getCard(StumblingShot.ID).makeCopy());
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
