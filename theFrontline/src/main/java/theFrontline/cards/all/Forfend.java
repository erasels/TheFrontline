package theFrontline.cards.all;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.powers.IncreaseSwitchCostPower;
import theFrontline.util.CardInfo;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.*;

public class Forfend extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Forfend",
            1,
            CardType.SKILL,
            CardTarget.SELF);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int BLOCK = 9;
    private static final int UPG_BLOCK = 3;

    private static final int MAGIC = 1;

    public Forfend() {
        super(cardInfo, false);

        setBlock(BLOCK, UPG_BLOCK);
        setMagic(MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doDef(block);
        doPow(p, new IncreaseSwitchCostPower(magicNumber));
    }
}