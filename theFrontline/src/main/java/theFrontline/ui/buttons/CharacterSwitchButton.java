package theFrontline.ui.buttons;

import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.util.UC;

public class CharacterSwitchButton extends CharacterImageButton {
    public CharacterSwitchButton(float x, float y, AbstractCharacterInfo ci) {
        super(x, y, ci, "", "", false, null, 0.75f);
    }

    @Override
    public void onClick() {
        UC.pc().switchCharacter(ci);
    }

    @Override
    public void onRightClick() {

    }


}
