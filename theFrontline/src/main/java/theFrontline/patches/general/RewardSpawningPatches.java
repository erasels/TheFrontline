package theFrontline.patches.general;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import javassist.CtBehavior;
import theFrontline.util.CharacterHelper;
import theFrontline.util.ScrapHelper;

@SpirePatch(clz = CombatRewardScreen.class, method = "setupItemReward", paramtypez = {})
public class RewardSpawningPatches {
    @SpireInsertPatch(locator = Locator.class)
    public static void modifyRewards(CombatRewardScreen __instance) {
        int tmp = AbstractDungeon.relicRng.counter;
        CharacterHelper.modifyCombatRewards(__instance);
        ScrapHelper.modifyCombatRewards(__instance);
        AbstractDungeon.relicRng.counter = tmp;
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(CombatRewardScreen.class, "positionRewards");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
