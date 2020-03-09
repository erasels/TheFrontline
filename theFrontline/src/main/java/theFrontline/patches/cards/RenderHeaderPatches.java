package theFrontline.patches.cards;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import theFrontline.patches.ui.MasterDeckViewPatches;
import theFrontline.util.UC;

import java.util.ArrayList;

public class RenderHeaderPatches {
    @SpirePatch(clz = AbstractCard.class, method = "renderCard")
    public static class RenderHeader {
        @SpirePostfixPatch
        public static void patch(AbstractCard __instance, SpriteBatch sb, boolean hovered, boolean selected) {
            ArrayList<String> tmp = MasterDeckViewPatches.AbstractCardFields.charID.get(__instance);
            if (!Settings.hideCards && tmp != null) {
                float xPos, yPos, offsetY;
                BitmapFont font;
                String text = tmp.get(1);
                if (text == null || __instance.isFlipped || __instance.isLocked || __instance.transparency <= 0.0F)
                    return;
                if (false) { //isCardPopup
                    font = FontHelper.SCP_cardTitleFont_small;
                    xPos = Settings.WIDTH / 2.0F + 10.0F * Settings.scale;
                    yPos = Settings.HEIGHT / 2.0F + 393.0F * Settings.scale;
                    offsetY = 0.0F;
                } else {
                    font = FontHelper.cardTitleFont_small;
                    xPos = __instance.current_x;
                    yPos = __instance.current_y;
                    offsetY = 400.0F * Settings.scale * __instance.drawScale / 2.0F;
                }
                BitmapFont.BitmapFontData fontData = font.getData();
                float originalScale = fontData.scaleX;
                float scaleMulti = 0.8F;
                int length = text.length();
                if (length > 20) {
                    scaleMulti -= 0.02F * (length - 20);
                    if (scaleMulti < 0.5F)
                        scaleMulti = 0.5F;
                }
                fontData.setScale(scaleMulti * (false ? 1.0F : __instance.drawScale));
                Color color = UC.pc().getChar(tmp.get(0)).getColor();
                color.a = __instance.transparency;
                FontHelper.renderRotatedText(sb, font, text, xPos, yPos, 0.0F, offsetY, __instance.angle, true, color);
                fontData.setScale(originalScale);
            }
        }
    }

}
