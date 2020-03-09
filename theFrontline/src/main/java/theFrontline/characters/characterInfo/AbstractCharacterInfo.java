package theFrontline.characters.characterInfo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardSave;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import theFrontline.characters.characterInfo.frontline.FrontlineInfo;

import java.util.ArrayList;

import static theFrontline.TheFrontline.makeID;

public abstract class AbstractCharacterInfo {
    public enum Rarity {
        BASIC, COMMON, UNCOMMON, RARE, EPIC
    }

    public Color col = Color.WHITE.cpy();
    public String id;
    protected CharacterStrings characterStrings;
    public ArrayList<String> availableCards;
    public Texture img;

    public String name;
    public String fullName;
    public int maxHP, currentHP;
    public CardGroup masterDeck;
    public Stats stats;
    public Rarity rarity;
    public int offence, defence, utility;
    public boolean isDead;
    protected int magicNumber;

    public AbstractCharacterInfo(String id, int maxHP) {
        this.id = id;
        this.maxHP = this.currentHP = maxHP;
        this.rarity = getRarity();
        isDead = false;

        initialize();
    }

    protected void initialize() {
        characterStrings = CardCrawlGame.languagePack.getCharacterString(makeID(id));
        name = characterStrings.NAMES[0];
        fullName = characterStrings.NAMES[1];
        masterDeck = new CardGroup(CardGroup.CardGroupType.MASTER_DECK);
        masterDeck.group.addAll(getStarterDeck());
        stats = new Stats();
        setFlavorStats();
    }

    public abstract ArrayList<AbstractCard> getStarterDeck();

    public abstract Rarity getRarity();

    public abstract String getDescription();

    public void onDeploy(){}
    public void onRetreat(AbstractCharacterInfo nextChar){}
    public void onSwitch(AbstractCharacterInfo currChar, AbstractCharacterInfo nextChar){}
    public void onAddNewCharacter(AbstractCharacterInfo newChar){}
    public void preBattlePrep(){}
    public void atTurnStart() {}
    public void onVictory() {}

    public Color getColor() {
        float tmp;
        switch(rarity) {
            case UNCOMMON:
                tmp = 1.15f;
                break;
            case RARE:
                tmp = 1.3f;
                break;
            case EPIC:
                tmp = 1.5f;
                break;
            default:
                tmp = 1f;
                break;
        }
        col.mul(tmp);
        return col;
    }

    public boolean isGFL() {
        return this instanceof FrontlineInfo;
    }

    public boolean isGFL(FrontlineInfo.Type type) {
        return isGFL() && ((FrontlineInfo) this).type == type;
    }

    public CharacterSave getSave() {
        return new CharacterSave(this.getClass().getName(), stats.getSaveString(), currentHP, maxHP, masterDeck.getCardDeck());
    }

    public abstract int getScrapValue();

    public void setSave(CharacterSave cs) {
        stats.setFromSaveString(cs.stats);
        currentHP = cs.currentHP;
        maxHP = cs.maxHP;
        masterDeck.clear();
        for(CardSave cas : cs.masterDeck) {
            masterDeck.addToTop(CardLibrary.getCopy(cas.id, cas.upgrades, cas.misc));
        }
    }

    public int getMN() {
        return magicNumber;
    }
    public abstract void setFlavorStats();


    public int getArmor() { return stats.armor; }
    public int getAddDraw() { return stats.addDraw; }
    public int getStrike() { return stats.strike; }
    public int getDefend() { return stats.defend; }
    public class Stats {
        protected int armor, addDraw, strike, defend;

        public Stats() {
            armor = 0;
            addDraw = 0;
            strike = 0;
            defend = 0;
        }

        public Stats(int armor, int addDraw) {
            this.armor = armor;
            this.addDraw = addDraw;
        }

        public String getSaveString() {
            return armor + ";" + addDraw + ";" + strike + ";" + defend;
        }

        public void setFromSaveString(String str) {
            String[] tmp = str.split(";");
            if(tmp.length < 4) return;
            armor = Integer.parseInt(tmp[0]);
            addDraw = Integer.parseInt(tmp[1]);
            strike = Integer.parseInt(tmp[2]);
            defend = Integer.parseInt(tmp[3]);
        }
    }
}
