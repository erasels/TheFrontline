package theFrontline.ui;

import basemod.TopPanelItem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import theFrontline.TheFrontline;
import theFrontline.util.ScrapHelper;
import theFrontline.util.TextureLoader;

public class ScrapDisplay extends TopPanelItem {
    private static final float FLASH_ANIM_TIME = 2.0F;
    private static final float tipYpos = Settings.HEIGHT - (120.0f * Settings.scale);
    public float flashTimer;

    public static final String ID = TheFrontline.makeID("ScrapDisplay");

    private static final Texture ICON = TextureLoader.getTexture(TheFrontline.makeUIPath("scrapIcon.png"));
    public static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public ScrapDisplay() {
        super(ICON, ID);
    }

    @Override
    public boolean isClickable() {
        return true;
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        renderFlash(sb);
        FontHelper.cardTitleFont.getData().setScale(1.0f);
        FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, Integer.toString(ScrapHelper.getScrap()), this.x + (this.hb_w / 2), this.y + 16f * Settings.scale, Color.WHITE.cpy());

        if (this.getHitbox().hovered) {
            TipHelper.renderGenericTip(this.x, tipYpos, uiStrings.TEXT[0], uiStrings.TEXT[1]);
        }

    }

    @Override
    protected void onClick() {
        CardCrawlGame.sound.play("RELIC_DROP_CLINK");
        //Menu opening
    }

    public void flash() {
        this.flashTimer = FLASH_ANIM_TIME;
    }

    @Override
    public void update() {
        updateFlash();
        super.update();
    }

    private void updateFlash() {
        if (flashTimer != 0.0F) {
            flashTimer -= Gdx.graphics.getDeltaTime();
        }
    }

    public void renderFlash(SpriteBatch sb) {
        float tmp = Interpolation.exp10In.apply(0.0F, 4.0F, flashTimer / FLASH_ANIM_TIME);
        sb.setBlendFunction(770, 1);
        sb.setColor(new Color(1.0F, 1.0F, 1.0F, flashTimer * FLASH_ANIM_TIME));

        float halfWidth = (float) this.image.getWidth() / 2.0F;
        float halfHeight = (float) this.image.getHeight() / 2.0F;
        sb.draw(this.image, this.x - halfWidth + halfHeight * Settings.scale, this.y - halfHeight + halfHeight * Settings.scale, halfWidth, halfHeight, (float) this.image.getWidth(), (float) this.image.getHeight(), Settings.scale + tmp, Settings.scale + tmp, this.angle, 0, 0, this.image.getWidth(), this.image.getHeight(), false, false);
        sb.draw(this.image, this.x - halfWidth + halfHeight * Settings.scale, this.y - halfHeight + halfHeight * Settings.scale, halfWidth, halfHeight, (float) this.image.getWidth(), (float) this.image.getHeight(), Settings.scale + tmp * 0.66F, Settings.scale + tmp * 0.66F, this.angle, 0, 0, this.image.getWidth(), this.image.getHeight(), false, false);
        sb.draw(this.image, this.x - halfWidth + halfHeight * Settings.scale, this.y - halfHeight + halfHeight * Settings.scale, halfWidth, halfHeight, (float) this.image.getWidth(), (float) this.image.getHeight(), Settings.scale + tmp / 3.0F, Settings.scale + tmp / 3.0F, this.angle, 0, 0, this.image.getWidth(), this.image.getHeight(), false, false);

        sb.setBlendFunction(770, 771);
    }
}