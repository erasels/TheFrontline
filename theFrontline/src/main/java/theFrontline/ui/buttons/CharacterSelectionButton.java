package theFrontline.ui.buttons;

import com.megacrit.cardcrawl.core.Settings;
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

    public float getX() {
        return x - ((ci.img.getWidth()/4f) * Settings.scale);
    }

    public float getY() {
        return y;
    }
}
