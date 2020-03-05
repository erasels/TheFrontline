package theFrontline.cards.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import theFrontline.cards.abstracts.FrontlineCard;

public class MagicNumber2 extends DynamicVariable {
    @Override
    public String key() {
        return "theFrontline:M2";
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof FrontlineCard) {
            return ((FrontlineCard) card).baseMagicNumber2;
        }
        return -1;
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof FrontlineCard) {
            return ((FrontlineCard) card).magicNumber2;
        }
        return -1;
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof FrontlineCard) {
            return ((FrontlineCard) card).isMagicNumber2Modified;
        }
        return false;
    }

    @Override
    public void setIsModified(AbstractCard card, boolean v) {
        if (card instanceof FrontlineCard) {
            ((FrontlineCard) card).isMagicNumber2Modified = true;
        }
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return false;
    }
}
