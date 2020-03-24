package theFrontline;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.abstracts.CustomSavable;
import basemod.devcommands.ConsoleCommand;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theFrontline.cards.variables.MagicNumber2;
import theFrontline.cards.variables.ShowNumber;
import theFrontline.characters.FrontlineCharacter;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.characters.characterInfo.CharacterSave;
import theFrontline.characters.characterInfo.frontline.FrontlineInfo;
import theFrontline.commands.SetCharCommand;
import theFrontline.patches.character.SelectionPatches;
import theFrontline.patches.general.ScreenPatches;
import theFrontline.patches.ui.MasterDeckViewPatches;
import theFrontline.screens.AbstractScreen;
import theFrontline.ui.ScrapDisplay;
import theFrontline.util.CharacterHelper;
import theFrontline.util.ScrapHelper;
import theFrontline.util.TextureLoader;
import theFrontline.util.UC;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Properties;

@SpireInitializer
public class TheFrontline implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber,
        StartGameSubscriber,
        PostUpdateSubscriber{
    public static final Logger logger = LogManager.getLogger(TheFrontline.class.getName());
    private static String modID;

    public static Properties theFrontlineSettings = new Properties();
    public static final String ENABLE_PLACEHOLDER_SETTINGS = "enablePlaceholder";
    public static boolean enablePlaceholder = true;

    public static AbstractScreen screen;
    public static ScrapDisplay scrapDisplay;

    private static final String MODNAME = "The Frontline";
    private static final String AUTHOR = "erasels";
    private static final String DESCRIPTION = "TODO"; //TODO: Write character Description

    public static final Color FRONTLINE_GREY = CardHelper.getColor(60, 60, 60);

    private static final String ATTACK_FRONTLINE_GREY = "theFrontlineResources/images/512/bg_attack_immortal_red.png";
    private static final String SKILL_FRONTLINE_GREY = "theFrontlineResources/images/512/bg_skill_immortal_red.png";
    private static final String POWER_FRONTLINE_GREY = "theFrontlineResources/images/512/bg_power_immortal_red.png";

    private static final String ENERGY_ORB_FRONTLINE_GREY = "theFrontlineResources/images/512/card_immortal_red_orb.png";
    private static final String CARD_ENERGY_ORB = "theFrontlineResources/images/512/card_small_orb.png";

    private static final String ATTACK_FRONTLINE_GREY_PORTRAIT = "theFrontlineResources/images/1024/bg_attack_immortal_red.png";
    private static final String SKILL_FRONTLINE_GREY_PORTRAIT = "theFrontlineResources/images/1024/bg_skill_immortal_red.png";
    private static final String POWER_FRONTLINE_GREY_PORTRAIT = "theFrontlineResources/images/1024/bg_power_immortal_red.png";
    private static final String ENERGY_ORB_FRONTLINE_GREY_PORTRAIT = "theFrontlineResources/images/1024/card_immortal_red_orb.png";

    private static final String THE_FRONTLINE_BUTTON = "theFrontlineResources/images/charSelect/CharacterButton.png";
    private static final String THE_FRONTLINE_PORTRAIT = "theFrontlineResources/images/charSelect/CharacterPortraitBG.png";
    public static final String THE_FRONTLINE_SHOULDER_1 = "theFrontlineResources/images/char/shoulder.png";
    public static final String THE_FRONTLINE_SHOULDER_2 = "theFrontlineResources/images/char/shoulder2.png";
    public static final String THE_FRONTLINE_CORPSE = "theFrontlineResources/images/char/corpse.png";

    public static final float WAIFU_SCALE = 1f;

    public static final String BADGE_IMAGE = "theFrontlineResources/images/Badge.png";

    public TheFrontline() {
        BaseMod.subscribe(this);

        setModID("theFrontline");

        logger.info("Creating the color " + FrontlineCharacter.Enums.COLOR_FRONTLINE.toString());

        BaseMod.addColor(FrontlineCharacter.Enums.COLOR_FRONTLINE, FRONTLINE_GREY, FRONTLINE_GREY, FRONTLINE_GREY,
                FRONTLINE_GREY, FRONTLINE_GREY, FRONTLINE_GREY, FRONTLINE_GREY,
                ATTACK_FRONTLINE_GREY, SKILL_FRONTLINE_GREY, POWER_FRONTLINE_GREY, ENERGY_ORB_FRONTLINE_GREY,
                ATTACK_FRONTLINE_GREY_PORTRAIT, SKILL_FRONTLINE_GREY_PORTRAIT, POWER_FRONTLINE_GREY_PORTRAIT,
                ENERGY_ORB_FRONTLINE_GREY_PORTRAIT, CARD_ENERGY_ORB);

        theFrontlineSettings.setProperty(ENABLE_PLACEHOLDER_SETTINGS, "FALSE");
        try {
            SpireConfig config = new SpireConfig("theFrontline", "theFrontlineConfig", theFrontlineSettings);
            config.load();
            enablePlaceholder = config.getBool(ENABLE_PLACEHOLDER_SETTINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public static void initialize() {
        TheFrontline frontlineMod = new TheFrontline();
    }

    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + FrontlineCharacter.Enums.THE_FRONTLINE.toString());
        BaseMod.addCharacter(new FrontlineCharacter("the Frontline", FrontlineCharacter.Enums.THE_FRONTLINE), THE_FRONTLINE_BUTTON, THE_FRONTLINE_PORTRAIT, FrontlineCharacter.Enums.THE_FRONTLINE);
    }

    @Override
    public void receivePostInitialize() {
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);
        ModPanel settingsPanel = new ModPanel();

        ModLabeledToggleButton enableNormalsButton = new ModLabeledToggleButton("This is the text which goes next to the checkbox.",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, enablePlaceholder, settingsPanel, (label) -> {
        }, (button) -> {
            enablePlaceholder = button.enabled;
            try {
                SpireConfig config = new SpireConfig("theFrontline", "theFrontlineConfig", theFrontlineSettings);
                config.setBool(ENABLE_PLACEHOLDER_SETTINGS, enablePlaceholder);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        settingsPanel.addUIElement(enableNormalsButton);

        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        BaseMod.addSaveField(makeID("CharacterList"), new CustomSavable<ArrayList<CharacterSave>>() {
            @Override
            public ArrayList<CharacterSave> onSave() {
                FrontlineCharacter p = UC.pc();
                if(p != null) {
                    ArrayList<CharacterSave> tmp = new ArrayList<>();
                    p.characters.forEach(c -> tmp.add(c.getSave()));
                    return tmp;
                }
                return null;
            }

            @Override
            public void onLoad(ArrayList<CharacterSave> i) {
                FrontlineCharacter p = UC.pc();
                if(i != null && p != null) {
                    for(CharacterSave cs : i) {
                        AbstractCharacterInfo ci = CharacterHelper.getCharacterByClassName(cs.id);
                        ci.setSave(cs);
                        p.characters.add(ci);
                        ci.costlyInit();
                    }
                }
            }
        });

        //BaseMod.registerCustomReward(RewardItemTypeEnumPatch.FRONTLINER, );

        BaseMod.addSaveField(makeID("CurrentCharacter"), new CustomSavable<String>() {
            @Override
            public String onSave() {
                return UC.pc()!=null?UC.pc().currentCharacter:null;
            }

            @Override
            public void onLoad(String i) {
                if(i != null && UC.p() instanceof FrontlineCharacter) {
                    UC.pc().currentCharacter = i;
                }
            }
        });

        BaseMod.addSaveField(makeID("CurrentScrap"), new CustomSavable<Integer>() {
            @Override
            public Integer onSave() {
                return ScrapHelper.getScrap();
            }

            @Override
            public void onLoad(Integer i) {
                if(i != null) {
                    ScrapHelper.addScrap(i, false);
                }
            }
        });

        if(scrapDisplay == null) {
            scrapDisplay = new ScrapDisplay();
        }

        new AutoAdd(getModID())
                .packageFilter("theFrontline.characters.characterInfo.frontline")
                .any(FrontlineInfo.class, (info, character) -> CharacterHelper.addToMap(character));

        ConsoleCommand.addCommand("char", SetCharCommand.class);
    }

    @Override
    public void receivePostUpdate() {
        if ((screen != null && screen.isDone) || !CardCrawlGame.isInARun()) {
            screen = null;
            ScreenPatches.openDeckHack = false;
            ScreenPatches.openDeckHackDone = false;
        }

        if(MasterDeckViewPatches.RESET_ON_CLOSE && CardCrawlGame.dungeon != null && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.MASTER_DECK_VIEW) {
            UC.pc().masterDeck.group.removeIf(c -> !(MasterDeckViewPatches.AbstractCardFields.charID.get(c).get(0).equals(UC.pc().currentCharacter)));
            MasterDeckViewPatches.RESET_ON_CLOSE = false;
        }
    }

    public static ArrayList<AbstractCharacterInfo> charsToLoad = new ArrayList<>();
    @Override
    public void receiveStartGame() {
        BaseMod.removeTopPanelItem(scrapDisplay);
        if (AbstractDungeon.player.chosenClass == FrontlineCharacter.Enums.THE_FRONTLINE) {
            if( charsToLoad != null && !charsToLoad.isEmpty()) {
                UC.pc().setChar(charsToLoad.get(0));
                charsToLoad.remove(0);
                for(AbstractCharacterInfo ci : charsToLoad) {
                    CharacterHelper.addCharacter(ci);
                }
                charsToLoad = null;
            } else if (CardCrawlGame.loadingSave) {
                UC.pc().setChar(UC.pc().getCurrChar());
            }
            SelectionPatches.starterCharacters = null;
            SelectionPatches.selectedChar = null;
            SelectionPatches.backUpChar = null;
            BaseMod.addTopPanelItem(scrapDisplay);
        }
    }

    @Override
    public void receiveEditRelics() {
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addDynamicVariable(new MagicNumber2());
        BaseMod.addDynamicVariable(new ShowNumber());

        new AutoAdd(getModID())
                .packageFilter("theFrontline.cards")
                .setDefaultSeen(true)
                .cards();
    }

    @Override
    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(CardStrings.class, getModID() + "Resources/localization/eng/cardStrings.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, getModID() + "Resources/localization/eng/powerStrings.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, getModID() + "Resources/localization/eng/relicStrings.json");
        BaseMod.loadCustomStringsFile(EventStrings.class, getModID() + "Resources/localization/eng/eventStrings.json");
        BaseMod.loadCustomStringsFile(PotionStrings.class, getModID() + "Resources/localization/eng/potionStrings.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, getModID() + "Resources/localization/eng/characterStrings.json");
        BaseMod.loadCustomStringsFile(OrbStrings.class, getModID() + "Resources/localization/eng/orbStrings.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, getModID() + "Resources/localization/eng/uiStrings.json");
        BaseMod.loadCustomStringsFile(MonsterStrings.class, getModID() + "Resources/localization/eng/monsterStrings.json");
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID() + "Resources/localization/eng/keywordStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

    public static String makePath(String resourcePath) {
        return getModID() + "Resources/" + resourcePath;
    }

    public static String makeImagePath(String resourcePath) {
        return getModID() + "Resources/images/" + resourcePath;
    }

    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }

    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }

    public static String makeOrbPath(String resourcePath) {
        return getModID() + "Resources/images/orbs/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }

    public static String makeUIPath(String resourcePath) {
        return getModID() + "Resources/images/ui/" + resourcePath;
    }

    public static String makeCharPath(String resourcePath) {
        return getModID() + "Resources/images/char/" + resourcePath;
    }

    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }


    public static void setModID(String ID) {
        modID = ID;
    }

    public static String getModID() { // NO
        return modID;
    }
}
