package theFrontline.cards.basic;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.util.CardInfo;
import theFrontline.vfx.general.ButtonConfirmedEffect;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.doDmg;
import static theFrontline.util.UC.doVfx;

public class TreasuredOrb extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "TreasuredOrb",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DMG = 9;

    public TreasuredOrb() {
        super(cardInfo, true);
        setDamage(DMG);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doVfx(new ButtonConfirmedEffect(m.hb.cX, m.hb.cY, Color.SALMON, 2f, 3f));
        //doVfx(new FlashImageEffect(TextureLoader.getTexture(makeOrbPath("YinYangOrb.png")), m.hb.cX, m.hb.cY, Color.WHITE, 1f, 1.5f));
        doDmg(m, damage, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }

}