package theFrontline.cards.RF;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.powers.OneShotPower;
import theFrontline.util.CardInfo;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.*;

public class OneShot extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "OneShot",
            2,
            CardType.SKILL,
            CardTarget.SELF);

    public final static String ID = makeID(cardInfo.cardName);


    public OneShot() {
        super(cardInfo, false);
        p(); //Stupid intellij stuff s, s

        setCostUpgrade(1);
        setExhaust(true);
        setMagic(1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doPow(p, new OneShotPower(magicNumber));
    }
}