package theFrontline.patches.general;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.daily.mods.NightTerrors;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.RegalPillow;
import com.megacrit.cardcrawl.ui.campfire.RestOption;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepEffect;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import theFrontline.TheFrontline;
import theFrontline.characters.FrontlineCharacter;
import theFrontline.util.UC;

import java.util.ArrayList;

public class CampfireChangePatches {
    public static final int NONMAIN_HEAL_AMOUNT = 10;
    public static final int REGAL_PILLOW_HEAL_AMT = 5;

    @SpirePatch(clz = CampfireSleepEffect.class, method = "update")
    public static class HealAllOnRest {
        @SpireInstrumentPatch
        public static ExprEditor replaceHeal() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractPlayer.class.getName()) && m.getMethodName().equals("heal")) {
                        m.replace("{" +
                                "if(" + UC.class.getName() + ".pc() != null) {" +
                                CampfireChangePatches.class.getName() + ".healLogic();" +
                                "} else {" +
                                "$proceed($$);" +
                                "}" +
                                "}");
                    }
                }
            };
        }
    }

    public static void healLogic() {
        FrontlineCharacter p = UC.pc();
        int healAmt = NONMAIN_HEAL_AMOUNT;
        if(p.hasRelic(RegalPillow.ID)) {
            healAmt += REGAL_PILLOW_HEAL_AMT;
        }
        boolean fullHeal = ModHelper.isModEnabled(NightTerrors.ID);

        int finalHealAmt = healAmt;
        p.getCharacters().forEach(c -> {
            p.heal(fullHeal?c.maxHP:finalHealAmt, c);
        });
    }

    @SpirePatch(clz = RestOption.class, method = SpirePatch.CONSTRUCTOR)
    public static class DisplayManaOnRest {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(RestOption __instance, boolean active, @ByRef String[] ___description) {
            FrontlineCharacter p = UC.pc();
            if (p != null) {
                ___description[0] = restText();
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(RestOption.class, "updateUsability");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }
    }

    private static String restText() {
        UIStrings s = CardCrawlGame.languagePack.getUIString(TheFrontline.makeID("RestOption"));
        if(ModHelper.isModEnabled(NightTerrors.ID)) {
            return s.TEXT[4];
        }

        String tmp = s.TEXT[0] + NONMAIN_HEAL_AMOUNT + s.TEXT[1];
        if(UC.p().hasRelic(RegalPillow.ID)) {
            tmp += s.TEXT[2] + REGAL_PILLOW_HEAL_AMT + s.TEXT[3];
        }
        return tmp;
    }

    @SpirePatch(clz = RegalPillow.class, method = "getUpdatedDescription")
    public static class FixRPDesciption {
        @SpirePostfixPatch
        public static String patch(String __restult, RegalPillow __instance) {
            RelicStrings s = CardCrawlGame.languagePack.getRelicStrings(TheFrontline.makeID("RegalPillow"));
            return s.DESCRIPTIONS[0] + REGAL_PILLOW_HEAL_AMT + s.DESCRIPTIONS[1];
        }
    }
}
