package theFrontline.characters.characterInfo.frontline.HG;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import theFrontline.cards.HG.Tokarev.Charm;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.util.UC;

import java.util.ArrayList;

public class Tokarev extends HGInfo {
    public static final String ID = Tokarev.class.getSimpleName();
    public static final int MAX_HP = 10;
    private static final int CARD_DRAW = 1;

    public Tokarev() {
        super(ID, MAX_HP);
        magicNumber = CARD_DRAW;
    }

    @Override
    public void onSwitch(AbstractCharacterInfo currChar, AbstractCharacterInfo nextChar) {
        UC.doDraw(magicNumber);
    }

    @Override
    public ArrayList<AbstractCard> getStarterDeck() {
        ArrayList<AbstractCard> tmp = super.getStarterDeck();
        tmp.add(CardLibrary.getCard(Charm.ID).makeCopy());
        return tmp;
    }

    @Override
    public void setFlavorStats() {
        offence = 1;
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
