package theFrontline.patches.cards;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

public class CostModifiedUntilPlayedPatches {
    @SpirePatch(
            clz = AbstractCard.class,
            method = SpirePatch.CLASS
    )
    public static class Fields {
        public static SpireField<Boolean> isCostModifiedUntilPlayed = new SpireField<>(() -> false);
    }

    @SpirePatch(clz = AbstractCard.class, method = "resetAttributes")
    public static class SkipResettingCost {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<?> Insert(AbstractCard __instance) {
            if (Fields.isCostModifiedUntilPlayed.get(__instance)) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "costForTurn");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }
    }

    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class SetToFalseAfterPlay {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(UseCardAction __instance) {
            //if(__instance.actionType == AbstractGameAction.ActionType.USE) {
            AbstractCard c = (AbstractCard) ReflectionHacks.getPrivate(__instance, UseCardAction.class, "targetCard");
            Fields.isCostModifiedUntilPlayed.set(c, false);
            //}
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "freeToPlayOnce");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }
    }

    //If I ever want Ethereal cards to have their cost unmodified on exhausting from it
    /*@SpirePatch(clz = ExhaustSpecificCardAction.class, method = "update")
    public static class SetToFalseAfterExhaust {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(ExhaustSpecificCardAction __instance) {
            AbstractCard c = (AbstractCard)ReflectionHacks.getPrivate(__instance, ExhaustSpecificCardAction.class, "targetCard");
            ModifiedUntilPlayedSpireField.isCostModifiedUntilPlayed.set(c, false);
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "freeToPlayOnce");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }
    }*/
}
