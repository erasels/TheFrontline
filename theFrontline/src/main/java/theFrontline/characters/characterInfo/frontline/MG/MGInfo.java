package theFrontline.characters.characterInfo.frontline.MG;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import theFrontline.cards.MG.Shredder;
import theFrontline.cards.MG.SprayNPray;
import theFrontline.cards.MG.Suppression;
import theFrontline.characters.characterInfo.frontline.FrontlineInfo;

import java.util.ArrayList;

public abstract class MGInfo extends FrontlineInfo {
    public static final int BASE_HP = 20;

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
        tmp.add(CardLibrary.getCard(Suppression.ID).makeCopy());
        tmp.add(CardLibrary.getCard(Shredder.ID).makeCopy());
        tmp.add(CardLibrary.getCard(SprayNPray.ID).makeCopy());
        return tmp;
    }
}
