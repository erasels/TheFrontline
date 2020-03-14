package theFrontline.cards.MG;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.actions.unique.SprayNPrayAction;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.util.CardInfo;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.*;

public class SprayNPray extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "SprayNPray",
            1,
            CardType.ATTACK,
            CardTarget.ALL_ENEMY);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 3;
    private static final int UPG_DAMAGE = 2;

    private static final int MAGIC = 2;

    public SprayNPray() {
        super(cardInfo, false);
        p(); //Stupid intellij stuff , alle

        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new SprayNPrayAction(this, AbstractGameAction.AttackEffect.BLUNT_LIGHT, magicNumber));
    }
}