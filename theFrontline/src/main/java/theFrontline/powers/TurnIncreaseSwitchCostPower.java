package theFrontline.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theFrontline.TheFrontline;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.powers.abstracts.AbstractFrontlinePower;
import theFrontline.util.UC;

public class TurnIncreaseSwitchCostPower extends AbstractFrontlinePower implements CloneablePowerInterface {
    public static final String POWER_ID = TheFrontline.makeID("LimitedIncreaseSwitchCost");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public TurnIncreaseSwitchCostPower(int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = UC.p();
        this.amount = amount;
        type = PowerType.DEBUFF;
        updateDescription();
        loadRegion("skillBurn");
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if(isPlayer) {
            UC.removePower(this);
        }
    }

    @Override
    public void onSwitch(AbstractCharacterInfo currChar, AbstractCharacterInfo nextChar) {
        flash();
        UC.removePower(this);
    }

    @Override
    public int characterSwitchCost(int cost, AbstractCharacterInfo ci) {
        return cost + amount;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new TurnIncreaseSwitchCostPower(amount);
    }
}