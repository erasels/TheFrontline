package theFrontline.cards.SMG;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.actions.utility.DoActionIfCreatureCheckAction;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.powers.FlankingPower;
import theFrontline.util.CardInfo;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.*;

public class Bushwack extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Bushwack",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 8;
    private static final int UPG_DAMAGE = 2;


    public Bushwack() {
        super(cardInfo, false);
        p(); //Stupid intellij stuff , 

        setDamage(DAMAGE, UPG_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doDmg(m, this, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        atb(new DoActionIfCreatureCheckAction(m, mo -> mo.hasPower(FlankingPower.POWER_ID), new DamageAction(m, getDmg(m, this), AbstractGameAction.AttackEffect.BLUNT_HEAVY)));
    }
}