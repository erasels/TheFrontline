package theFrontline.orbs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import theFrontline.TheFrontline;
import theFrontline.characters.FrontlineCharacter;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.util.CharacterHelper;
import theFrontline.util.TextureLoader;
import theFrontline.util.UC;

import java.util.ArrayList;

public class CombatCharacterSelection {
    public static Texture borderImg = TextureLoader.getTexture(TheFrontline.makeUIPath("charStatusBorder.png"));
    public static Color bgCol = Color.BLACK.cpy();
    public ArrayList<CharacterOrb> chars = new ArrayList<>();
    protected final float MULT = Settings.scale * 0.5f;
    protected Hitbox curCharStatusHB = new Hitbox(borderImg.getWidth() * MULT, borderImg.getHeight() * MULT);

    public CombatCharacterSelection() {
        bgCol.a = 0.5f;
        curCharStatusHB.move(((borderImg.getWidth() * MULT) * 0.5f) + 12 * Settings.scale, ((borderImg.getHeight() * MULT) * 0.5f) + Settings.HEIGHT/2f);
    }

    public void update() {
        curCharStatusHB.update();
        chars.forEach(CharacterOrb::update);

        if(curCharStatusHB.hovered) {
            AbstractCharacterInfo ci = UC.pc().getCurrChar();
            TipHelper.renderGenericTip(curCharStatusHB.cX + ((borderImg.getWidth()* MULT)*0.5f), InputHelper.mY, ci.name, ci.getDescription());
        }
    }

    public void render(SpriteBatch sb) {
        AbstractPlayer p = AbstractDungeon.player;
        if (p != null && (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !p.isDead)) {
            if (!this.chars.isEmpty()) {
                for (CharacterOrb co : chars) {
                    co.render(sb);
                }
            }
            AbstractCharacterInfo ci = UC.pc().getCurrChar();
            AtlasRegion tmp = ci.getStatusImage((ci.currentHP > ci.maxHP/2));
            if(tmp != null) {
                sb.draw(tmp, 12 * Settings.scale, Settings.HEIGHT/2f + (10 * MULT), tmp.getRegionWidth() * MULT, tmp.getRegionHeight() * MULT);

                Texture tIcon = ci.getTypeIcon();
                sb.setColor(bgCol);
                sb.draw(ImageMaster.WHITE_SQUARE_IMG, 16 * Settings.scale, Settings.HEIGHT/2f + (10 * MULT), tIcon.getWidth() * MULT, tIcon.getHeight() * MULT);
                sb.setColor(CharacterHelper.getRarityColor(ci));
                sb.draw(tIcon, 16 * Settings.scale, Settings.HEIGHT/2f + (10 * MULT), tIcon.getWidth() * MULT, tIcon.getHeight() * MULT);

                sb.setColor(Color.WHITE);
                sb.draw(borderImg, 10 * Settings.scale, Settings.HEIGHT/2f, borderImg.getWidth() * MULT, borderImg.getHeight() * MULT);

                if(Settings.isDebug) {
                    curCharStatusHB.render(sb);
                }
            }
        }
    }

    public void preBattlePrep() {
        fixSelection();
    }

    public void fixSelection() {
        chars.clear();
        FrontlineCharacter p = UC.pc();
        if (p != null) {
            for (AbstractCharacterInfo ci : p.getCharacters()) {
                if (!p.currentCharacter.equals(ci.id)) {
                    addChar(new CharacterOrb(ci));
                }
            }
        }
    }

    public void addChar(CharacterOrb co) {
        chars.add(co);

        float dist = 420.0F * Settings.scale;
        float angle = (size() - 1) * 25.0F;
        float offsetAngle = angle / 2.0F;
        if (size() != 1) {
            angle *= (float) chars.indexOf(co) / ((float) size() - 1.0F);
        } else {
            angle = 0.0F;
        }
        angle += 90.0F - offsetAngle;
        co.tX = dist * MathUtils.cosDeg(angle) + AbstractDungeon.player.drawX;
        float yOffset = -180.0F * Settings.scale;
        if (AbstractDungeon.player.maxOrbs == 0) {
            co.tY = dist * MathUtils.sinDeg(angle) + AbstractDungeon.player.drawY + AbstractDungeon.player.hb_h / 2.0F + yOffset;
        } else {
            yOffset += ((float) AbstractDungeon.player.maxOrbs * 10.0F + 20.0F) * Settings.scale;
            co.tY = dist * MathUtils.sinDeg(angle) + AbstractDungeon.player.drawY + AbstractDungeon.player.hb_h / 2.0F + yOffset;
        }
        co.hb.move(co.tX, co.tY);
    }

    public int size() {
        return this.chars.size();
    }

    public int getSlot(AbstractCharacterInfo ci) {
        for (int i = 0; i < chars.size()-1; i++) {
            if(chars.get(i).character.id.equals(ci.id)) {
                return i;
            }
        }
        return -1;
    }
}
