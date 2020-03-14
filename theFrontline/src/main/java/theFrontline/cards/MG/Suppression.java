package theFrontline.cards.MG;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.powers.SuppressionPower;
import theFrontline.util.CardInfo;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.*;

public class Suppression extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Suppression",
            2,
            CardType.SKILL,
            CardTarget.SELF_AND_ENEMY);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 8;
    private static final int UPG_DAMAGE = 3;

    private static final int BLOCK = 8;
    private static final int UPG_BLOCK = 1;


    public Suppression() {
        super(cardInfo, false);
        p(); //Stupid intellij stuff s, se

        setDamage(DAMAGE, UPG_DAMAGE);
        setBlock(BLOCK, UPG_BLOCK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doDef(block);
        doPow(m, new SuppressionPower(m, damage));
    }
}