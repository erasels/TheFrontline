package theFrontline.vfx.general;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import theFrontline.util.UC;

public class MakeImageRunEffect extends AbstractGameEffect {
    protected float x;
    protected float y;
    protected float speed;
    protected Texture img;

    public MakeImageRunEffect(float x, float y, Texture img, float speed) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.speed = ((img.getWidth()* Settings.scale)/2f) * speed;
    }

    public void update() {
        x += fpsHack(speed);
        if(x > Settings.WIDTH + img.getWidth()) {
            isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        float tmpImgW = img.getWidth() * Settings.scale;
        float tmpImgH = img.getHeight() * Settings.scale;
        sb.draw(img, x, y, tmpImgW, tmpImgH, 0, 0, img.getWidth(), img.getHeight(), false, false);
    }

    public void dispose() {}

    protected float fpsHack(float f) {
        return f * 60 * UC.gt();
    }
}
