package theFrontline.cards.MG;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.util.CardInfo;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.*;

public class Shredder extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Shredder",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 9;
    private static final int UPG_DAMAGE = 3;


    public Shredder() {
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
        if(mo.currentBlock > 0) {
            baseDamage *= 2;
        }
        super.calculateCardDamage(mo);
        if(tmp != baseDamage) {
            isDamageModified = true;
        }
        baseDamage = tmp;
    }
}