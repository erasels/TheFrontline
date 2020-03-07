package theFrontline.actions.utility;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import theFrontline.characters.FrontlineCharacter;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.util.UC;
import theFrontline.vfx.combat.unique.CharacterRunEffect;

public class SwitchCharacterCombatAction extends SwitchCharacterAction{
    private static final int ENERGY_COST = 1;

    public SwitchCharacterCombatAction(AbstractCharacterInfo ci) {
        super(ci);
    }

    @Override
    public void update() {
        if(startDuration == duration) {
            AbstractDungeon.effectsQueue.add(new SmokeBombEffect(UC.p().hb.cX, UC.p().hb.cY));
            AbstractDungeon.effectsQueue.add(new CharacterRunEffect(UC.p().drawX, UC.p().drawY, UC.p().img, 0.75f));

            FrontlineCharacter p = UC.pc();
            if(p != null) {
                p.energy.use(ENERGY_COST);
                p.switchCharacter(ci);
                p.healthBarUpdatedEvent();
                p.switchOrbSystem.fixSelection();
            }
        }
        tickDuration();
    }
}
