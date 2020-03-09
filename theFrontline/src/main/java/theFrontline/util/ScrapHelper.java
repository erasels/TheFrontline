package theFrontline.util;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import theFrontline.TheFrontline;
import theFrontline.characters.FrontlineCharacter;
import theFrontline.rewards.ScrapReward;

public class ScrapHelper {
    public static int getScrap() {
        FrontlineCharacter p = UC.pc();
        if(p != null) {
            return p.scrap;
        }
        return -1;
    }

    public static void addScrap(int i) {
        FrontlineCharacter p = UC.pc();
        if(p != null) {
            CardCrawlGame.sound.play("RELIC_DROP_CLINK");
            TheFrontline.scrapDisplay.flash();
            p.scrap += i;
        }
    }

    public static void loseScrap(int i) {
        addScrap(-i);
    }

    public static void modifyCombatRewards(CombatRewardScreen crs) {
        if (CardCrawlGame.isInARun()) {
            AbstractRoom room = AbstractDungeon.getCurrRoom();
            if(room instanceof MonsterRoomBoss) {
                crs.rewards.add(new ScrapReward(30));
            } else if(room instanceof MonsterRoomElite || room instanceof TreasureRoom) {
                crs.rewards.add(new ScrapReward(10));
            }
        }
    }
}
