package theFrontline.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theFrontline.TheFrontline;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.powers.abstracts.AbstractFrontlinePower;

import static theFrontline.util.UC.doDef;
import static theFrontline.util.UC.p;

public class BlockOnSwitchPower extends AbstractFrontlinePower implements CloneablePowerInterface {
    public static final String POWER_ID = TheFrontline.makeID("BlockOnSwitch");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public BlockOnSwitchPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = AbstractPower.PowerType.BUFF;
        updateDescription();
        loadRegion("like_water");
    }

    public BlockOnSwitchPower(int amount) {
        this(p(), amount);
    }

    @Override
    public void onSwitch(AbstractCharacterInfo currChar, AbstractCharacterInfo nextChar) {
        doDef(amount);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new BlockOnSwitchPower(owner, amount);
    }
}