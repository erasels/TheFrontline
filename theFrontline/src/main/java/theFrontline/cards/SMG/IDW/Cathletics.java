package theFrontline.cards.SMG.IDW;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.powers.GracePower;
import theFrontline.util.CardInfo;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.*;

public class Cathletics extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Cathletics",
            1,
            CardType.SKILL,
            CardTarget.SELF);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int MAGIC = 8;
    private static final int UPG_MAGIC = 3;

    public Cathletics() {
        super(cardInfo, false);
        p(); //Stupid intellij stuff s, s

        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doPow(p, new GracePower(magicNumber));
    }
}