package theFrontline.cards.HG.Tokarev;

import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.TheFrontline;
import theFrontline.actions.utility.DoActionIfCreatureCheckAction;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.util.CardInfo;
import theFrontline.util.UC;

import java.lang.reflect.Field;

import static theFrontline.TheFrontline.makeID;
import static theFrontline.util.UC.atb;

public class Charm extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Charm",
            2,
            CardType.SKILL,
            CardTarget.ENEMY);
    private static Field multiIntentField;

    public final static String ID = makeID(cardInfo.cardName);

    private static final int EX = 2;

    public Charm() {
        super(cardInfo, true);

        setExhaust(true, false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(multiIntentField == null) {
            try {
                multiIntentField = AbstractMonster.class.getDeclaredField("intentMultiAmt");
                multiIntentField.setAccessible(true);
            } catch (Exception e) {
                TheFrontline.logger.error("Exception occurred when getting private field " + "intentMultiAmt" + " of " + AbstractMonster.class.getName(), e);
            }
        }

        AbstractMonster target = AbstractDungeon.getRandomMonster(m);
        if(target == null)
            target = m;
        AbstractMonster finalTarget = target;

        atb(new DoActionIfCreatureCheckAction(m, UC::isAttacking, () -> {
            int i = (int) UC.getFieldContent(multiIntentField, m);
            for (int j = 0; j < i; j++) {
                UC.att(new DamageAction(finalTarget, new DamageInfo(m, m.getIntentDmg(), DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            }
        }, null));
    }

    @Override
    public void upgrade() {
        if(!upgraded) {
            ExhaustiveVariable.setBaseValue(this, EX);
        }
        super.upgrade();
    }
}