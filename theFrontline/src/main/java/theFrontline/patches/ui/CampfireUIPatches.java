package theFrontline.patches.ui;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepEffect;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import theFrontline.util.UC;

public class CampfireUIPatches {
    @SpirePatch(clz = CampfireSleepEffect.class, method = "update")
    public static class RenderCharacters {
        @SpireInstrumentPatch
        public static ExprEditor renderReplace() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractPlayer.class.getName()) && m.getMethodName().equals("render")) {
                        m.replace("{" +
                                "if(" + UC.class.getName() + ".pc() != null) {" +
                                RenderCharacters.class.getName() + ".renderCharacters();" +
                                "} else {" +
                                "$proceed($$);" +
                                "}" +
                                "}");
                    }
                }
            };
        }

        //TODO: Add arraylist with character buttons and render them on the left side, show their HP below them
        public static void renderCharacters() {

        }
    }
}
