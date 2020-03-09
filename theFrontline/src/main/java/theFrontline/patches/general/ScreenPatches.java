package theFrontline.patches.general;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CtBehavior;
import theFrontline.TheFrontline;
import theFrontline.screens.CharacterAddScreen;

public class ScreenPatches {
    @SpireEnum
    public static AbstractDungeon.CurrentScreen CHARACTER_MANAGEMENT;

    @SpirePatch(clz = AbstractDungeon.class, method = "render")
    public static class BlackscreenRenderCall {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(AbstractDungeon __instance, SpriteBatch sb) {
            if (TheFrontline.screen != null) {
                TheFrontline.screen.renderController(sb);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(TopPanel.class, "render");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "update")
    public static class UpdateCaller {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(AbstractDungeon __instance) {
            if (TheFrontline.screen != null) {
                TheFrontline.screen.update();
                if (hackyHackHack && !hackyHackHack2) {
                    hackyHackHack = false;
                    hackyHackHack2 = true;
                    AbstractDungeon.deckViewScreen.open();
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "topPanel");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    private static boolean hackyHackHack = false;
    private static boolean hackyHackHack2 = false;

    @SpirePatch(clz = TopPanel.class, method = "updateDeckViewButtonLogic")
    public static class AllowDeckButton {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(TopPanel __instance, @ByRef boolean[] ___deckButtonDisabled, Hitbox ___deckHb) {
            if (TheFrontline.screen instanceof CharacterAddScreen) {
                ___deckButtonDisabled[0] = false;
                ___deckHb.update();

            }
        }

        @SpireInsertPatch(locator = Locator2.class)
        public static void patch2(TopPanel __instance) {
            if (TheFrontline.screen instanceof CharacterAddScreen) {
                if (AbstractDungeon.isScreenUp) {
                    AbstractDungeon.deckViewScreen.open();
                    hackyHackHack = true;
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(InputHelper.class, "justClickedLeft");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }

        private static class Locator2 extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.CurrentScreen.class, "COMBAT_REWARD");
                return new int[]{LineFinder.findAllInOrder(ctBehavior, finalMatcher)[1]};
            }
        }
    }

    @SpirePatch(clz = TopPanel.class, method = "updateMapButtonLogic")
    public static class DisableMapFuckery {
        @SpirePrefixPatch
        public static SpireReturn patch(TopPanel __instance) {
            if (TheFrontline.screen instanceof CharacterAddScreen) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}
