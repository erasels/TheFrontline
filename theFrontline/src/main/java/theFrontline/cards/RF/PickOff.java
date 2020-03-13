package theFrontline.cards.RF;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.actions.utility.DoActionIfMonsterDeadAction;
import theFrontline.actions.utility.ModifyCostAction;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.util.CardInfo;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.*;

public class PickOff extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "PickOff",
            2,
            CardType.ATTACK,
            CardTarget.ENEMY);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 14;
    private static final int UPG_DAMAGE = 3;

    private static final int MAGIC = 1;

    public PickOff() {
        super(cardInfo, false);
        p(); //Stupid intellij stuff , 

        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doDmg(m, this);
        atb(new DoActionIfMonsterDeadAction(m, new ModifyCostAction(this.uuid, -magicNumber)));
    }
}