package theFrontline.cards.SMG;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.util.CardInfo;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.*;

public class Zip extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Zip",
            1,
            CardType.SKILL,
            CardTarget.SELF);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int BLOCK = 6;
    private static final int UPG_BLOCK = 1;

    private static final int MAGIC = 1;
    private static final int UPG_MAGIC = 3;

    public Zip() {
        super(cardInfo, false);
        p(); //Stupid intellij stuff s, s

        setBlock(BLOCK, UPG_BLOCK);
        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doDef(block);
        doDef(magicNumber);
    }

    @Override
    public void applyPowers() {
        int tmp = baseBlock;
        baseBlock = baseMagicNumber;

        super.applyPowers();

        magicNumber = block;
        baseBlock = tmp;

        super.applyPowers();

        isMagicNumberModified = magicNumber != baseMagicNumber;
    }
}