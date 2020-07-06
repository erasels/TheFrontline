package theFrontline.cardModifiers;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DauerfeuerCMod extends AbstractCardModifier {

    public DauerfeuerCMod() {
        priority = -99;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.freeToPlayOnce = true;
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        return true;
    }



    @Override
    public float modifyDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {



        if(type == DamageInfo.DamageType.NORMAL) {
            return damage/2f;
        }

        return damage;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new DauerfeuerCMod();
    }
}
