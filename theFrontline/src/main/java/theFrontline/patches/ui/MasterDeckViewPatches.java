package theFrontline.patches.ui;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.screens.MasterDeckViewScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import theFrontline.characters.FrontlineCharacter;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.util.UC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class MasterDeckViewPatches {
    public static boolean RESET_ON_CLOSE = false;

    @SpirePatch(
            clz = AbstractCard.class,
            method = SpirePatch.CLASS
    )
    public static class AbstractCardFields {
        public static SpireField<ArrayList<String>> charID = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = MasterDeckViewScreen.class, method = "open")
    public static class AddAllTheCardsMonkaS {
        @SpirePrefixPatch
        public static void patch(MasterDeckViewScreen __instance) {
            FrontlineCharacter p = UC.pc();
            if (p != null) {
                RESET_ON_CLOSE = true;
                AbstractCharacterInfo cur_ci = p.getCurrChar();
                for (AbstractCard c : p.masterDeck.group) {
                    AbstractCardFields.charID.set(c, new ArrayList<>(Arrays.asList(cur_ci.id, cur_ci.name)));
                }
                for (AbstractCharacterInfo ci : p.getCharacters()) {
                    if (!ci.id.equals(cur_ci.id)) {
                        for (AbstractCard c : ci.masterDeck.group) {
                            AbstractCardFields.charID.set(c, new ArrayList<>(Arrays.asList(ci.id, ci.name)));
                            p.masterDeck.group.add(c);
                        }
                    }
                }
            }
        }
    }

    public static String curID, prevID;
    public static int posTracker, lineNumTracker;
    @SpirePatch(clz = MasterDeckViewScreen.class, method = "updatePositions")
    public static class PositionAdjuster1 {
        @SpireInsertPatch(locator = Locator.class, localvars = {"lineNum", "i", "mod"})
        public static void patch(MasterDeckViewScreen __instance, @ByRef int[] lineNum, int i, @ByRef int[] mod) {
            FrontlineCharacter p = UC.pc();
            if (p != null) {
                if(ReflectionHacks.getPrivate(__instance, MasterDeckViewScreen.class, "sortOrder") != null) {
                    return;
                }
                if(i == 0) {
                    prevID = AbstractCardFields.charID.get(p.masterDeck.group.get(i)).get(0);
                    curID = prevID;
                    posTracker = 0;
                    lineNumTracker = 0;
                } else {
                    curID = AbstractCardFields.charID.get(p.masterDeck.group.get(i)).get(0);
                    posTracker++;
                }

                if(posTracker % 5 == 0 && i != 0) {
                    lineNumTracker++;
                } else if(!prevID.equals(curID)) {
                    lineNumTracker++;
                    posTracker = 0;
                }

                prevID = curID;

                mod[0] = posTracker % 5;
                lineNum[0] = lineNumTracker;
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "get");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
            }
        }
    }

    @SpirePatch(clz = MasterDeckViewScreen.class, method = "calculateScrollBounds")
    public static class FixScrollBounds {
        @SpireInsertPatch(locator = Locator.class, localvars = {"scrollTmp"})
        public static void patch(MasterDeckViewScreen __instance, @ByRef int[] scrollTmp) {
            FrontlineCharacter p = UC.pc();
            if(p != null) {
                if(ReflectionHacks.getPrivate(__instance, MasterDeckViewScreen.class, "sortOrder") != null) {
                    return;
                }

                AtomicInteger tmp = new AtomicInteger();

                p.characters.forEach(c -> {
                    tmp.addAndGet(c.masterDeck.size() / 5 - 2);
                    if (c.masterDeck.size() % 5 != 0) {
                        tmp.incrementAndGet();
                    }
                });

                scrollTmp[0] = tmp.get() + p.characters.size() / 2;
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(Settings.class, "DEFAULT_SCROLL_LIMIT");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
            }
        }
    }
}
