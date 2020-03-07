package theFrontline.patches.character;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CtBehavior;
import theFrontline.characters.FrontlineCharacter;
import theFrontline.util.UC;

public class HealingEffectPatches {
    @SpirePatch(clz = AbstractCreature.class, method = "heal", paramtypez = {int.class, boolean.class})
    public static class HealEffectLogic {
        @SpireInsertPatch(locator =  Locator.class)
        public static SpireReturn<Void> patch(AbstractCreature __instance, int amt, boolean showEffect) {
            FrontlineCharacter p = UC.pc();
            if(p != null && !p.idOfHealingTarget.equals(p.currentCharacter)) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(TopPanel.class, "panelHealEffect");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }
}
