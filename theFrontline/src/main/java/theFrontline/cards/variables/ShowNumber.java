package theFrontline.cards.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import theFrontline.cards.abstracts.FrontlineCard;

public class ShowNumber extends DynamicVariable {
    @Override
    public String key() {
        return "theFrontline:SN";
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof FrontlineCard) {
            return ((FrontlineCard) card).baseShowNumber;
        }
        return -1;
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof FrontlineCard) {
            return ((FrontlineCard) card).showNumber;
        }
        return -1;
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof FrontlineCard) {
            return ((FrontlineCard) card).isShowNumberModified;
        }
        return false;
    }

    @Override
    public void setIsModified(AbstractCard card, boolean v) {
        if (card instanceof FrontlineCard) {
            ((FrontlineCard) card).isShowNumberModified = true;
        }
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return false;
    }
}
