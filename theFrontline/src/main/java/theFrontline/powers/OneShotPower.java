package theFrontline.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theFrontline.TheFrontline;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.powers.abstracts.AbstractFrontlinePower;
import theFrontline.util.UC;

import static theFrontline.util.UC.getPlayerChar;
import static theFrontline.util.UC.p;

public class OneShotPower extends AbstractFrontlinePower implements CloneablePowerInterface {
    public static final String POWER_ID = TheFrontline.makeID("OneShot");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private AbstractCharacterInfo ci;

    public OneShotPower(int amount, AbstractCharacterInfo ci) {
        name = NAME;
        ID = POWER_ID;
        this.owner = p();
        this.amount = amount;
        type = AbstractPower.PowerType.BUFF;
        this.ci = ci;
        updateDescription();
        loadRegion("lockon");
    }

    public OneShotPower(int amount) {
        this(amount, getPlayerChar());
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        if (type == DamageInfo.DamageType.NORMAL && getPlayerChar().id.equals(ci.id))
            return damage * 2;
        return damage;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        //Will probably not be compatible with certain cards, can be fixed via dynamic patching, maybe later.
        if(card.type == AbstractCard.CardType.ATTACK && getPlayerChar().id.equals(ci.id)) {
            flash();
            UC.att(new RemoveSpecificPowerAction(UC.p(), UC.p(), this));
        }
    }

    @Override
    public void updateDescription() {
        description = ci.name + DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new OneShotPower(amount);
    }
}