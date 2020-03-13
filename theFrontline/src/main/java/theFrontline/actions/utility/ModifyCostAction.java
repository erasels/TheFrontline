package theFrontline.actions.utility;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import theFrontline.patches.cards.CostModifiedUntilPlayedPatches;

import java.util.UUID;

public class ModifyCostAction extends AbstractGameAction {
    private final UUID uuid;
    private boolean untilUsed;

    public ModifyCostAction(UUID targetUUID, int amount) {
        this.amount = amount;
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.uuid = targetUUID;
        untilUsed = false;
    }

    public ModifyCostAction(UUID targetUUID, int amount, boolean untilUsed) {
        this(targetUUID, amount);
        this.untilUsed = untilUsed;
    }

    public void update() {
        for (AbstractCard c : GetAllInBattleInstances.get(uuid)) {
            if(!untilUsed) {
                c.modifyCostForCombat(amount);
            } else {
                c.setCostForTurn(c.costForTurn+amount);
                CostModifiedUntilPlayedPatches.Fields.isCostModifiedUntilPlayed.set(c, true);
            }

            if (AbstractDungeon.player.hand.contains(c))
                c.superFlash();
        }
        this.isDone = true;
    }
}