package theFrontline.actions.unique;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.cardModifiers.DauerfeuerCMod;

public class DauerfeuerAction extends AbstractGameAction {

    public DauerfeuerAction(AbstractMonster defaultTarget) {
        startDuration = Settings.ACTION_DUR_FAST;
        duration = startDuration;
        target = defaultTarget;
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            if(target == null)
                target = AbstractDungeon.getRandomMonster();
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if(c.type != AbstractCard.CardType.ATTACK) continue;

                boolean skip = false;
                // Card is already queued, ignore
                for (CardQueueItem q : AbstractDungeon.actionManager.cardQueue) {
                    if (q.card == c) {
                        skip = true;
                        break;
                    }
                }
                if(skip) continue;

                if(target.isDeadOrEscaped()) {
                    target = AbstractDungeon.getRandomMonster();
                }

                CardModifierManager.addModifier(c, new DauerfeuerCMod());
                switch (c.target) {
                    case SELF_AND_ENEMY:
                    case ENEMY:
                        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(c, (AbstractMonster) target));
                        break;
                    default:
                        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(c, null));
                }
            }
        }

        tickDuration();
    }
}
