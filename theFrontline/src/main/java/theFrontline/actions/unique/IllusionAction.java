package theFrontline.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.util.UC;

public class IllusionAction extends AbstractGameAction {
    private FrontlineCard c;

    public IllusionAction(FrontlineCard c, AbstractMonster target) {
        this.c = c;
        this.target = target;
    }

    @Override
    public void update() {
        for (int i = 0; i < c.baseMagicNumber2; i++) {
            UC.doDmg(target, c.magicNumber, DamageInfo.DamageType.THORNS, AttackEffect.BLUNT_LIGHT, true, true);
        }
        isDone = true;
    }
}
