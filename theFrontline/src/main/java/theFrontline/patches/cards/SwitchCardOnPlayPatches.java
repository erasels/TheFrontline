package theFrontline.patches.cards;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CtBehavior;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.util.UC;

public class SwitchCardOnPlayPatches {
    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class Switcher {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(UseCardAction __instance, @ByRef AbstractCard[] ___targetCard) {
            if(___targetCard[0] instanceof FrontlineCard) {
                if(((FrontlineCard)___targetCard[0]).switchCardOnPlay) {
                    AbstractCard tmp = ___targetCard[0].cardsToPreview.makeStatEquivalentCopy();
                    UC.copyCardPosition(___targetCard[0], tmp);
                    ___targetCard[0] = tmp;
                    ___targetCard[0].superFlash();
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "renderHand")
    public static class AlwaysRenderSwitchcardTip {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(AbstractPlayer __instance, SpriteBatch sb) {
            AbstractCard c = __instance.hoveredCard;
                if((__instance.isDraggingCard || __instance.inSingleTargetMode) && c.cardsToPreview != null && c instanceof FrontlineCard && ((FrontlineCard) c).switchCardOnPlay) {
                    c.renderCardPreview(sb);
                }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "renderHoverShadow");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }
}
