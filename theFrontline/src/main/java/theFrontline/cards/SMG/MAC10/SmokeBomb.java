package theFrontline.cards.SMG.MAC10;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.VerticalAuraEffect;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.powers.CoverPower;
import theFrontline.util.CardInfo;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.*;

public class SmokeBomb extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "SmokeBomb",
            2,
            CardType.SKILL,
            CardTarget.SELF);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int BLOCK = 8;
    private static final int MAGIC = 3;

    public SmokeBomb() {
        super(cardInfo, false);
        p(); //Stupid intellij stuff s, s

        setBlock(BLOCK);
        setMagic(MAGIC);
        setCostUpgrade(1);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doVfx(new VerticalAuraEffect(Color.LIGHT_GRAY, p.hb.cX, p.hb.cY));
        doDef(block);
        doPow(p, new CoverPower(magicNumber));
    }
}