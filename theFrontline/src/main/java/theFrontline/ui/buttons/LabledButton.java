package theFrontline.ui.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

public class LabledButton {
    private static final int W = 512;
    private static final int H = 256;
    public static final float TAKE_Y = Settings.HEIGHT / 2.0F - 340.0F * Settings.scale;
    private static final float SHOW_X = Settings.WIDTH / 2.0F;
    private static final float HIDE_X = Settings.WIDTH / 2.0F;
    private float current_x;
    private float target_x;
    private float x, y;
    private boolean isHidden = true;
    private String msg;
    private boolean toCancel;
    private Runnable exec;
    private Color textColor = Color.WHITE.cpy();
    private Color btnColor;
    public boolean screenDisabled = false;
    private static final float HITBOX_W = 260.0F * Settings.scale;
    private static final float HITBOX_H = 80.0F * Settings.scale;
    public Hitbox hb = new Hitbox(0.0F, 0.0F, HITBOX_W, HITBOX_H);

    //x, y, text, cancel, exec
    public LabledButton(float x, float y, String msg, boolean toCancel, Runnable exec, Color col) {
        this.msg = msg;
        this.toCancel = toCancel;
        this.exec = exec;
        btnColor = col.cpy();
        this.hb.move(x, y);
        this.y = y;
        this.x = x;
        current_x = 0;
        target_x = x;
    }

    public void update() {
        if (this.isHidden)
            return;
        this.hb.update();
        if (this.hb.justHovered)
            CardCrawlGame.sound.play("UI_HOVER");
        if (this.hb.hovered && InputHelper.justClickedLeft) {
            this.hb.clickStarted = true;
            CardCrawlGame.sound.play("UI_CLICK_1");
        }
        if ((this.hb.clicked || ((InputActionSet.cancel.isJustPressed() || CInputActionSet.cancel.isJustPressed()) && toCancel)) && !this.screenDisabled) {
            this.hb.clicked = false;
            exec.run();
        }
        this.screenDisabled = false;
        if (this.current_x != this.target_x) {
            this.current_x = MathUtils.lerp(this.current_x, this.target_x, Gdx.graphics.getDeltaTime() * 9.0F);
            if (Math.abs(this.current_x - this.target_x) < Settings.UI_SNAP_THRESHOLD) {
                this.current_x = this.target_x;
                this.hb.move(this.current_x, y);
            }
        }
        this.textColor.a = MathHelper.fadeLerpSnap(this.textColor.a, 1.0F);
        this.btnColor.a = this.textColor.a;
    }

    public void hideInstantly() {
        this.current_x = HIDE_X;
        this.target_x = HIDE_X;
        this.isHidden = true;
        this.textColor.a = 0.0F;
        this.btnColor.a = 0.0F;
    }

    public void hide() {
        this.isHidden = true;
    }

    public void show() {
        this.isHidden = false;
        this.textColor.a = 0.0F;
        this.btnColor.a = 0.0F;
        this.current_x = HIDE_X;
        this.target_x = x;
        this.hb.move(target_x, y);
    }

    public void render(SpriteBatch sb) {
        if (this.isHidden)
            return;
        renderButton(sb);
        if (FontHelper.getSmartWidth(FontHelper.buttonLabelFont, msg, 9999.0F, 0.0F) > 200.0F * Settings.scale) {
            FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, msg, this.current_x, y, this.textColor, 0.8F);
        } else {
            FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, msg, this.current_x, y, this.textColor);
        }
    }

    private void renderButton(SpriteBatch sb) {
        sb.setColor(this.btnColor);
        sb.draw(ImageMaster.REWARD_SCREEN_TAKE_BUTTON, this.current_x - H, y - 128.0F, H, 128.0F, W, H, Settings.scale, Settings.scale, 0.0F, 0, 0, W, H, false, false);
        if (this.hb.hovered && !this.hb.clickStarted) {
            //sb.setBlendFunction(Gdx.gl20.GL_SRC_ALPHA, Gdx.gl20.GL_DST_COLOR);
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.3F));
            sb.draw(ImageMaster.REWARD_SCREEN_TAKE_BUTTON, this.current_x - H, y - 128.0F, H, 128.0F, W, H, Settings.scale, Settings.scale, 0.0F, 0, 0, W, H, false, false);
            //sb.setBlendFunction(Gdx.gl20.GL_SRC_ALPHA, Gdx.gl20.GL_ONE_MINUS_SRC_ALPHA);
        }
        this.hb.render(sb);
    }
}

