package theFrontline.actions.unique;

import com.megacrit.cardcrawl.actions.unique.GreedAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import theFrontline.util.UC;

public class LikeOnTVAction extends GreedAction {
    private int hpAmount, goldBak;
    private DamageInfo infoBak;
    public LikeOnTVAction(AbstractCreature target, DamageInfo info, int goldAmount, int hpAmount) {
        super(target, info, goldAmount);
        this.hpAmount = hpAmount;
        infoBak = info;
        goldBak = goldAmount;
    }

    @Override
    public void update() {
        if (duration == 0.1F && target != null) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, AttackEffect.BLUNT_HEAVY));
            target.damage(infoBak);
            if ((((AbstractMonster)target).isDying || target.currentHealth <= 0) && !target.halfDead && !target.hasPower(MinionPower.POWER_ID)) {
                AbstractDungeon.player.gainGold(goldBak);

                for(int i = 0; i < goldBak; ++i) {
                    AbstractDungeon.effectList.add(new GainPennyEffect(source, target.hb.cX, target.hb.cY, source.hb.cX, source.hb.cY, true));
                }

                UC.p().increaseMaxHp(hpAmount, true);
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        tickDuration();
    }
}
