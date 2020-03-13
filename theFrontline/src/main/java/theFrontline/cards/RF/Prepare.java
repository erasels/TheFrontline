package theFrontline.cards.RF;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.actions.common.CallbackDiscardAction;
import theFrontline.actions.utility.ModifyCostAction;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.util.CardInfo;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.*;

public class Prepare extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Prepare",
            1,
            CardType.SKILL,
            CardTarget.SELF);

    public final static String ID = makeID(cardInfo.cardName);

    public Prepare() {
        super(cardInfo, true);
        p(); //Stupid intellij stuff s, s

        setMagic(1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new CallbackDiscardAction(1, !upgraded, c -> att(new ModifyCostAction(c.uuid, -magicNumber, true))));
    }
}