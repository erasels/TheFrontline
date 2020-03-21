package theFrontline.util;

import basemod.animations.SpineAnimation;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import theFrontline.TheFrontline;
import theFrontline.characters.FrontlineCharacter;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.characters.characterInfo.frontline.FrontlineInfo;
import theFrontline.patches.character.SelectionPatches;
import theFrontline.rewards.FrontlinerReward;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class CharacterHelper {
    public static void modifyCombatRewards(CombatRewardScreen crs) {
        if (CardCrawlGame.isInARun()) {
            AbstractRoom room = AbstractDungeon.getCurrRoom();
            if (room instanceof MonsterRoomBoss) {
                crs.rewards.add(new FrontlinerReward(CharacterHelper.getRandomCharacter(CharacterHelper.getRandomRarity(50))));
            } else if(room instanceof MonsterRoomElite || room instanceof TreasureRoom) {
                crs.rewards.add(new FrontlinerReward(CharacterHelper.getRandomCharacter(CharacterHelper.getRandomRarity(25))));
            } else if(room instanceof MonsterRoom) {
                crs.rewards.add(new FrontlinerReward());
            }
        }
    }

    /*public static HashMap<AbstractCharacterInfo.Rarity, HashMap<String, Class<? extends AbstractCharacterInfo>>> characterMap = new HashMap<AbstractCharacterInfo.Rarity, HashMap<String, Class<? extends AbstractCharacterInfo>>>() {{
        put(AbstractCharacterInfo.Rarity.BASIC, new HashMap<>());
        put(AbstractCharacterInfo.Rarity.COMMON, new HashMap<>());
        put(AbstractCharacterInfo.Rarity.UNCOMMON, new HashMap<>());
        put(AbstractCharacterInfo.Rarity.RARE, new HashMap<>());
    }};*/

    public static void addCharacter(AbstractCharacterInfo ci) {
        FrontlineCharacter p = UC.pc();
        if (p != null) {
            p.addCharacter(ci);
        }
    }

    public static HashMap<FrontlineInfo.Type, HashMap<String, FlInstanceInfo>> frontlineMap = new HashMap<FrontlineInfo.Type, HashMap<String, FlInstanceInfo>>() {{
        put(FrontlineInfo.Type.AR, new HashMap<>());
        put(FrontlineInfo.Type.HG, new HashMap<>());
        put(FrontlineInfo.Type.MG, new HashMap<>());
        put(FrontlineInfo.Type.RF, new HashMap<>());
        put(FrontlineInfo.Type.SG, new HashMap<>());
        put(FrontlineInfo.Type.SMG, new HashMap<>());
    }};
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(TheFrontline.makeID("HelperStrings")).TEXT;

    public static void addToMap(AbstractCharacterInfo ci) {
        //characterMap.get(ci.rarity).put(ci.id, ci.getClass());
    }

    public static void addToMap(FrontlineInfo ci) {
        frontlineMap.get(ci.type).put(ci.id, new FlInstanceInfo(ci.rarity, ci.getClass()));
    }

    public static FrontlineInfo retrieveCharacter(String id, FrontlineInfo.Type type) {
        return frontlineMap.get(type).get(id).getChar();
    }

    public static FrontlineInfo getRandomCharacter(FrontlineInfo.Type type, AbstractCharacterInfo.Rarity rarity) {
        ArrayList<FlInstanceInfo> tmp;
        ArrayList<FrontlineInfo.Type> usedTypes = new ArrayList<>();
        FrontlineInfo.Type curType = null;
        do {
            if(curType == null) {
                curType = type;
            } else {
                ArrayList<FrontlineInfo.Type> validTypes = new ArrayList<>(Arrays.asList(FrontlineInfo.Type.values()));
                validTypes.removeIf(usedTypes::contains);
                curType = validTypes.get(AbstractDungeon.relicRng.random(validTypes.size() - 1));
            }
            usedTypes.add(curType);

            tmp = frontlineMap.get(curType).values().stream().filter(flInstanceInfo -> flInstanceInfo.rarity == rarity).collect(Collectors.toCollection(ArrayList::new));
            if (tmp.isEmpty()) {
                tmp.addAll(frontlineMap.get(curType).values());
            }
            for(AbstractCharacterInfo tci : UC.pc().characters) {
                if(tci.isGFL(curType)) {
                    tmp.removeIf(ci -> ci.fClass == tci.getClass());
                }
            }
        } while (tmp.isEmpty());
        return tmp.get(AbstractDungeon.relicRng.random(tmp.size() - 1)).getChar();
    }

    public static FrontlineInfo getRandomCharacter(FrontlineInfo.Type type) {
        return getRandomCharacter(type, AbstractCharacterInfo.Rarity.values()[AbstractDungeon.relicRng.random(AbstractCharacterInfo.Rarity.values().length - 1)]);
    }

    public static FrontlineInfo getRandomCharacter(AbstractCharacterInfo.Rarity rarity) {
        return getRandomCharacter(FrontlineInfo.Type.values()[AbstractDungeon.relicRng.random(FrontlineInfo.Type.values().length - 1)], rarity);
    }

    public static FrontlineInfo getRandomCharacter() {
        return getRandomCharacter(FrontlineInfo.Type.values()[AbstractDungeon.relicRng.random(FrontlineInfo.Type.values().length - 1)]);
    }

    public static AbstractCharacterInfo.Rarity getRandomRarity() {
        return getRandomRarity(0);
    }

    public static AbstractCharacterInfo.Rarity getRandomRarity(int chanceInc) {
        int roll = AbstractDungeon.relicRng.random(0, 100) + chanceInc;
        if (roll > 79) {
            //20% chance
            return AbstractCharacterInfo.Rarity.RARE;
        } else if (roll > 44) {
            //35%
            return AbstractCharacterInfo.Rarity.UNCOMMON;
        } else {
            //45%
            return AbstractCharacterInfo.Rarity.COMMON;
        }
    }

    public static AbstractCharacterInfo getCharacterByClassName(String id) {
        AbstractCharacterInfo ci = null;
        try {
            Class<?> clazz = Class.forName(id);
            ci = (AbstractCharacterInfo) clazz.newInstance();
        } catch (Exception e) {
            System.out.println("Failed to load character from Frontline: " + Arrays.toString(e.getStackTrace()));
        }

        return ci;
    }

    public static Texture getTypeIcon(AbstractCharacterInfo ci) {
        if (ci.isGFL()) {
            return TextureLoader.getTexture(TheFrontline.makeUIPath("TypeIcon/" + ((FrontlineInfo) ci).type.name() + ".png"));
        }
        return TextureLoader.getTexture(TheFrontline.makeUIPath("TypeIcon/Undefined.png"));
    }

    public static AbstractCharacterInfo getPlayerChar() {
        FrontlineCharacter p = UC.pc();
        AbstractCharacterInfo tmp = null;
        if(p != null) {
            tmp = p.getCurrChar();
        }
        return tmp;
    }

    public static String getFlavorStatsString(AbstractCharacterInfo ci, boolean nlBreak) {
        String tmp = TEXT[0] + ci.offence + " NL " +
                TEXT[1] + ci.defence + " NL " +
                TEXT[2] + ci.utility;
        if (!nlBreak) {
            tmp = tmp.replaceAll("NL ", "\n");
        }
        return tmp;
    }

    public static String getStatsString(AbstractCharacterInfo ci, boolean nlBreak) {
        String tmp = TEXT[11] + ci.currentHP + "/" + ci.maxHP + " NL " +
                TEXT[3] + ci.getArmor() + " NL " +
                TEXT[4] + ci.getAddDraw() + " NL " +
                TEXT[5] + ci.getStrike() + " NL " +
                TEXT[6] + ci.getDefend() + " NL ";
        if (!nlBreak) {
            tmp = tmp.replaceAll("NL ", "\n");
        }
        return tmp;
    }

    public static String getEffectString(AbstractCharacterInfo ci) {
        String tmp = TEXT[7] + ci.getDescription();
        tmp = tmp.replaceAll("NL ", "\n");
        return tmp;
    }

    public static String getCharacterSelectionString() {
        return TEXT[8] + "\n" +
                TEXT[9] + SelectionPatches.selectedChar.getChar().fullName + "\n" +
                TEXT[10] + SelectionPatches.backUpChar.getChar().fullName;
    }

    public static SpineAnimation getAnimation(AbstractCharacterInfo ci) {
        //change Frontline if I ever add more franchises
        if (!ci.isGFL()) return null;
        FrontlineInfo fi = (FrontlineInfo) ci;
        return new SpineAnimation(
                TheFrontline.makeCharPath("frontline/" + fi.type.name() + "/" + fi.id + "/skeleton.atlas"),
                TheFrontline.makeCharPath("frontline/" + fi.type.name() + "/" + fi.id + "/skeleton.json"),
                TheFrontline.WAIFU_SCALE);
    }

    public static class FlInstanceInfo {
        public FrontlineInfo.Rarity rarity;
        public Class<? extends FrontlineInfo> fClass;

        public FlInstanceInfo(FrontlineInfo.Rarity rarity, Class<? extends FrontlineInfo> fClass) {
            this.rarity = rarity;
            this.fClass = fClass;
        }

        public FrontlineInfo getChar() {
            try {
                return fClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
