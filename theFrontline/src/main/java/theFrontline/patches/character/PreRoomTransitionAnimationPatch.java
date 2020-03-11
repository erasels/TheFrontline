package theFrontline.patches.character;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import theFrontline.characters.FrontlineCharacter;
import theFrontline.util.UC;

@SpirePatch(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = {SaveFile.class})
public class PreRoomTransitionAnimationPatch {
    @SpirePrefixPatch
    public static void patch(AbstractDungeon __instance, SaveFile sf) {
        FrontlineCharacter p = UC.pc();
        if(p != null) {
            p.state.clearTracks();
            p.setAni(0, "wait", true);
        }
    }
}
