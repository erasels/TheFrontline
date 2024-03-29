package theFrontline.characters.characterInfo.frontline.HG;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import theFrontline.cards.HG.AimedShot;
import theFrontline.cards.HG.CheerOn;
import theFrontline.cards.HG.PackTactics;
import theFrontline.characters.characterInfo.frontline.FrontlineInfo;

import java.util.ArrayList;

public abstract class HGInfo extends FrontlineInfo {
    public static final int BASE_HP = 15;

    public HGInfo(String id, int maxHP) {
        super(id, BASE_HP + maxHP, Type.HG);
    }

    @Override
    protected void initialize() {
        super.initialize();
        col = Color.BROWN.cpy();
    }

    @Override
    public ArrayList<String> initializeAvailableCards() {
        return super.initializeAvailableCards();
    }

    @Override
    public ArrayList<AbstractCard> getStarterDeck() {
        ArrayList<AbstractCard> tmp = super.getStarterDeck();
        tmp.add(CardLibrary.getCard(AimedShot.ID).makeCopy());
        tmp.add(CardLibrary.getCard(PackTactics.ID).makeCopy());
        tmp.add(CardLibrary.getCard(CheerOn.ID).makeCopy());
        return tmp;
        //TODO: Add base cards
    }
}
