package theFrontline.actions.utility;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import theFrontline.actions.common.DumbApplyPowerAction;
import theFrontline.characters.FrontlineCharacter;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.powers.FlankingPower;
import theFrontline.relics.abstracts.FrontlineRelic;
import theFrontline.util.UC;
import theFrontline.vfx.combat.unique.CharacterRunEffect;

public class SwitchCharacterCombatAction extends SwitchCharacterAction{
    private int eCost;
    private boolean death;

    public SwitchCharacterCombatAction(AbstractCharacterInfo ci, int i) {
        this(ci, i, false);
    }

    public SwitchCharacterCombatAction(AbstractCharacterInfo ci, int i, boolean death) {
        super(ci);
        eCost = i;
        this.death = death;
    }

    @Override
    public void update() {
        if(startDuration == duration) {
            AbstractDungeon.effectsQueue.add(new SmokeBombEffect(UC.p().hb.cX, UC.p().hb.cY));
            if(!death) {
                AbstractDungeon.effectsQueue.add(new CharacterRunEffect(UC.p().drawX, UC.p().drawY, UC.p().img, 0.75f));
                AbstractDungeon.getMonsters().monsters.forEach(m -> UC.att(new DumbApplyPowerAction(m, UC.p(), new FlankingPower(m))));
            }

            FrontlineCharacter p = UC.pc();
            if(p != null) {
                if(!death) {
                    p.energy.use(eCost);
                }
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
