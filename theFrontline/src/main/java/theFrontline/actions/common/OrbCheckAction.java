package theFrontline.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import theFrontline.util.UC;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class OrbCheckAction extends AbstractGameAction {
    private Predicate<AbstractOrb> filter;
    private Consumer<AbstractOrb> callback;

    public OrbCheckAction(Predicate<AbstractOrb> filter, Consumer<AbstractOrb> callback) {
        this.target = UC.p();
        this.filter = filter;
        this.callback = callback;
    }

    public void update() {
        for(AbstractOrb o : ((AbstractPlayer)target).orbs) {
            if(filter.test(o)) {
                callback.accept(o);
            }
        }
        isDone = true;
    }
}
