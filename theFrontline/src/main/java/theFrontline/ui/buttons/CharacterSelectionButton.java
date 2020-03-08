package theFrontline.ui.buttons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.patches.character.SelectionPatches;

public class CharacterSelectionButton extends CharacterImageButton {
    public CharacterSelectionButton(float x, float y, AbstractCharacterInfo ci) {
        super(x, y, ci, false);
    }

    @Override
    public void onClick() {
        SelectionPatches.setSelectedCharacter(this, false);
    }

    @Override
    public void onRightClick() {
        SelectionPatches.setSelectedCharacter(this, true);
    }

    public AbstractCharacterInfo getChar() {
        return ci;
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        FontHelper.renderFontLeft(sb, FontHelper.tipHeaderFont, CharacterOption.TEXT[4] + ci.maxHP, x, y, Color.SALMON);
    }

    public float getX() {
        return x - ((ci.img.getWidth()/4f) * Settings.scale);
    }

    public float getY() {
        return y;
    }
}
