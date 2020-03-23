package theFrontline.cards.AR;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.util.CardInfo;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.*;

public class Deterrent extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Deterrent",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 6;

    private static final int MAGIC = 2;
    private static final int UPG_MAGIC = 2;

    public Deterrent() {
        super(cardInfo, false);

        setDamage(DAMAGE);
        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doDmg(m, this, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        atb(new ModifyDamageAction(uuid, magicNumber));
    }
}