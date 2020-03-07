package theFrontline.vfx.combat.unique;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.vfx.general.MakeImageRunEffect;

public class CharacterRunEffect extends MakeImageRunEffect {
    protected float vY;
    protected float baseY;
    protected boolean startHop;
    public CharacterRunEffect(float x, float y, Texture img, float speed) {
        super(x, y, img, speed);
        baseY = y;
        float tmp = 0;
        for(AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            float yCoord = (m.hb.cY + (m.hb.height/2f)) - AbstractDungeon.floorY;
            if(yCoord > tmp) {
                tmp = yCoord;
            }
        }

        vY = (tmp * Settings.scale) + (17f * Settings.scale);
    }



    @Override
    public void update() {
        super.update();
        for(AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if(!m.isDeadOrEscaped()) {
                if((m.drawX - x) < (img.getWidth()*Settings.scale) * 2) {
                    startHop = true;
                }

                if(!m.flipHorizontal && x > m.drawX) {
                    m.flipHorizontal = true;
                }
            }
        }

        if(startHop) {
            updateHopAnimation();
        }
    }

    protected void updateHopAnimation() {
        this.vY -= 17.0F * Settings.scale;
        y += fpsHack(vY);
        if(y < baseY) {
            y = baseY;
        }
    }
}
