package theFrontline.actions.utility;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import theFrontline.characters.FrontlineCharacter;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.relics.abstracts.FrontlineRelic;
import theFrontline.util.UC;

public class SwitchCharacterAction extends AbstractGameAction {
    protected AbstractCharacterInfo ci;

    public SwitchCharacterAction(AbstractCharacterInfo ci) {
        if(ci == null) {
            isDone = true;
        }
        this.ci = ci;
        startDuration = duration = Settings.ACTION_DUR_LONG;
    }

    public SwitchCharacterAction(int amt) {
        this(UC.pc().characters.get(amt));
    }

    @Override
    public void update() {
        if(startDuration == duration) {
            FrontlineCharacter p = UC.pc();
            if(p != null) {
                p.switchCharacter(ci);
                p.healthBarUpdatedEvent();
                for(AbstractRelic r : p.relics) {
                    if(r instanceof FrontlineRelic) {
                        ((FrontlineRelic) r).onCharacterSwitch(false);
                    }
                }
            }
        }
        tickDuration();
    }
}
