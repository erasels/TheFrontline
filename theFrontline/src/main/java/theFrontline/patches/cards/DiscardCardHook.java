package theFrontline.patches.cards;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import javassist.CtBehavior;

import java.util.function.Consumer;

@SpirePatch(clz = DiscardAction.class, method = "update")
public class DiscardCardHook {
    public static Consumer<AbstractCard> callback;

    @SpireInsertPatch(locator =  Locator.class, localvars = {"c"})
    public static void patch(DiscardAction __instance, AbstractCard c) {
        if(callback != null) {
            callback.accept(c);
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(CardGroup.class, "moveToDiscardPile");
            return LineFinder.findAllInOrder(ctBehavior, finalMatcher);
        }
    }
}
