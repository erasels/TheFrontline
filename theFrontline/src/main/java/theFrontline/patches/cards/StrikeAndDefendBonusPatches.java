package theFrontline.patches.cards;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;
import theFrontline.characters.FrontlineCharacter;
import theFrontline.util.UC;

public class StrikeAndDefendBonusPatches {
    @SpirePatch(clz = AbstractCard.class, method = "applyPowers")
    public static class StrikeBonus1 {
        @SpireInsertPatch(locator = Locator.class, localvars = {"tmp"})
        public static void patch(AbstractCard __instance, @ByRef float[] tmp) {
            FrontlineCharacter p = UC.pc();
            if(tmp[0] > -1 && p != null && __instance.hasTag(AbstractCard.CardTags.STARTER_STRIKE)) {
                int atkAdd = p.getCurrChar().getStrike();
                tmp[0]+= atkAdd;

                if(atkAdd != 0) {
                    __instance.isDamageModified = true;
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "relics");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class StrikeBonus2 {
        @SpireInsertPatch(locator = Locator.class, localvars = {"tmp"})
        public static void patch(AbstractCard __instance, AbstractMonster m, @ByRef float[] tmp) {
            FrontlineCharacter p = UC.pc();
            if(tmp[0] > -1 && p != null && __instance.hasTag(AbstractCard.CardTags.STARTER_STRIKE)) {
                int atkAdd = p.getCurrChar().getStrike();
                tmp[0]+= atkAdd;

                if(atkAdd != 0) {
                    __instance.isDamageModified = true;
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "relics");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "applyPowersToBlock")
    public static class BlockModifyHook {
        @SpireInsertPatch(locator = Locator.class, localvars = {"tmp"})
        public static void callHook(AbstractCard __instance, @ByRef float[] tmp) {
            FrontlineCharacter p = UC.pc();
            if(tmp[0] > -1 && p != null && __instance.hasTag(AbstractCard.CardTags.STARTER_DEFEND)) {
                int defAdd = p.getCurrChar().getDefend();
                tmp[0] += defAdd;

                if(defAdd != 0) {
                    __instance.isBlockModified = true;
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }
}
