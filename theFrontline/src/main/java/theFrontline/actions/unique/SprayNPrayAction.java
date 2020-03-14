package theFrontline.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import theFrontline.util.UC;

public class SprayNPrayAction extends AbstractGameAction {
    private AbstractCard card;
    private AttackEffect effect;

    public SprayNPrayAction(AbstractCard card, AttackEffect effect, int repeats) {
        // We need the card as a param since Wrist Blade exists.
        this.card = card;
        this.effect = effect;
        this.amount = repeats;
    }

    public void update() {
        --amount;
        if(amount > 0) {
            addToTop(new SprayNPrayAction(card, effect, amount));
        }
        this.target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        if (this.target != null) {
            this.card.calculateCardDamage((AbstractMonster) this.target);
            addToTop(new DamageAction(this.target, new DamageInfo(AbstractDungeon.player, this.card.damage, this.card.damageTypeForTurn), this.effect));
            UC.doPow(target, new WeakPower(target, 1, false), true);
        }
        this.isDone = true;
    }
}