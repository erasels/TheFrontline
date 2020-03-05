package theFrontline.actions.utility;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import theFrontline.TheFrontline;
import theFrontline.mechanics.speed.AbstractSpeedTime;

public class SetSpeedModeAction extends AbstractGameAction {
    AbstractSpeedTime instance;

    public SetSpeedModeAction(AbstractSpeedTime instance) {
        this.instance = instance;
    }

    public void update() {
        TheFrontline.speedScreen = instance;
        isDone = true;
    }
}
