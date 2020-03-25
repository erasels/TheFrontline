package theFrontline.characters.characterInfo.frontline.SMG;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import theFrontline.cards.SMG.MAC10.SmokeBomb;

import java.util.ArrayList;

public class MAC10 extends SMGInfo {
    public static final String ID = MAC10.class.getSimpleName();
    public static final int MAX_HP = 0;
    private static final int DMG_MULT = 50;

    public MAC10() {
        super(ID, MAX_HP);
        magicNumber = DMG_MULT;
    }

    @Override
    public float atDamageModify(float amount, AbstractCard card) {
        if(currentHP <= (maxHP/2)) {
            return amount * (1f+(getMN()/100f));
        }
        return amount;
    }

    @Override
    public ArrayList<AbstractCard> getStarterDeck() {
        ArrayList<AbstractCard> tmp = super.getStarterDeck();
        tmp.add(CardLibrary.getCard(SmokeBomb.ID).makeCopy());
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
        return Rarity.COMMON;
    }

    @Override
    public String getDescription() {
        return characterStrings.TEXT[0] + getMN() + characterStrings.TEXT[1];
    }
}
