package theFrontline.rewards;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import org.apache.commons.lang3.math.NumberUtils;
import theFrontline.TheFrontline;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.patches.general.RewardItemTypeEnumPatch;
import theFrontline.screens.CharacterAddScreen;
import theFrontline.util.CharacterHelper;

public class FrontlinerReward extends CustomReward {
    public static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(TheFrontline.makeID("FrontlinerReward"));

    public AbstractCharacterInfo ci;

    public FrontlinerReward() {
        this(CharacterHelper.getRandomCharacter());
    }

    public FrontlinerReward(AbstractCharacterInfo ci) {
        super(null, uiStrings.TEXT[0] + ci.name, RewardItemTypeEnumPatch.FRONTLINER);
        this.ci = ci;
        this.icon = ci.img;
    }

    @Override
    public boolean claimReward() {
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        CharacterAddScreen.roomPhase = AbstractDungeon.getCurrRoom().phase;
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;

        AbstractDungeon.closeCurrentScreen();

        TheFrontline.screen = new CharacterAddScreen();
        ((CharacterAddScreen) TheFrontline.screen).open(ci);

        return true;
    }

    @Override
    public void render(SpriteBatch sb) {
        if (this.hb.hovered) {
            sb.setColor(new Color(0.4f, 0.6f, 0.6f, 1.0f));
        } else {
            sb.setColor(new Color(0.5f, 0.6f, 0.6f, 0.8f));
        }

        if (this.hb.clickStarted) {
            sb.draw(ImageMaster.REWARD_SCREEN_ITEM, Settings.WIDTH / 2.0f - 232.0f, this.y - 49.0f, 232.0f, 49.0f, 464.0f, 98.0f, Settings.scale * 0.98f, Settings.scale * 0.98f, 0.0f, 0, 0, 464, 98, false, false);
        } else {
            sb.draw(ImageMaster.REWARD_SCREEN_ITEM, Settings.WIDTH / 2.0f - 232.0f, this.y - 49.0f, 232.0f, 49.0f, 464.0f, 98.0f, Settings.scale, Settings.scale, 0.0f, 0, 0, 464, 98, false, false);
        }

        if (this.flashTimer != 0.0f) {
            sb.setColor(0.6f, 1.0f, 1.0f, this.flashTimer * 1.5f);
            sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
            sb.draw(ImageMaster.REWARD_SCREEN_ITEM, Settings.WIDTH / 2.0f - 232.0f, this.y - 49.0f, 232.0f, 49.0f, 464.0f, 98.0f, Settings.scale * 1.03f, Settings.scale * 1.15f, 0.0f, 0, 0, 464, 98, false, false);
            sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }

        sb.setColor(Color.WHITE.cpy());

        float ratio = (float)NumberUtils.min(icon.getWidth(), icon.getHeight()) / NumberUtils.max(icon.getWidth(), icon.getHeight());

        sb.draw(this.icon,
                RewardItem.REWARD_ITEM_X - 32.0f,
                this.y - 32.0f - 2.0f * Settings.scale,
                32.0f,
                32.0f,
                64.0f * ratio,
                64.0f,
                Settings.scale,
                Settings.scale,
                0.0f,
                0,
                0,
                icon.getWidth(),
                icon.getHeight(),
                false,
                false);

        Color c = Settings.CREAM_COLOR.cpy();
        if (this.hb.hovered) {
            c = Settings.GOLD_COLOR.cpy();
        }

        FontHelper.renderSmartText(sb, FontHelper.cardDescFont_N, this.text, 833.0f * Settings.scale, this.y + 5.0f * Settings.scale, 1000.0f * Settings.scale, 0.0f, c);

        if (!this.hb.hovered) {
            for (AbstractGameEffect e : this.effects) {
                e.render(sb);
            }
        }

        this.hb.render(sb);
    }
}