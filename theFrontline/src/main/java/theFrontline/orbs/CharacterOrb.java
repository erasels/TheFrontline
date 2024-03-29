package theFrontline.orbs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbPassiveEffect;
import theFrontline.TheFrontline;
import theFrontline.actions.utility.SwitchCharacterAction;
import theFrontline.actions.utility.SwitchCharacterCombatAction;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.characters.characterInfo.frontline.FrontlineInfo;
import theFrontline.powers.abstracts.AbstractFrontlinePower;
import theFrontline.relics.abstracts.FrontlineRelic;
import theFrontline.util.UC;

public class CharacterOrb extends AbstractOrb {
    // Standard ID/Description
    public static final String ORB_ID = TheFrontline.makeID("CharacterOrb");
    private static final OrbStrings orbString = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String[] DESC = orbString.DESCRIPTION;

    public AbstractCharacterInfo character;

    protected static final float NUM_X_OFFSET = 42.0F * Settings.scale;
    protected static final float NUM_Y_OFFSET = -30.0F * Settings.scale;
    protected static final float MAX_CLICK_CD = 1f; //1sec
    protected static Color normalCol = Color.WHITE.cpy();
    protected static Color energyTextCol = Color.SKY.cpy();
    protected static Color healthTextCol = new Color(1, 0.4f, 0.4f, 1);
    protected static float clickCD = 0;
    // Animation Rendering Numbers - You can leave these at default, or play around with them and see what they change.
    private float vfxTimer = 1.0f;
    private float vfxIntervalMin = 0.3f;
    private float vfxIntervalMax = 0.7f;
    private float alpha = 1f;
    private int eCost;

    public CharacterOrb(AbstractCharacterInfo c) {
        character = c;
        ID = ORB_ID;
        String tmp = "";
        if (c.isGFL()) {
            tmp = ((FrontlineInfo) c).type.name() + ": ";
        }
        name = tmp + c.name;

        updateDescription();

        //angle = MathUtils.random(360.0f); // More Animation-related Numbers
        angle = 0;

        this.cX = AbstractDungeon.player.drawX + AbstractDungeon.player.hb_x;
        this.cY = AbstractDungeon.player.drawY + AbstractDungeon.player.hb_y + AbstractDungeon.player.hb_h / 2.0F;
        channelAnimTimer = 0.5f;
    }

    @Override
    public void updateDescription() {
        description = character.getDescription();
    }

    @Override
    public void updateAnimation() {
        super.updateAnimation();
        //angle += Gdx.graphics.getDeltaTime() * 45.0f;
        vfxTimer -= Gdx.graphics.getDeltaTime();
        if (vfxTimer < 0.0f) {
            AbstractDungeon.effectList.add(new FrostOrbPassiveEffect(cX, cY));
            vfxTimer = MathUtils.random(vfxIntervalMin, vfxIntervalMax);
        }
    }

    @Override
    public void update() {
        super.update();
        clickCD -= UC.gt();
        eCost = 1;
        for(AbstractRelic r : UC.p().relics) {
            if(eCost < -100) {
                break;
            }
            if(r instanceof FrontlineRelic) {
                eCost = ((FrontlineRelic) r).characterSwitchCost(eCost, character);
            }
        }

        for(AbstractPower p : UC.p().powers) {
            if(eCost < -100) {
                break;
            }
            if(p instanceof AbstractFrontlinePower) {
                eCost = ((AbstractFrontlinePower) p).characterSwitchCost(eCost, character);
            }
        }

        if(eCost < 0) {
            eCost = 0;
        }

        if (hb.hovered) {
            if (eCost <= EnergyPanel.totalCount &&
                    clickCD <= 0 &&
                    AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT &&
                    AbstractDungeon.actionManager.phase == GameActionManager.Phase.WAITING_ON_USER) {
                alpha = 1.0f;
                if (InputHelper.justReleasedClickLeft && AbstractDungeon.actionManager.actions.stream().noneMatch(a -> a instanceof SwitchCharacterAction)) {
                    clickCD = MAX_CLICK_CD;
                    UC.atb(new SwitchCharacterCombatAction(character, eCost));
                }
            }
        } else {
            alpha = 0.3f;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        normalCol.a = alpha;
        sb.setColor(normalCol);
        sb.draw(character.img,
                cX - ((character.img.getWidth()*Settings.scale)/4f),
                cY - (48.0f*Settings.scale) + bobEffect.y,
                0,
                0,
                character.img.getWidth() * Settings.scale,
                character.img.getHeight() * Settings.scale,
                scale * 0.5f,
                scale * 0.5f,
                angle,
                0,
                0,
                character.img.getWidth(),
                character.img.getHeight(),
                false,
                false);
        renderText(sb);
        hb.render(sb);
        sb.setColor(Color.WHITE);
    }

    @Override
    protected void renderText(SpriteBatch sb) {
        healthTextCol.a = alpha;
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(character.currentHP), this.cX - NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F - NUM_Y_OFFSET, healthTextCol, this.fontScale);
        energyTextCol.a = alpha;
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(eCost), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET, energyTextCol, this.fontScale);
    }

    public void onEvoke() {
    }

    public void triggerEvokeAnimation() {
    }

    public void playChannelSFX() {
    }

    @Override
    public AbstractOrb makeCopy() {
        return new CharacterOrb(character);
    }
}
