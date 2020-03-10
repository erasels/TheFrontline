package theFrontline.characters.characterInfo.frontline.AR;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import theFrontline.cards.AR.Deterrent;
import theFrontline.characters.characterInfo.frontline.FrontlineInfo;

import java.util.ArrayList;

public abstract class ARInfo extends FrontlineInfo {
    public static final int BASE_HP = 25;

    public ARInfo(String id, int maxHP) {
        super(id, BASE_HP + maxHP, Type.AR);
    }

    @Override
    protected void initialize() {
        super.initialize();
        col = Color.SKY.cpy();
    }

    @Override
    public ArrayList<String> initializeAvailableCards() {
        //TODO: Add AR base crafting cards
        return super.initializeAvailableCards();
    }

    @Override
    public ArrayList<AbstractCard> getStarterDeck() {
        ArrayList<AbstractCard> tmp = super.getStarterDeck();
        tmp.add(CardLibrary.getCard(Deterrent.ID).makeCopy());
        return tmp;
    }
}
