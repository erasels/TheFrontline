package theFrontline.characters.characterInfo.frontline.HG;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import theFrontline.cards.HG.BrenTen.StarOfHope;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.util.UC;

import java.util.ArrayList;

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
    public ArrayList<AbstractCard> getStarterDeck() {
        ArrayList<AbstractCard> tmp = super.getStarterDeck();
        tmp.add(CardLibrary.getCard(StarOfHope.ID).makeCopy());
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
