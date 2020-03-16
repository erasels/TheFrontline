package theFrontline.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theFrontline.TheFrontline;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.powers.abstracts.AbstractFrontlinePower;
import theFrontline.util.UC;

import static theFrontline.util.UC.p;

public class GracePower extends AbstractFrontlinePower implements CloneablePowerInterface {
    public static final String POWER_ID = TheFrontline.makeID("Grace");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public GracePower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = AbstractPower.PowerType.BUFF;
        updateDescription();
        loadRegion("pressure_points");
    }

    public GracePower(int amount) {
        this(p(), amount);
    }

    @Override
    public float modifyBlock(float blockAmount) {
        return blockAmount + amount;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        //Will probably not be compatible with certain cards, can be fixed via dynamic patching, maybe later.
        if(card.baseBlock > -1 || (card instanceof FrontlineCard && ((FrontlineCard) card).useGrace)) {
            flash();
            UC.att(new RemoveSpecificPowerAction(UC.p(), UC.p(), this));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new GracePower(owner, amount);
    }
}