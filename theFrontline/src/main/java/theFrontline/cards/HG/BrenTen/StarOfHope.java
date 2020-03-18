package theFrontline.cards.HG.BrenTen;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.util.CardInfo;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.doDmg;
import static theFrontline.util.UC.doPow;

public class StarOfHope extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "StarOfHope",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 13;
    private static final int MAGIC = 5;

    public StarOfHope() {
        this(false);
    }

    public StarOfHope(boolean preventRecursion) {
        super(cardInfo, true);

        setDamage(DAMAGE);
        setMagic(MAGIC);
        switchCardOnPlay = true;
        if(!preventRecursion) {
            cardsToPreview = new Misfortune(true);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doDmg(m, this, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        if(upgraded) {
            doPow(p, new VigorPower(p, magicNumber));
        }
    }
}