package theFrontline.cards.AR.INSAS;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.powers.SpicyPower;
import theFrontline.util.CardInfo;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.*;

public class SpicyFeast extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "SpicyFeast",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 4;
    private static final int MAGIC = 4;
    private static final int UPG_MAGIC = 2;

    public SpicyFeast() {
        super(cardInfo, false);
        p(); //Stupid intellij stuff , 

        setDamage(DAMAGE);
        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doDmg(m, this, AbstractGameAction.AttackEffect.FIRE);
        doPow(m, new SpicyPower(m, magicNumber));
    }
}