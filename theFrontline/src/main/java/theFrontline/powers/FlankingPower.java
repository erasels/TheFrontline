package theFrontline.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theFrontline.TheFrontline;
import theFrontline.patches.combat.NewPowerTypePatches;
import theFrontline.powers.abstracts.AbstractFrontlinePower;
import theFrontline.util.UC;

import static theFrontline.util.UC.atb;
import static theFrontline.util.UC.p;

public class FlankingPower extends AbstractFrontlinePower implements CloneablePowerInterface {
    public static final String POWER_ID = TheFrontline.makeID("Flanked");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final int DMG_INC = 25;

    public FlankingPower(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;
        this.priority = 0;
        this.owner = owner;
        this.amount = -1;
        this.isTurnBased = false;
        type = NewPowerTypePatches.NEUTRAL;
        updateDescription();
        loadRegion("surrounded");
    }

    @Override
    public void onInitialApplication() {
        owner.flipHorizontal = true;
    }

    @Override
    public void onRemove() {
        owner.flipHorizontal = false;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        atb(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void atStartOfTurn() {
        atb(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * convert();
        }
        return damage;
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        if (damageType == DamageInfo.DamageType.NORMAL) {
            return damage * convert();
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + DMG_INC + DESCRIPTIONS[1] + DMG_INC + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new FlankingPower(owner);
    }

    private float convert() {
        return 1f + (DMG_INC / 100f);
    }
}