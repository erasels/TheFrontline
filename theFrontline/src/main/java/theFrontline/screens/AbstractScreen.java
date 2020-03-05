package theFrontline.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public abstract class AbstractScreen {
    public static final float BLACKSCREEN_INTENSITY = 0.85f;
    protected Color blackScreenColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
    protected float blackScreenTarget;
    protected float startingDuration, duration;

    public boolean paused;
    public boolean isDone;

    public AbstractScreen(float duration, float blackscreenIntensity) {
        this.startingDuration = this.duration = duration;
        blackScreenTarget = blackscreenIntensity;
        isDone = false;
        hideElements();
    }

    public AbstractScreen(float duration) {
        this(duration, BLACKSCREEN_INTENSITY);
    }

    public void renderController(SpriteBatch sb) {
            renderBlackscreen(sb);
            renderEffects(sb);
            render(sb);
    }

    protected void renderBlackscreen(SpriteBatch sb) {
        if (!paused && blackScreenColor.a != 0.0F) {
            sb.setColor(this.blackScreenColor);
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, Settings.WIDTH, Settings.HEIGHT);
        }
    }

    protected void renderEffects(SpriteBatch sb) { }

    public void render(SpriteBatch sb) {}

    public void update() {
        //duration -= gt();
        if(duration < 0) {
            close();
            if(blackScreenColor.a == 0.0f) {
                isDone = true;
            }
        } else {
            generalEffect();
        }
        updateBlackScreen();
    }

    protected void generalEffect() {}
    public void triggerEffect() {}

    protected void updateBlackScreen() {
        if (this.blackScreenColor.a != this.blackScreenTarget)
            if (this.blackScreenTarget > this.blackScreenColor.a) {
                this.blackScreenColor.a += Gdx.graphics.getRawDeltaTime() * 2.0F;
                if (this.blackScreenColor.a > this.blackScreenTarget)
                    this.blackScreenColor.a = this.blackScreenTarget;
            } else {
                this.blackScreenColor.a -= Gdx.graphics.getRawDeltaTime() * 2.0F;
                if (this.blackScreenColor.a < this.blackScreenTarget)
                    this.blackScreenColor.a = this.blackScreenTarget;
            }
    }

    public void close() {
        blackScreenTarget = 0.0f;
    }

    public void closeInstantly() {
        blackScreenColor.a = 0.0f;
        isDone = true;
    }


    public void unpause() { paused = false; }
    public void pause() { paused = true; }

    public void hideElements() {

    }
}
