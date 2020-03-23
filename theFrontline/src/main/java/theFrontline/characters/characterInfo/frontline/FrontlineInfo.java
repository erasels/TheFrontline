package theFrontline.characters.characterInfo.frontline;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import theFrontline.TheFrontline;
import theFrontline.cards.all.Defend;
import theFrontline.cards.all.Forfend;
import theFrontline.cards.all.Strike;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.util.CharacterHelper;
import theFrontline.util.TextureLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class FrontlineInfo extends AbstractCharacterInfo {
    public enum Type {
        HG, AR, SMG, RF, MG, SG
    }

    public Type type;

    public FrontlineInfo(String id, int maxHP, Type type) {
        super(id, maxHP);
        this.type = type;
        img = TextureLoader.getTexture(TheFrontline.makeCharPath("frontline/" + type.name() + "/" + id + "/pic.png"));
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    @Override
    public int getScrapValue() {
        int amt = 5;
        switch (getRarity()) {
            case RARE:
                amt *= 4;
                break;
            case UNCOMMON:
                amt *= 2;
        }
        return amt;
    }

    @Override
    public ArrayList<String> initializeAvailableCards() {
        //TODO: Add universal cards
        ArrayList<String> cards = new ArrayList<>(Arrays.asList(Strike.ID, Defend.ID));
        return cards;
    }

    @Override
    public ArrayList<AbstractCard> getStarterDeck() {
        ArrayList<String> cards = new ArrayList<>(Arrays.asList(Strike.ID, Defend.ID, Forfend.ID));
        return cards.stream().map(id -> CardLibrary.getCard(id).makeCopy()).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void costlyInit() {
        if(statusImg == null) {
            statusImg = CharacterHelper.getStatusImages(this);
        }
    }

    @Override
    public void dispose() {
        if(statusImg != null) {
            statusImg.dispose();
        }
    }
}
