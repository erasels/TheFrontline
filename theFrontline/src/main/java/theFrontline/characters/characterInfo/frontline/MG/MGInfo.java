package theFrontline.characters.characterInfo.frontline.MG;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import theFrontline.characters.characterInfo.frontline.FrontlineInfo;

import java.util.ArrayList;

public abstract class MGInfo extends FrontlineInfo {
    public static final int BASE_HP = 30;

    public MGInfo(String id, int maxHP) {
        super(id, BASE_HP + maxHP, Type.MG);
    }

    @Override
    protected void initialize() {
        super.initialize();
        col = Color.SALMON.cpy();
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
