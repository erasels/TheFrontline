package theFrontline.cards.RF.G43;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.actions.unique.DauerfeuerAction;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.util.CardInfo;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.*;

public class Dauerfeuer extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Dauerfeuer",
            1,
            CardType.SKILL,
            CardTarget.ENEMY);

    public final static String ID = makeID(cardInfo.cardName);


    public Dauerfeuer() {
        super(cardInfo, false);
        p(); //Stupid intellij stuff s, 

        setCostUpgrade(0);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new DauerfeuerAction(m));
    }
}