package theFrontline.cards.HG;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.powers.GracePower;
import theFrontline.util.CardInfo;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.*;

public class CheerOn extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "CheerOn",
            1,
            CardType.SKILL,
            CardTarget.SELF);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int MAGIC = 4;
    private static final int UPG_MAGIC = 2;

    public CheerOn() {
        super(cardInfo, false);
        p(); //Stupid intellij stuff s, s

        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doPow(p, new VigorPower(p, magicNumber));
        doPow(p, new GracePower(p, magicNumber));
    }
}