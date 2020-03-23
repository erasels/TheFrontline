package theFrontline.characters;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomPlayer;
import basemod.animations.SpineAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.*;
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
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.PrismaticShard;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theFrontline.TheFrontline;
import theFrontline.actions.utility.ForcedWaitAction;
import theFrontline.actions.utility.SwitchCharacterCombatAction;
import theFrontline.cards.all.Defend;
import theFrontline.cards.all.Forfend;
import theFrontline.cards.all.Strike;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.orbs.CharacterOrb;
import theFrontline.orbs.CombatCharacterSelection;
import theFrontline.powers.abstracts.AbstractFrontlinePower;
import theFrontline.ui.SacredEnergyOrb;
import theFrontline.util.CharacterHelper;
import theFrontline.util.UC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

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

    public static final int MAX_CHARACTERS = 4;

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
        super(name, setClass, new SacredEnergyOrb(), new SpineAnimation(
                TheFrontline.makeCharPath("frontline/MG/AAT52/skeleton.atlas"),
                TheFrontline.makeCharPath("frontline/MG/AAT52/skeleton.json"),
                WAIFU_SCALE
        ));

        initializeClass(null, // required call to load textures and setup energy/loadout.
                THE_FRONTLINE_SHOULDER_1,
                THE_FRONTLINE_SHOULDER_2,
                THE_FRONTLINE_CORPSE,
                getLoadout(), 0.0F, 0.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN));

        dialogX = (drawX + 0.0F * Settings.scale);
        dialogY = (drawY + 220.0F * Settings.scale);
        combatDecks = new HashMap<>();
        scrap = 0;
    }

    public String getPortraitImageName() {
        return BaseMod.getPlayerPortrait(chosenClass);
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0], STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        updateCharInfo();
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage", paramtypez = {DamageInfo.class})
    public static class CharacterDeathMechanic {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn patch(AbstractPlayer __instance, DamageInfo info) {
            FrontlineCharacter p = UC.pc();
            if (p != null && p.getCharacters().size() > 1) {
                AbstractCharacterInfo deadChar = p.getCurrChar();
                deadChar.isDead = true;
                if(AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                    p.state.clearTracks();
                    p.setAni(0, "die", false);
                    UC.att(new SwitchCharacterCombatAction(p.getCharacters().get(0), 0, true));
                    UC.att(new ForcedWaitAction(Settings.ACTION_DUR_LONG));
                } else {
                    p.switchCharacter(p.getCharacters().get(0));
                    p.killChar(deadChar);
                }

                p.isDead = false;
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "deathScreen");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    @Override
    public void update() {
        super.update();
        switchOrbSystem.chars.forEach(CharacterOrb::updateAnimation);
    }

    @Override
    public void combatUpdate() {
        super.combatUpdate();
        switchOrbSystem.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        switchOrbSystem.render(sb);
    }

    public String idOfHealingTarget = "";

    public void heal(int amount, AbstractCharacterInfo ci) {
        if (ci.id.equals(currentCharacter)) {
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
        getCharacters().forEach(AbstractCharacterInfo::atTurnStart);
        super.applyStartOfTurnRelics();
    }

    @Override
    public void onVictory() {
        addAni(2, "victory", false, 0);
        addAni(3, "victoryloop", true, 0);
        super.onVictory();
        getCharacters().forEach(AbstractCharacterInfo::onVictory);
        updateCharInfo();
        combatDecks.clear();
        killChars();
    }

    @Override
    public void useCard(AbstractCard c, AbstractMonster monster, int energyOnUse) {
        //TODO: fix whatever is wrong with the animations ( http://de.esotericsoftware.com/spine-applying-animations )
        if (c.type == AbstractCard.CardType.ATTACK) {
            setAni(0, "attack", false);
            addAni(0, "wait", true, 0.0F);
        }
        super.useCard(c, monster, energyOnUse);
    }

    public void onDeploy() {
        getCurrChar().onDeploy();
    }

    public void onRetreat(AbstractCharacterInfo nextChar) {
        getCurrChar().onRetreat(nextChar);
    }

    public void onSwitch(AbstractCharacterInfo currChar, AbstractCharacterInfo nextChar) {
        getCharacters().forEach(c -> c.onSwitch(currChar, nextChar));
        powers.stream().filter(p -> p instanceof AbstractFrontlinePower).forEachOrdered(p -> ((AbstractFrontlinePower) p).onSwitch(currChar, nextChar));
    }

    public void onAddCharacter(AbstractCharacterInfo newChar) {
        getCharacters().forEach(c -> c.onAddNewCharacter(newChar));
    }

    public void switchCharacter(AbstractCharacterInfo c) {
        updateCharInfo();

        onRetreat(c);

        int cardsInHand = 0;
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            cardsInHand = hand.size();

            while (!hand.isEmpty()) {
                hand.moveToDiscardPile(hand.getTopCard());
            }

            combatDecks.put(currentCharacter, new CombatDeckHandler(this));
        }

        previousCharacter = currentCharacter;
        currentCharacter = c.id;

        loadCharInfo();

        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            CombatDeckHandler cdh = combatDecks.get(c.id);
            if (cdh != null) {
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
        if (getPrevChar() != null) {
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
        switchAnimation(cc);
    }

    public void switchAnimation(AbstractCharacterInfo ci) {
        this.animation = CharacterHelper.getAnimation(ci);
        SpineAnimation spine = (SpineAnimation) animation;
        this.loadAnimation(spine.atlasUrl, spine.skeletonUrl, spine.scale);
        state.clearTracks();
        setAni(0, "wait", true);
        //wait, attack, move, die, victoryloop, victory
        stateData.setMix("attack", "wait", 0.05f);
        if (stateData.getSkeletonData().findAnimation("victoryloop") != null) {
            stateData.setMix("victory", "victoryloop", 0f);
        }
    }

    public void setChar(AbstractCharacterInfo ci) {
        if (!characters.contains(ci)) {
            characters.add(ci);
            ci.costlyInit();
        }
        currentCharacter = ci.id;
        loadCharInfo();
    }

    public void addCharacter(AbstractCharacterInfo ci) {
        if (ci != null) {
            if (!characters.contains(ci)) {
                onAddCharacter(ci);
                characters.add(ci);
                ci.costlyInit();
            } else {
                logger.warn("Tried to add duplicate character.");
            }
        }
    }

    public void killChar(AbstractCharacterInfo ci) {
        ci.dispose();
        characters.remove(ci);
    }

    public void killChars() {
        characters.stream().filter(c -> c.isDead).forEach(AbstractCharacterInfo::dispose);
        characters.removeIf(c -> c.isDead);
    }

    public void switchToNextCharacter() {
        if(characters.size() < 2) return;
        int tmp = characters.indexOf(getCurrChar()) + 1;
        if(tmp > characters.size() - 1) {
            tmp = 0;
        }
        switchCharacter(characters.get(tmp));
    }

    public void updateCharInfo() {
        AbstractCharacterInfo cc = getCurrChar();
        cc.maxHP = maxHealth;
        cc.currentHP = currentHealth;
        cc.masterDeck = masterDeck;
    }

    public ArrayList<AbstractCharacterInfo> getCharacters() {
        return characters.stream().filter(c -> !c.isDead).collect(Collectors.toCollection(ArrayList::new));
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
        if (characters != null && !characters.isEmpty()) {
            for (AbstractCharacterInfo c : characters) {
                if (c.id.equals(s)) {
                    return c;
                }
            }
        }
        return null;
    }

    public AbstractCharacterInfo getCharByName(String s) {
        if (characters != null && !characters.isEmpty()) {
            for (AbstractCharacterInfo c : characters) {
                if (c.name.equals(s)) {
                    return c;
                }
            }
        }
        return null;
    }

    public void setAni(int trackIndex, String animationName, boolean loop) {
        if (stateData.getSkeletonData().findAnimation(animationName) != null) {
            AnimationState.TrackEntry e = state.setAnimation(trackIndex, animationName, loop);
            e.setTimeScale(1f);
        }
    }

    public void addAni(int trackIndex, String animationName, boolean loop, float delay) {
        if (stateData.getSkeletonData().findAnimation(animationName) != null) {
            AnimationState.TrackEntry e = state.addAnimation(trackIndex, animationName, loop, delay);
            e.setTimeScale(1f);
        }
    }

    // Starting Deck
    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            retVal.add(Strike.ID);
        }
        for (int i = 0; i < 4; i++) {
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
    @Override
    public AbstractCard getStartCardForEvent() {
        return new Forfend();
    }

    // The class name as it appears next to your player name in-game
    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        if (getCurrChar() != null) {
            return getCurrChar().name;
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
            draw = new CardGroup(p.drawPile, CardGroup.CardGroupType.DRAW_PILE);
            discard = new CardGroup(p.discardPile, CardGroup.CardGroupType.DISCARD_PILE);
        }
    }
}
