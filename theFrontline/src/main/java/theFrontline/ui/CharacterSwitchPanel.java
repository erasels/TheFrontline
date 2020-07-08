package theFrontline.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.ui.panels.AbstractPanel;
import theFrontline.characters.FrontlineCharacter;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.ui.buttons.CharacterSwitchButton;
import theFrontline.util.UC;

import java.util.ArrayList;

public class CharacterSwitchPanel extends AbstractPanel {
    private FrontlineCharacter p;
    private int charSize;
    private ArrayList<CharacterSwitchButton> charBtns;

    public CharacterSwitchPanel(float show_x, float show_y, boolean startHidden) {
        super(show_x, show_y, 0, 0, ImageMaster.WHITE_SQUARE_IMG, startHidden);
        FrontlineCharacter p = UC.pc();
        if(p != null) {
            this.p = p;
            charSize = p.getCharacters().size();

            rebuildCharSelection();
        } else {
            isHidden = true;
        }
    }

    public void update() {
        if(!isHidden) {
            if(charSize != p.getCharacters().size()) {
                charSize = p.getCharacters().size();

                rebuildCharSelection();
            }


        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if(!isHidden) {
            //Render shadow background behind buttons

            charBtns.forEach(ci -> ci.render(sb));

            //Render circle around current selected char
        }
    }

    private void rebuildCharSelection() {
        charBtns = new ArrayList<>();
        for(AbstractCharacterInfo ci : p.getCharacters()) {
            charBtns.add(new CharacterSwitchButton(show_x, show_y, ci));
        }
    }

    @Override
    public void updatePositions() {
        //Update character buttons
    }
}
