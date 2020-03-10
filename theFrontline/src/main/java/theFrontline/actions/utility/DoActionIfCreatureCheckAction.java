package theFrontline.actions.utility;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import theFrontline.util.UC;

import java.util.function.Predicate;

public class DoActionIfCreatureCheckAction extends AbstractGameAction {
    AbstractGameAction action;
    Predicate<AbstractCreature> check;
    Runnable runSucc;
    Runnable runFail;

    public DoActionIfCreatureCheckAction(AbstractCreature m, Predicate<AbstractCreature> check, AbstractGameAction action) {
        target = m;
        this.check = check;
        this.action = action;
    }

    public DoActionIfCreatureCheckAction(AbstractCreature m, Predicate<AbstractCreature> check, Runnable succ, Runnable fail) {
        target = m;
        this.check = check;
        this.action = null;
        runSucc = succ;
        runFail = fail;
    }

    @Override
    public void update() {
        if(action != null) {
            if (check.test(target)) {
                UC.att(action);
            }
        } else {
            if (check.test(target)) {
                runSucc.run();
            } else {
                runFail.run();
            }
        }

        isDone = true;
    }
}