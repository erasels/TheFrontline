package theFrontline.actions.utility;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import theFrontline.characters.FrontlineCharacter;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.relics.abstracts.FrontlineRelic;
import theFrontline.util.UC;
import theFrontline.vfx.combat.unique.CharacterRunEffect;

public class SwitchCharacterCombatAction extends SwitchCharacterAction{
    private int eCost;

    public SwitchCharacterCombatAction(AbstractCharacterInfo ci, int i) {
        super(ci);
        eCost = i;
    }

    @Override
    public void update() {
        if(startDuration == duration) {
            AbstractDungeon.effectsQueue.add(new SmokeBombEffect(UC.p().hb.cX, UC.p().hb.cY));
            AbstractDungeon.effectsQueue.add(new CharacterRunEffect(UC.p().drawX, UC.p().drawY, UC.p().img, 0.75f));

            FrontlineCharacter p = UC.pc();
            if(p != null) {
                p.energy.use(eCost);
                p.switchCharacter(ci);
                p.healthBarUpdatedEvent();
                p.switchOrbSystem.fixSelection();
                for(AbstractRelic r : p.relics) {
                    if(r instanceof FrontlineRelic) {
                        ((FrontlineRelic) r).onCharacterSwitch(true);
                    }
                }
            }
        }
        tickDuration();
    }
}
