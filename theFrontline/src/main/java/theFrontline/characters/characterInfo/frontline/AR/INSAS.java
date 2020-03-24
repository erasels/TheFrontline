package theFrontline.characters.characterInfo.frontline.AR;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import theFrontline.cards.AR.INSAS.SpicyFeast;
import theFrontline.util.UC;

import java.util.ArrayList;

public class INSAS extends ARInfo {
    public static final String ID = INSAS.class.getSimpleName();
    public static final int MAX_HP = 5;
    private static final int AMT = 2;
    private boolean triggered;

    public INSAS() {
        super(ID, MAX_HP);
        magicNumber = AMT;
    }

    @Override
    public void onDeploy() {
        if(!triggered) {
            triggered = true;

            UC.doPow(UC.p(), new StrengthPower(UC.p(), getMN()));
            UC.doPow(UC.p(), new LoseStrengthPower(UC.p(), getMN()));
            UC.doPow(UC.p(), new DexterityPower(UC.p(), getMN()));
            UC.doPow(UC.p(), new LoseDexterityPower(UC.p(), getMN()));
        }
    }

    @Override
    public void atBattleStart() {
        triggered = false;
    }

    @Override
    public ArrayList<AbstractCard> getStarterDeck() {
        ArrayList<AbstractCard> tmp = super.getStarterDeck();
        tmp.add(CardLibrary.getCard(SpicyFeast.ID).makeCopy());
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
