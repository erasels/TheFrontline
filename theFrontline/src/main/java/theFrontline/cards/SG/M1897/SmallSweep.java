package theFrontline.cards.SG.M1897;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.actions.utility.DoActionIfMonsterDeadAction;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.util.CardInfo;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.*;

public class SmallSweep extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "SmallSweep",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 8;
    private static final int UPG_DAMAGE = 2;

    private static final int MAGIC = 2;
    private static final int UPG_MAGIC = 1;

    public SmallSweep() {
        super(cardInfo, false);
        p(); //Stupid intellij stuff , 

        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doDmg(m, this);
        atb(new DoActionIfMonsterDeadAction(m, new HealAction(p, p, magicNumber)));
    }
}