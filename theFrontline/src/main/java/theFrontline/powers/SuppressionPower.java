package theFrontline.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theFrontline.TheFrontline;
import theFrontline.powers.abstracts.AbstractFrontlinePower;
import theFrontline.util.UC;

public class SuppressionPower extends AbstractFrontlinePower implements CloneablePowerInterface {
    public static final String POWER_ID = TheFrontline.makeID("Suppression");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public SuppressionPower(AbstractCreature owner, int dmg) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = dmg;
        type = PowerType.DEBUFF;
        updateDescription();
        loadRegion("lockon");
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        UC.doDmg(owner, amount, DamageInfo.DamageType.THORNS ,AbstractGameAction.AttackEffect.BLUNT_LIGHT, false, true);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new SuppressionPower(owner, amount);
    }
}