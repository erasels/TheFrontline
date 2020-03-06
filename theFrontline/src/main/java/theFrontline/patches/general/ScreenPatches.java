package theFrontline.patches.general;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CtBehavior;
import theFrontline.TheFrontline;

public class ScreenPatches {
    @SpirePatch(clz = AbstractDungeon.class, method = "render")
    public static class BlackscreenRenderCall {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(AbstractDungeon __instance, SpriteBatch sb) {
            if(TheFrontline.screen != null) {
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
            if(TheFrontline.screen != null) {
                TheFrontline.screen.update();
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
}
