package theFrontline.cards.SMG;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.actions.common.FlankAction;
import theFrontline.actions.utility.DelayActionAction;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.util.CardInfo;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.*;

public class Conceal extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Conceal",
            1,
            CardType.SKILL,
            CardTarget.ALL_ENEMY);

    public final static String ID = makeID(cardInfo.cardName);


    public Conceal() {
        super(cardInfo, false);
        p(); //Stupid intellij stuff s, ae
        setCostUpgrade(0);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new DelayActionAction(new FlankAction()));
    }
}