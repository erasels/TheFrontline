package theFrontline.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ChokePower;
import theFrontline.TheFrontline;
import theFrontline.powers.abstracts.AbstractFrontlinePower;
import theFrontline.util.UC;

import static theFrontline.util.UC.p;

public class SpicyPower extends AbstractFrontlinePower implements CloneablePowerInterface {
    public static final String POWER_ID = TheFrontline.makeID("Spicy");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ChokePower.POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public SpicyPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.DEBUFF;
        updateDescription();
        loadRegion("choke");
    }

    public SpicyPower(int amount) {
        this(p(), amount);
    }

    public void atStartOfTurn() {
        UC.removePower(this);
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        flash();
        addToBot(new LoseHPAction(this.owner, null, this.amount, AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new SpicyPower(owner, amount);
    }
}