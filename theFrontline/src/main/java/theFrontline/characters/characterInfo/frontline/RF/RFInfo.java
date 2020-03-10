package theFrontline.characters.characterInfo.frontline.RF;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import theFrontline.characters.characterInfo.frontline.FrontlineInfo;

import java.util.ArrayList;

public abstract class RFInfo extends FrontlineInfo {
    public static final int BASE_HP = 15;

    public RFInfo(String id, int maxHP) {
        super(id, BASE_HP + maxHP, Type.RF);
    }

    @Override
    protected void initialize() {
        super.initialize();
        col = Color.PURPLE.cpy();
    }

    @Override
    public ArrayList<String> initializeAvailableCards() {
        return super.initializeAvailableCards();
    }

    @Override
    public ArrayList<AbstractCard> getStarterDeck() {
        ArrayList<AbstractCard> tmp = super.getStarterDeck();
        return tmp;
        //TODO: Add base cards
    }
}

