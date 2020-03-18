package theFrontline.patches.cards;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CtBehavior;
import theFrontline.cards.abstracts.FrontlineCard;

public class SwitchCardOnPlayPatches {
    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class Switcher {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(UseCardAction __instance, @ByRef AbstractCard[] ___targetCard) {
            if(___targetCard[0] instanceof FrontlineCard) {
                if(((FrontlineCard)___targetCard[0]).switchCardOnPlay) {
                    ___targetCard[0] = ___targetCard[0].cardsToPreview.makeStatEquivalentCopy();
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
}
