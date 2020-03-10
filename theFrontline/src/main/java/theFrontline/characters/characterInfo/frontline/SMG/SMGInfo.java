package theFrontline.characters.characterInfo.frontline.SMG;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import theFrontline.characters.characterInfo.frontline.FrontlineInfo;

import java.util.ArrayList;

public abstract class SMGInfo extends FrontlineInfo {
    public static final int BASE_HP = 15;

    public SMGInfo(String id, int maxHP) {
        super(id, BASE_HP + maxHP, Type.SMG);
        col = Color.GREEN.cpy();
    }

    @Override
    protected void initialize() {
        super.initialize();
        //TODO: Add SMG base crafting cards
        //availableCards.add
    }

    @Override
    public ArrayList<AbstractCard> getStarterDeck() {
        ArrayList<AbstractCard> tmp = super.getStarterDeck();
        return tmp;
        //TODO: Add base cards
    }
}