package theFrontline.characters;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomPlayer;
import basemod.animations.G3DJAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.relics.PrismaticShard;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theFrontline.TheFrontline;
import theFrontline.cards.basic.Defend;
import theFrontline.cards.basic.Strike;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.orbs.CharacterOrb;
import theFrontline.orbs.CombatCharacterSelection;
import theFrontline.ui.SacredEnergyOrb;
import theFrontline.util.UC;

import java.util.ArrayList;
import java.util.HashMap;

import static theFrontline.TheFrontline.*;

public class FrontlineCharacter extends CustomPlayer {
    public static final Logger logger = LogManager.getLogger(TheFrontline.class.getName());

    public static class Enums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass THE_FRONTLINE;
        @SpireEnum(name = "FRONTLINE_GREY_COLOR") // These two HAVE to have the same absolutely identical name.
        public static AbstractCard.CardColor COLOR_FRONTLINE;
        @SpireEnum(name = "FRONTLINE_GREY_COLOR")
        @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
    }

    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 30;
    public static final int MAX_HP = 30;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;

    public static final int MAX_CHARACTERS = 5;

    private static final String ID = makeID("FrontlineCharacter");
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;

    public int scrap;
    public ArrayList<AbstractCharacterInfo> characters = new ArrayList<>();
    public String previousCharacter;
    public String currentCharacter;
    public HashMap<String, CombatDeckHandler> combatDecks;
    public CombatCharacterSelection switchOrbSystem = new CombatCharacterSelection();

    public FrontlineCharacter(String name, PlayerClass setClass) {
        super(name, setClass, new SacredEnergyOrb(), new G3DJAnimation(null, null));

        initializeClass(TheFrontline.makeCharPath("main2.png"), // required call to load textures and setup energy/loadout.
                THE_FRONTLINE_SHOULDER_1,
                THE_FRONTLINE_SHOULDER_2,
                THE_FRONTLINE_CORPSE,
                getLoadout(), 0.0F, 0.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN));

        dialogX = (drawX + 0.0F * Settings.scale);
        dialogY = (drawY + 220.0F * Settings.scale);
        combatDecks = new HashMap<>();
        scrap = 0;
    }

    /*
    super(name, setClass, new SacredEnergyOrb(), new SpineAnimation(
                TheFrontline.makeCharPath("frontline/MG/AAT52/aat52.atlas"),
                TheFrontline.makeCharPath("frontline/MG/AAT52/skeleton.json"),
                1f
                ));

        initializeClass(null, // required call to load textures and setup energy/loadout.
                THE_FRONTLINE_SHOULDER_1,
                THE_FRONTLINE_SHOULDER_2,
                THE_FRONTLINE_CORPSE,
                getLoadout(), 0.0F, 0.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN));

        AnimationState.TrackEntry e = state.setAnimation(0, "wait", true);
        //stateData.setMix("Hit", "Idle", 0.1f);
        e.setTimeScale(1f);
     */

    public String getPortraitImageName() {
        return BaseMod.getPlayerPortrait(chosenClass);
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0], STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public void damage(DamageInfo info) {
        //TODO: Switch cahracter on death logic
        super.damage(info);
        updateCharInfo();
    }

    @Override
    public void update() {
        super.update();
        switchOrbSystem.chars.forEach(CharacterOrb::updateAnimation);
    }

    @Override
    public void combatUpdate() {
        super.combatUpdate();
        switchOrbSystem.chars.forEach(CharacterOrb::update);
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        switchOrbSystem.render(sb);
    }

    public String idOfHealingTarget = "";
    public void heal(int amount, AbstractCharacterInfo ci) {
        if(ci.id.equals(currentCharacter)) {
            heal(amount);
            return;
        }

        idOfHealingTarget = ci.id;
        int tmpHp = currentHealth, tmpMHp = maxHealth;
        currentHealth = ci.currentHP;
        maxHealth = ci.maxHP;
        heal(amount);
        ci.currentHP = currentHealth;
        ci.maxHP = maxHealth;
        currentHealth = tmpHp;
        maxHealth = tmpMHp;
        idOfHealingTarget = "";
    }

    @Override
    public void preBattlePrep() {
        super.preBattlePrep();
        switchOrbSystem.preBattlePrep();
        updateCharInfo();
        previousCharacter = null;

        getCurrChar().preBattlePrep();
    }

    @Override
    public void applyStartOfTurnRelics() {
        characters.forEach(AbstractCharacterInfo::atTurnStart);
        super.applyStartOfTurnRelics();
    }

    @Override
    public void onVictory() {
        super.onVictory();
        characters.forEach(AbstractCharacterInfo::onVictory);
        updateCharInfo();
        combatDecks.clear();
    }

    public void onDeploy() {
        getCurrChar().onDeploy();
    }

    public void onRetreat(AbstractCharacterInfo nextChar) {
        getCurrChar().onRetreat(nextChar);
    }

    public void onSwitch(AbstractCharacterInfo currChar, AbstractCharacterInfo nextChar) {
        characters.forEach(c -> c.onSwitch(currChar, nextChar));
    }

    public void onAddCharacter(AbstractCharacterInfo newChar) {
        characters.forEach(c -> c.onAddNewCharacter(newChar));
    }

    public void switchCharacter(AbstractCharacterInfo c) {
        updateCharInfo();

        onRetreat(c);

        int cardsInHand = 0;
        if(AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            cardsInHand = hand.size();

            while (!hand.isEmpty()) {
                hand.moveToDiscardPile(hand.getTopCard());
            }

            combatDecks.put(currentCharacter, new CombatDeckHandler(this));
        }

        previousCharacter = currentCharacter;
        currentCharacter = c.id;

        loadCharInfo();

        if(AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            CombatDeckHandler cdh = combatDecks.get(c.id);
            if(cdh != null) {
                drawPile = cdh.draw;
                discardPile = cdh.discard;
            } else {
                drawPile.initializeDeck(masterDeck);
                discardPile.clear();
            }
            UC.att(new DrawCardAction(cardsInHand));
        }

        onSwitch(getPrevChar(), c);
        onDeploy();
    }

    public void loadCharInfo() {
        if(getPrevChar() != null) {
            this.masterHandSize -= getPrevChar().getAddDraw();
        }

        AbstractCharacterInfo cc = getCurrChar();
        title = getTitle(this.chosenClass);
        ReflectionHacks.setPrivate(AbstractDungeon.topPanel, TopPanel.class, "title", title);
        maxHealth = cc.maxHP;
        currentHealth = cc.currentHP;
        masterDeck = cc.masterDeck;

        this.masterHandSize += cc.getAddDraw();

        this.img = cc.img;
    }

    public void setChar(AbstractCharacterInfo ci) {
        if(!characters.contains(ci)) {
            characters.add(ci);
        }
        currentCharacter = ci.id;
        loadCharInfo();
    }

    public void updateCharInfo() {
        AbstractCharacterInfo cc = getCurrChar();
        cc.maxHP = maxHealth;
        cc.currentHP = currentHealth;
        cc.masterDeck = masterDeck;
    }

    public int getDamageReduction() {
        return getCurrChar().getArmor();
    }

    public AbstractCharacterInfo getCurrChar() {
        return getChar(currentCharacter);
    }

    public AbstractCharacterInfo getPrevChar() {
        return getChar(previousCharacter);
    }

    public AbstractCharacterInfo getChar(String s) {
        if(characters != null && !characters.isEmpty()) {
            for(AbstractCharacterInfo c : characters) {
                if(c.id.equals(s)) {
                    return c;
                }
            }
        }
        return null;
    }

    // Starting Deck
    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        for(int i = 0; i<4;i++) {
            retVal.add(Strike.ID);
        }
        for(int i = 0; i<4;i++) {
            retVal.add(Defend.ID);
        }
        return retVal;
    }

    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(PrismaticShard.ID);
        //UnlockTracker.markRelicAsSeen(PurificationRod.ID);
        return retVal;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_WHIRLWIND", 1.75f);
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.LONG, true);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_WHIFF_1";
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 3;
    }

    // Should return the card color enum to be associated with your character.
    @Override
    public AbstractCard.CardColor getCardColor() {
        return Enums.COLOR_FRONTLINE;
    }

    // Should return a color object to be used to color the trail of moving cards
    @Override
    public Color getCardTrailColor() {
        return FRONTLINE_GREY;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontBlue;
    }

    // Should return class name as it appears in run history screen.
    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }

    //Which card should be obtainable from the Match and Keep event?
    //TODO: Add other starter card
    @Override
    public AbstractCard getStartCardForEvent() {
        return new Strike();
    }

    // The class name as it appears next to your player name in-game
    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        if(getCurrChar() != null) {
            return getCurrChar().fullName;
        }
        return NAMES[1];
    }

    // Should return a new instance of your character, sending name as its name parameter.
    @Override
    public AbstractPlayer newInstance() {
        return new FrontlineCharacter(name, chosenClass);
    }

    // Should return a Color object to be used to color the miniature card images in run history.
    @Override
    public Color getCardRenderColor() {
        return FRONTLINE_GREY;
    }

    // Should return a Color object to be used as screen tint effect when your
    // character attacks the heart.
    @Override
    public Color getSlashAttackColor() {
        return FRONTLINE_GREY;
    }

    // Should return an AttackEffect array of any size greater than 0. These effects
    // will be played in sequence as your character's finishing combo on the heart.
    // Attack effects are the same as used in DamageAction and the like.
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                AbstractGameAction.AttackEffect.SLASH_VERTICAL,
                AbstractGameAction.AttackEffect.FIRE,};

    }

    // Should return a string containing what text is shown when your character is
    // about to attack the heart. For example, the defect is "NL You charge your
    // core to its maximum..."
    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    // The vampire events refer to the base game characters as "brother", "sister",
    // and "broken one" respectively.This method should return a String containing
    // the full text that will be displayed as the first screen of the vampires event.
    //TODO: Write better vampire text
    @Override
    public String getVampireText() {
        return TEXT[2];
    }

    public class CombatDeckHandler {
        public CardGroup draw, discard;

        public CombatDeckHandler(AbstractPlayer p) {
            draw = p.drawPile;
            discard = p.discardPile;
        }
    }
}
