package theFrontline.ui.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.ui.buttons.Button;
import com.megacrit.cardcrawl.vfx.RarePotionParticleEffect;
import com.megacrit.cardcrawl.vfx.UncommonPotionParticleEffect;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.characters.characterInfo.frontline.FrontlineInfo;
import theFrontline.util.CharacterHelper;

public class CharacterImageButton extends Button {
    protected String header, msg;
    protected Runnable exec;
    protected AbstractCharacterInfo ci;
    protected boolean rarityEffect;
    protected float vfxTimer = 1.0f;
    protected float vfxIntervalMin = 0.075f;
    protected float vfxIntervalMax = 0.3f;


    public CharacterImageButton(float x, float y, AbstractCharacterInfo ci, String header, String msg, boolean rarityEffect, Runnable exec) {
        super(x, y, ci.img);
        this.header = header;
        this.msg = msg;
        this.exec = exec;
        this.ci = ci;
        this.hb = new Hitbox(x, y, (float)ci.img.getWidth() * Settings.scale, (float)ci.img.getHeight() * Settings.scale);
        this.rarityEffect = rarityEffect;
        //inactiveColor = activeColor;
    }

    public CharacterImageButton(float x, float y, AbstractCharacterInfo ci, String header, String msg, boolean rarityEffect) {
        this(x, y, ci, header, msg, rarityEffect,null);
    }

    public CharacterImageButton(float x, float y, AbstractCharacterInfo ci, boolean rarityEffect) {
        this(x, y, ci, "", "", rarityEffect,null);
        String tmp = "";
        if(ci.isGFL()) {
            tmp = ((FrontlineInfo)ci).type.name() +": ";
        }
        tmp += ci.name;
        header = tmp;
        msg = CharacterHelper.getEffectString(ci);
    }

    @Override
    public void update() {
        super.update();
        updateAnimation();
        if(pressed) {
            pressed = false;
            onClick();
        }
        if(hb.hovered) {
            if(InputHelper.justClickedRight) {
                onRightClick();
            }
            TipHelper.renderGenericTip(x + ((ci.img.getWidth()* Settings.scale)*0.75f), InputHelper.mY, header, msg);
        }
    }

    public void updateAnimation() {
        if(!rarityEffect) return;

        vfxTimer -= Gdx.graphics.getDeltaTime();
        if (vfxTimer < 0.0f) {
            switch (ci.getRarity()) {
                case RARE:
                    AbstractDungeon.topLevelEffectsQueue.add(new RarePotionParticleEffect(hb.cX + (MathUtils.random(hb.width/2) * (MathUtils.randomBoolean()?-1:1)), hb.cY+ (MathUtils.random(hb.height/2) * (MathUtils.randomBoolean()?-1:1))));
                    break;
                case UNCOMMON:
                    AbstractDungeon.topLevelEffectsQueue.add(new UncommonPotionParticleEffect(hb.cX + (MathUtils.random(hb.width/2) * (MathUtils.randomBoolean()?-1:1)), hb.cY+ (MathUtils.random(hb.height/2) * (MathUtils.randomBoolean()?-1:1))));
                    break;
            }
            vfxTimer = MathUtils.random(vfxIntervalMin, vfxIntervalMax);
        }
    }

    public void onClick() {
        if(exec != null) {
            exec.run();
        }
    }

    public void onRightClick() {
        if(exec != null) {
            exec.run();
        }
    }

    public void render(SpriteBatch sb) {
        /*if (hb.hovered) {
            sb.setColor(activeColor);
        } else {
            sb.setColor(inactiveColor);
        }*/
        //sb.draw(ci.img, x, y);
        sb.draw(ci.img, x, y, ci.img.getWidth() * Settings.scale, ci.img.getHeight() * Settings.scale, 0, 0, ci.img.getWidth(), ci.img.getHeight(), false, false);
        //sb.setColor(Color.WHITE);
        hb.render(sb);
    }
}
