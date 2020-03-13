package theFrontline.actions.common;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import theFrontline.patches.cards.DiscardCardHook;
import theFrontline.util.UC;

import java.util.function.Consumer;

public class CallbackDiscardAction extends DiscardAction {
    private Consumer<AbstractCard> callback;

    public CallbackDiscardAction(int numCards, boolean isRandom, Consumer<AbstractCard> callback) {
        super(UC.p(), UC.p(), numCards, isRandom);
        this.callback = callback;
    }

    @Override
    public void update() {
        DiscardCardHook.callback = callback;
        super.update();
        DiscardCardHook.callback = null;
    }
}