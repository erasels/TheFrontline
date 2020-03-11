package theFrontline.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.powers.FlankingPower;
import theFrontline.util.UC;

public class FlankAction extends AbstractGameAction {
    public FlankAction() {
    }

    public FlankAction(AbstractMonster m) {
        target = m;
    }

    public void update() {
        if(target != null) {
            UC.doPow(target, new FlankingPower(target), true);
        } else {
            UC.getAliveMonsters().forEach(m -> UC.doPow(m, new FlankingPower(m), true));
        }
        isDone = true;
    }
}
