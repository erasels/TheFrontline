package theFrontline.cards.HG;

import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.powers.BlockOnSwitchPower;
import theFrontline.util.CardInfo;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.*;

public class PackTactics extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "PackTactics",
            0,
            CardType.SKILL,
            CardTarget.SELF);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int MAGIC = 2;
    private static final int UPG_MAGIC = 1;
    private static final int EX = 2;

    public PackTactics() {
        super(cardInfo, false);
        p(); //Stupid intellij stuff s, s

        setMagic(MAGIC, UPG_MAGIC);
        ExhaustiveVariable.setBaseValue(this, EX);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doPow(p, new BlockOnSwitchPower(magicNumber));
    }
}