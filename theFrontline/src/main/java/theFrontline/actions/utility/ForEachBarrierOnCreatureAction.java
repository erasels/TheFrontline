package theFrontline.actions.utility;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theFrontline.powers.abstracts.AbstractSacredPower;

import java.util.function.Consumer;

public class ForEachBarrierOnCreatureAction extends AbstractGameAction {
    private Consumer<AbstractSacredPower> callback;
    public ForEachBarrierOnCreatureAction(AbstractCreature target, Consumer<AbstractSacredPower> callback) {
        this.target = target;
        this.callback = callback;
    }

    @Override
    public void update() {
        for(AbstractPower p : target.powers) {
            if(p instanceof AbstractSacredPower && ((AbstractSacredPower) p).isBarrierPower) {
                callback.accept((AbstractSacredPower) p);
            }
        }
    }
}
