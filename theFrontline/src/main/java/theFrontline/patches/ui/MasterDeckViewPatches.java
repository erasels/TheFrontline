package theFrontline.patches.ui;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.MasterDeckViewScreen;
import theFrontline.characters.FrontlineCharacter;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.util.UC;

import java.util.ArrayList;
import java.util.Arrays;

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

    /*@SpirePatch(clz = MasterDeckViewScreen.class, method = "updatePositions")
    public static class AddAllCards1 {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(MasterDeckViewScreen __instance, @ByRef ArrayList<AbstractCard>[] ___cards) {
            FrontlineCharacter p = UC.pc();
            if(p != null) {
                ___cards[0] = new ArrayList<>(___cards[0]);
                for(AbstractCharacterInfo ci : p.characters) {
                    if(ci != p.getCurrChar()) {
                        for (AbstractCard c : ci.masterDeck.group) {
                            AbstractCardFields.charID.set(c, ci.name);
                            ___cards[0].add(c);
                        }
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(MasterDeckViewScreen.class, "sortOrder");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = MasterDeckViewScreen.class, method = "hideCards")
    public static class AddAllCards2 {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(MasterDeckViewScreen __instance, @ByRef ArrayList<AbstractCard>[] ___cards) {
            FrontlineCharacter p = UC.pc();
            if(p != null) {
                ___cards[0] = new ArrayList<>(___cards[0]);
                for(AbstractCharacterInfo ci : p.characters) {
                    if(ci != p.getCurrChar()) {
                        for (AbstractCard c : ci.masterDeck.group) {
                            AbstractCardFields.charID.set(c, ci.name);
                            ___cards[0].add(c);
                        }
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "size");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    /*public static ArrayList<AbstractCard> cards;
    @SpirePatch(clz = CardGroup.class, method = "renderMasterDeck")
    public static class RenderAllCards {
        @SpirePrefixPatch
        public static void patch(CardGroup __instance, SpriteBatch sb) {
            FrontlineCharacter p = UC.pc();
            if(p != null && cards == null) {
                cards = __instance.group;
                for(AbstractCharacterInfo ci : p.characters) {
                    if(ci != p.getCurrChar()) {
                        for (AbstractCard c : ci.masterDeck.group) {
                            AbstractCardFields.charID.set(c, ci.name);
                            __instance.group.add(c);
                        }
                    }
                }
            }
        }

        @SpirePostfixPatch
        public static void patch2(CardGroup __instance, SpriteBatch sb) {
            __instance.group = cards;
            cards = null;
        }
    }*/
}
