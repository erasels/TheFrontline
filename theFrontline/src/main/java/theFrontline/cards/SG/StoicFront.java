package theFrontline.cards.SG;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.powers.DamageToGracePower;
import theFrontline.util.CardInfo;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.*;

public class StoicFront extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "StoicFront",
            2,
            CardType.SKILL,
            CardTarget.SELF);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int BLOCK = 12;
    private static final int UPG_BLOCK = -3;

    public StoicFront() {
        super(cardInfo, false);
        p(); //Stupid intellij stuff s, s

        setBlock(BLOCK, UPG_BLOCK);
        setCostUpgrade(1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doDef(block);
        doPow(p, new DamageToGracePower(1));
    }
}