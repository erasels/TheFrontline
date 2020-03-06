package theFrontline.util;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import theFrontline.TheFrontline;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;

import java.util.Arrays;

public class CharacterHelper {
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(TheFrontline.makeID("HelperStrings")).TEXT;

    public static AbstractCharacterInfo getCharacterFromID(String id) {
        AbstractCharacterInfo ci = null;
        try {
            Class<?> clazz = Class.forName(id);
            ci = (AbstractCharacterInfo) clazz.newInstance();
        } catch (Exception e) {
            System.out.println("Failed to load character from Frontline: " + Arrays.toString(e.getStackTrace()));
        }

        return ci;
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
        String tmp = TEXT[3] + ci.stats.getArmor() + " NL " +
                TEXT[4] + ci.stats.getAddDraw() + " NL " +
                TEXT[5] + ci.stats.getStrike() + " NL " +
                TEXT[6] + ci.stats.getDefend() + " NL ";
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
}
