package theFrontline.cards.AR;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.util.CardInfo;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.doDmg;
import static theFrontline.util.UC.p;

public class Deterrent extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Deterrent",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 6;
    private static final int UPG_DAMAGE = 2;

    public Deterrent() {
        super(cardInfo, false);
        p(); //Stupid intellij stuff , 

        setDamage(DAMAGE, UPG_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doDmg(m, this);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int tmp = baseDamage;
        if(mo.currentHealth > mo.maxHealth/2) {
            baseDamage *= 2;
        }
        super.calculateCardDamage(mo);
        if(tmp != baseDamage) {
            isDamageModified = true;
        }
        baseDamage = tmp;
    }
}