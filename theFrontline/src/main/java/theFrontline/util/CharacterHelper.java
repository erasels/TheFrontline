package theFrontline.util;

import basemod.animations.SpineAnimation;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theFrontline.TheFrontline;
import theFrontline.characters.FrontlineCharacter;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.characters.characterInfo.frontline.FrontlineInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class CharacterHelper {
    /*public static HashMap<AbstractCharacterInfo.Rarity, HashMap<String, Class<? extends AbstractCharacterInfo>>> characterMap = new HashMap<AbstractCharacterInfo.Rarity, HashMap<String, Class<? extends AbstractCharacterInfo>>>() {{
        put(AbstractCharacterInfo.Rarity.BASIC, new HashMap<>());
        put(AbstractCharacterInfo.Rarity.COMMON, new HashMap<>());
        put(AbstractCharacterInfo.Rarity.UNCOMMON, new HashMap<>());
        put(AbstractCharacterInfo.Rarity.RARE, new HashMap<>());
        put(AbstractCharacterInfo.Rarity.EPIC, new HashMap<>());
    }};*/

    public static void addCharacter(AbstractCharacterInfo ci) {
        FrontlineCharacter p = UC.pc();
        if (p != null && ci != null) {
            if (!p.characters.contains(ci)) {
                p.onAddCharacter(ci);
                p.characters.add(ci);
            } else {
                TheFrontline.logger.warn("Tried to add duplicate character.");
            }
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
        ArrayList<FlInstanceInfo> tmp = frontlineMap.get(type).values().stream().filter(flInstanceInfo -> flInstanceInfo.rarity == rarity).collect(Collectors.toCollection(ArrayList::new));
        if (tmp.isEmpty()) {
            tmp = new ArrayList<>(frontlineMap.get(type).values());
        }
        ArrayList<Class<? extends AbstractCharacterInfo>> noDuplicateCheck = UC.pc().characters.stream().filter(tci -> (tci instanceof FrontlineInfo) && (((FrontlineInfo) tci).type != type)).map(ci -> ci.getClass()).collect(Collectors.toCollection(ArrayList::new));
        tmp.removeIf(ci -> noDuplicateCheck.contains(ci.fClass));
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
        int roll = AbstractDungeon.relicRng.random(0, 100);
        if (roll > 89) {
            //10% chance
            return AbstractCharacterInfo.Rarity.EPIC;
        } else if (roll > 69) { //nice
            //20% chance
            return AbstractCharacterInfo.Rarity.RARE;
        } else if (roll > 39) {
            //30%
            return AbstractCharacterInfo.Rarity.UNCOMMON;
        } else {
            //40%
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
        String tmp = TEXT[3] + ci.getArmor() + " NL " +
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

    public static SpineAnimation getAnimation(AbstractCharacterInfo ci) {
        //change Frontline if I ever add more franchises
        if(!ci.isGFL()) return null;
        FrontlineInfo fi = (FrontlineInfo) ci;
        return new SpineAnimation(
                TheFrontline.makeCharPath("frontline/" + fi.type.name() + "/"+ fi.name + "/skeleton.atlas"),
                TheFrontline.makeCharPath("frontline/" + fi.type.name() + "/"+ fi.name + "/skeleton.json"),
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
