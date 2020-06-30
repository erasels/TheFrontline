package theFrontline.patches.character;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;
import theFrontline.characters.FrontlineCharacter;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.util.UC;

public class DamageModifyHookPatches {
    @SpirePatch(clz = AbstractCard.class, method = "applyPowers")
    public static class AbstractCardDamageGiveHook {
        //TODO: Replace with card mods
        @SpireInsertPatch(locator = LocatorSingle.class, localvars = {"tmp"})
        public static void callSingleHook(AbstractCard __instance, @ByRef float[] tmp) {
            FrontlineCharacter p = UC.pc();
            if(p != null) {
                AbstractCharacterInfo ci = p.getCurrChar();

                tmp[0] = ci.atDamageModify(tmp[0], __instance);
            }
        }

        @SpireInsertPatch(locator = LocatorMulti.class, localvars = {"tmp", "i"})
        public static void callMultiHook(AbstractCard __instance, float[] tmp, int i) {
            FrontlineCharacter p = UC.pc();
            if(p != null) {
                AbstractCharacterInfo ci = p.getCurrChar();

                tmp[i] = ci.atDamageModify(tmp[i], __instance);
            }
        }

        private static class LocatorSingle extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "relics");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }

        private static class LocatorMulti extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "relics");
                return new int[]{LineFinder.findAllInOrder(ctBehavior, finalMatcher)[1]};
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class AbstractCardDamageGiveHook2 {
        @SpireInsertPatch(locator = LocatorSingle.class, localvars = {"tmp"})
        public static void callSingleHook(AbstractCard __instance, AbstractMonster m, @ByRef float[] tmp) {
            FrontlineCharacter p = UC.pc();
            if(p != null) {
                AbstractCharacterInfo ci = p.getCurrChar();

                tmp[0] = ci.atDamageModify(tmp[0], __instance);
            }
        }

        @SpireInsertPatch(locator = LocatorMulti.class, localvars = {"tmp", "i"})
        public static void callMultiHook(AbstractCard __instance, AbstractMonster m, float[] tmp, int i) {
            FrontlineCharacter p = UC.pc();
            if(p != null) {
                AbstractCharacterInfo ci = p.getCurrChar();

                tmp[i] = ci.atDamageModify(tmp[i], __instance);
            }
        }

        private static class LocatorSingle extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "relics");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }

        private static class LocatorMulti extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "relics");
                return new int[]{LineFinder.findAllInOrder(ctBehavior, finalMatcher)[1]};
            }
        }
    }
}
