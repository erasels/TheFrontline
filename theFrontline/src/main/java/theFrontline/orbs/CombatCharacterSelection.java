package theFrontline.orbs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import theFrontline.characters.FrontlineCharacter;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.util.UC;

import java.util.ArrayList;

public class CombatCharacterSelection {
    public ArrayList<CharacterOrb> chars = new ArrayList<>();

    public CombatCharacterSelection() {
    }

    public void render(SpriteBatch sb) {
        AbstractPlayer p = AbstractDungeon.player;
        if (p != null && (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !p.isDead)) {
            if (!this.chars.isEmpty()) {
                for (CharacterOrb co : chars) {
                    co.render(sb);
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
