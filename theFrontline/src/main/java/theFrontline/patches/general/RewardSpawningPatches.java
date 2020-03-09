package theFrontline.patches.general;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import javassist.CtBehavior;
import theFrontline.util.CharacterHelper;
import theFrontline.util.ScrapHelper;

@SpirePatch(clz = CombatRewardScreen.class, method = "setupItemReward", paramtypez = {})
public class RewardSpawningPatches {
    @SpireInsertPatch(locator = Locator.class)
    public static void modifyRewards(CombatRewardScreen __instance) {
        CharacterHelper.modifyCombatRewards(__instance);
        ScrapHelper.modifyCombatRewards(__instance);
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(CombatRewardScreen.class, "positionRewards");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
