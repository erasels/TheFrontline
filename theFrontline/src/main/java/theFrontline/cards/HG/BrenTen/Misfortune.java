package theFrontline.cards.HG.BrenTen;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.util.CardInfo;
import theFrontline.util.UC;
import theFrontline.vfx.combat.LoseGoldEffect;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.atb;

public class Misfortune extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Misfortune",
            1,
            CardType.SKILL,
            CardTarget.SELF);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int MAGIC = 7;

    public Misfortune() {
        this(false);
    }

    public Misfortune(boolean preventRecursion) {
        super(cardInfo, false);

        setMagic(MAGIC);
        switchCardOnPlay = true;
        if(!preventRecursion) {
            cardsToPreview = new StarOfHope(true);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractDungeon.effectsQueue.add(new LoseGoldEffect(magicNumber));
                UC.p().loseGold(magicNumber);
                isDone = true;
            }
        });
    }
}