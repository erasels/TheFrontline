package theFrontline.cards.MG.AAT52;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.powers.GracePower;
import theFrontline.util.CardInfo;
import theFrontline.util.UC;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.doDmg;

public class StumblingShot extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "StumblingShot",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 8;
    private static final int UPG_DAMAGE = 3;


    public StumblingShot() {
        super(cardInfo, false);
        useGrace = true;

        setDamage(DAMAGE, UPG_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doDmg(m, this, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        GracePower tmp = (GracePower) UC.p().getPower(GracePower.POWER_ID);
        if(tmp != null) {
            damage += tmp.amount;
            isDamageModified = true;
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        GracePower tmp = (GracePower) UC.p().getPower(GracePower.POWER_ID);
        if(tmp != null) {
            damage += tmp.amount;
            isDamageModified = true;
        }
    }
}