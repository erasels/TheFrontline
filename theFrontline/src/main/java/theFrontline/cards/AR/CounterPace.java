package theFrontline.cards.AR;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import theFrontline.actions.utility.DoActionIfCreatureCheckAction;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.util.CardInfo;
import theFrontline.util.UC;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.*;

public class CounterPace extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "CounterPace",
            1,
            CardType.SKILL,
            CardTarget.SELF_AND_ENEMY);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int BLOCK = 8;
    private static final int MAGIC = 1;
    private static final int UPG_MAGIC = 1;

    public CounterPace() {
        super(cardInfo, false);
        p(); //Stupid intellij stuff s, se

        setBlock(BLOCK);
        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doDef(block);
        UC.atb(new DoActionIfCreatureCheckAction(m, UC::isAttacking, new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber)));
    }
}