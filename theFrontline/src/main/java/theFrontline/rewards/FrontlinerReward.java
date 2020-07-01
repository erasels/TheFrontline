package theFrontline.rewards;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.RarePotionParticleEffect;
import com.megacrit.cardcrawl.vfx.UncommonPotionParticleEffect;
import org.apache.commons.lang3.math.NumberUtils;
import theFrontline.TheFrontline;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.patches.general.FontCreationPatches;
import theFrontline.patches.general.RewardItemTypeEnumPatch;
import theFrontline.screens.CharacterAddScreen;
import theFrontline.util.CharacterHelper;
import theFrontline.util.ScrapHelper;
import theFrontline.util.UC;

public class FrontlinerReward extends CustomReward {
    public static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(TheFrontline.makeID("FrontlinerReward"));

    public AbstractCharacterInfo ci;
    private boolean rightClicked = false;
    private float sparkleTimer;

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
        if(!rightClicked) {
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
        } else {
            ScrapHelper.addScrap(ci.getScrapValue());
        }

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

        FontHelper.renderSmartText(sb, FontHelper.cardDescFont_N, this.text, 833.0f * Settings.scale, this.y + 5.0f * Settings.scale, 1000.0f * Settings.scale, 0.0f, Color.WHITE);
        FontHelper.renderSmartText(sb, FontCreationPatches.tipFont, uiStrings.TEXT[1] + ci.getScrapValue() + uiStrings.TEXT[2], 833.0f * Settings.scale, this.y - FontHelper.getHeight(FontHelper.cardDescFont_N, text, Settings.scale) - 6f * Settings.scale, 1000.0f * Settings.scale, 0.0f, Color.WHITE);


        if (!Settings.DISABLE_EFFECTS) {
            sparkleTimer -= UC.gt();
            if(sparkleTimer < 0.0F) {
               switch(ci.getRarity()){
                    case RARE:
                        effects.add(new RarePotionParticleEffect(MathUtils.random(hb.x, hb.x + hb.width), MathUtils.random(hb.y, hb.y + hb.height)));
                        sparkleTimer = MathUtils.random(0.2F, 0.35F);
                        break;
                   case UNCOMMON:
                       effects.add(new UncommonPotionParticleEffect(MathUtils.random(hb.x, hb.x + hb.width), MathUtils.random(hb.y, hb.y + hb.height)));
                       sparkleTimer = MathUtils.random(0.75F, 1F);
                }
            }
        }

        if (!this.hb.hovered) {
            for (AbstractGameEffect e : this.effects) {
                e.render(sb);
            }
        }

        this.hb.render(sb);
    }

    @Override
    public void update() {
        super.update();

        if (this.hb.hovered && InputHelper.justClickedRight && !this.isDone) {
            CardCrawlGame.sound.playA("UI_CLICK_1", 0.25f);
            this.hb.clickStarted = true;
            rightClicked = true;
        }
    }
}