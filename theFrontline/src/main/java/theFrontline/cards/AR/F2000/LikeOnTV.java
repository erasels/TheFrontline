package theFrontline.cards.AR.F2000;

import com.megacrit.cardcrawl.actions.unique.GreedAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.actions.common.RaisePlayerMaxHPAction;
import theFrontline.actions.utility.DoActionIfMonsterDeadAction;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.util.CardInfo;
import theFrontline.util.UC;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.atb;

public class LikeOnTV extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "LikeOnTV",
            2,
            CardType.ATTACK,
            CardTarget.ENEMY);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 12;
    private static final int UPG_DAMAGE = 2;

    private static final int MAGIC = 10;
    private static final int UPG_MAGIC = 2;

    private static final int MAGIC2 = 2;

    public LikeOnTV() {
        super(cardInfo, false);

        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(MAGIC, UPG_MAGIC);
        magicNumber2 = baseMagicNumber2 = MAGIC2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new GreedAction(m, UC.getDmg(m, this), magicNumber));
        atb(new DoActionIfMonsterDeadAction(m, new RaisePlayerMaxHPAction(magicNumber2, true)));
    }
}