package theFrontline.patches.cards;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

public class CardModPatches {
    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class Fields {
        public static SpireField<Integer> originalDmg = new SpireField<>(() -> -1);
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
    public static class ChangeBack {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse) {
            int tmp = Fields.originalDmg.get(c);
            if(tmp > -1) {
                c.baseDamage = tmp;
            }
        }

        //right after c.use()
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(GameActionManager.class, "addToBottom");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }
}
