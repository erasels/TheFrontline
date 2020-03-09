package theFrontline.patches.character;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import theFrontline.TheFrontline;
import theFrontline.characters.FrontlineCharacter;
import theFrontline.characters.characterInfo.frontline.AR.F2000;
import theFrontline.characters.characterInfo.frontline.HG.BrenTen;
import theFrontline.characters.characterInfo.frontline.MG.AAT52;
import theFrontline.characters.characterInfo.frontline.RF.G43;
import theFrontline.characters.characterInfo.frontline.SG.M1897;
import theFrontline.characters.characterInfo.frontline.SMG.IDW;
import theFrontline.ui.buttons.CharacterImageButton;
import theFrontline.ui.buttons.CharacterSelectionButton;
import theFrontline.util.CharacterHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class SelectionPatches {
    private static final float YPOS = Settings.HEIGHT * 0.5f;
    private static final float XPOS = Settings.WIDTH * 0.325f;
    private static final float SPACE = 175f * Settings.scale;
    private static boolean resetHP = true;

    public static ArrayList<CharacterSelectionButton> starterCharacters = new ArrayList<>(Arrays.asList(
            new CharacterSelectionButton(XPOS, YPOS, new F2000()),
            new CharacterSelectionButton(XPOS + SPACE, YPOS, new IDW()),
            new CharacterSelectionButton(XPOS + SPACE * 2, YPOS, new BrenTen()),
            new CharacterSelectionButton(XPOS + SPACE * 3, YPOS, new G43()),
            new CharacterSelectionButton(XPOS + SPACE * 4, YPOS, new AAT52()),
            new CharacterSelectionButton(XPOS + SPACE * 5, YPOS, new M1897())
    ));
    public static CharacterSelectionButton selectedChar = starterCharacters.get(0);
    public static CharacterSelectionButton backUpChar = starterCharacters.get(1);

    @SpirePatch(clz = CharacterOption.class, method = "update")
    public static class UpdateBtns {
        @SpirePostfixPatch
        public static void patch(CharacterOption __instance) {
            if (__instance.c instanceof FrontlineCharacter) {
                update();
                if (resetHP) {
                    ReflectionHacks.setPrivate(__instance, CharacterOption.class, "hp", Integer.toString(selectedChar.getChar().maxHP));
                    resetHP = false;
                }
            }
        }
    }

    @SpirePatch(clz = CharacterOption.class, method = "renderRelics")
    public static class RenderBtns {
        @SpirePostfixPatch
        public static void patch(CharacterOption __instance, SpriteBatch sb) {
            if (__instance.c instanceof FrontlineCharacter) {
                render(sb);
            }
        }
    }

    protected static void update() {
        starterCharacters.forEach(CharacterImageButton::update);
    }

    protected static void render(SpriteBatch sb) {
        FontHelper.renderFontLeftDownAligned(sb,
                FontHelper.panelEndTurnFont,
                CharacterHelper.getCharacterSelectionString(),
                XPOS,
                YPOS + SPACE * 1.25f,
                Settings.CREAM_COLOR);
        sb.setColor(Settings.RED_TEXT_COLOR);
        sb.draw(ImageMaster.MAP_CIRCLE_5,
                selectedChar.getX() - ((ImageMaster.MAP_CIRCLE_5.getWidth() * 0.125f) * Settings.scale),
                selectedChar.getY() - ((ImageMaster.MAP_CIRCLE_5.getHeight() * 0.25f)),
                (ImageMaster.MAP_CIRCLE_5.getWidth() * 1.25f) * Settings.scale,
                (ImageMaster.MAP_CIRCLE_5.getHeight() * 1.5f) * Settings.scale);
        sb.setColor(Color.SKY);
        sb.draw(ImageMaster.MAP_CIRCLE_5,
                backUpChar.getX() - ((ImageMaster.MAP_CIRCLE_5.getWidth() * 0.125f) * Settings.scale),
                backUpChar.getY() - ((ImageMaster.MAP_CIRCLE_5.getHeight() * 0.25f)),
                (ImageMaster.MAP_CIRCLE_5.getWidth() * 1.25f) * Settings.scale,
                (ImageMaster.MAP_CIRCLE_5.getHeight() * 1.5f) * Settings.scale);
        sb.setColor(Color.WHITE);
        starterCharacters.forEach(c -> c.render(sb));
        sb.setColor(Color.WHITE);
    }

    @SpirePatch(clz = CharacterOption.class, method = "renderInfo")
    public static class RenderSecondaryHP {
        private static float infx = 200.0F * Settings.scale, infy = Settings.HEIGHT / 2.0F;

        @SpireInsertPatch(locator = Locator.class)
        public static void patch(CharacterOption __instance, SpriteBatch sb) {
            if (__instance.c instanceof FrontlineCharacter) {
                FontHelper.renderSmartText(sb,
                        FontHelper.tipHeaderFont,
                        CharacterOption.TEXT[4] + backUpChar.getChar().maxHP,
                        infx + 18.0F * Settings.scale,
                        infy + (102.0F * Settings.scale) - (FontHelper.getHeight(FontHelper.tipHeaderFont, "HP", 1f) + 5 * Settings.scale),
                        10000.0F,
                        10000.0F,
                        Color.SKY);
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "draw");
                return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher)[1]};
            }
        }
    }

    public static void setSelectedCharacter(CharacterSelectionButton cbtn, boolean backup) {
        if (!backup) {
            if (cbtn == backUpChar) {
                backUpChar = selectedChar;
            }
            selectedChar = cbtn;
            resetHP = true;
        } else {
            if (cbtn == selectedChar) {
                selectedChar = backUpChar;
                resetHP = true;
            }
            backUpChar = cbtn;
        }
    }

    @SpirePatch(clz = CharacterSelectScreen.class, method = "updateButtons")
    public static class SetCharacter {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(CharacterSelectScreen __instance) {
            for (CharacterOption o : __instance.options) {
                if (o.selected) {
                    if (o.c instanceof FrontlineCharacter) {
                        TheFrontline.charsToLoad.add(selectedChar.getChar());
                        TheFrontline.charsToLoad.add(backUpChar.getChar());
                    }
                    break;
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
}
