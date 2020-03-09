package theFrontline.actions.utility;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import theFrontline.util.UC;

public class ForcedWaitAction extends AbstractGameAction {
    public ForcedWaitAction(float setDur) {
        setValues(null, null, 0);
        this.duration = setDur;
        this.actionType = AbstractGameAction.ActionType.WAIT;
    }

    public void update() {
        this.duration -= UC.gt();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }
}
