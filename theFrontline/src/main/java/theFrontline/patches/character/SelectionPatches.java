package theFrontline.patches.character;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import theFrontline.TheFrontline;
import theFrontline.characters.FrontlineCharacter;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.characters.characterInfo.frontline.AR.F2000;
import theFrontline.characters.characterInfo.frontline.SMG.IDW;

import java.util.ArrayList;
import java.util.LinkedList;

public class SelectionPatches {
    private static LinkedList<AbstractCharacterInfo> starterCharacters = new LinkedList<>();
    private static AbstractCharacterInfo selectedChar;
    private static AbstractCharacterInfo backUpChar;


    @SpirePatch(clz = CharacterSelectScreen.class, method = "updateButtons")
    public static class SetCharacter {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(CharacterSelectScreen __instance) {
            for (CharacterOption o : __instance.options) {
                if (o.selected) {
                    if(o.c instanceof FrontlineCharacter) {
                        AbstractCharacterInfo inf;
                        if(selectedChar != null) {
                            inf = selectedChar;
                        } else {
                            inf = new F2000();
                            backUpChar = new IDW();
                        }

                        TheFrontline.charsToLoad.add(inf);
                        TheFrontline.charsToLoad.add(backUpChar);
                    }
                    return;
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(Settings.class, "isDemo");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }
    }

    //https://github.com/Moocowsgomoo/StS-ConstructMod/blob/ca0924ce36aa7cfd1020125bfa9567a22753e668/src/main/java/constructmod/patches/PhoenixBtnPatch.java
}
