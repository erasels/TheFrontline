package theFrontline.cards.HG;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.util.CardInfo;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.*;

public class AimedShot extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "AimedShot",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 6;
    private static final int UPG_DAMAGE = 1;

    private static final int MAGIC = 1;
    private static final int UPG_MAGIC = 1;

    public AimedShot() {
        super(cardInfo, false);
        p(); //Stupid intellij stuff , 

        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doDmg(m, this);
        doPow(m, new VulnerablePower(m, magicNumber, false));
    }
}