package theFrontline.actions.utility;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import theFrontline.util.UC;

public class DoActionIfMonsterDeadAction extends AbstractGameAction {
    AbstractGameAction action;
    private boolean skipMinions;

    public DoActionIfMonsterDeadAction(AbstractMonster m, AbstractGameAction action) {
        target = m;
        this.action = action;
        skipMinions = true;
    }

    @Override
    public void update() {
        if(target != null && target.isDeadOrEscaped() && (!skipMinions || !target.hasPower(MinionPower.POWER_ID))) {
            UC.att(action);
        }

        isDone = true;
    }
}
