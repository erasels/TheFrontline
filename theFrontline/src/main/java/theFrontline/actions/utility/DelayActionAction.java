package theFrontline.actions.utility;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import theFrontline.util.UC;

public class DelayActionAction extends AbstractGameAction {
    private AbstractGameAction action;

    public DelayActionAction(AbstractGameAction action) {
        this.action = action;
    }

    @Override
    public void update() {
        UC.atb(action);
        isDone = true;
    }
}
