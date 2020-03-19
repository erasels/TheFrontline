package theFrontline.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import theFrontline.TheFrontline;
import theFrontline.characters.FrontlineCharacter;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.rewards.ScrapReward;

public class ScrapHelper {
    public static final int CARD_RARE_VAL = 10;
    public static final int CARD_UNCM_VAL = 5;
    public static final int CARD_CMMN_VAL = 2;

    public static int getScrap() {
        FrontlineCharacter p = UC.pc();
        if(p != null) {
            return p.scrap;
        }
        return -1;
    }

    public static void addScrap(int i) {
        addScrap(i, true);
    }

    public static void addScrap(int i, boolean showEffect) {
        FrontlineCharacter p = UC.pc();
        if(p != null) {
            if(showEffect) {
                CardCrawlGame.sound.play("RELIC_DROP_CLINK");
                TheFrontline.scrapDisplay.flash();
            }
            p.scrap += i;
        }
    }

    public static void loseScrap(int i) {
        addScrap(-i);
    }

    public static int getScrapValue(AbstractCharacterInfo ci) {
        int scrap = ci.getScrapValue();
        for(AbstractCard c: ci.masterDeck.group) {
            switch (c.rarity) {
                case RARE:
                    scrap += CARD_RARE_VAL;
                    break;
                case SPECIAL:
                case UNCOMMON:
                    scrap += CARD_UNCM_VAL;
                    break;
                case COMMON:
                    scrap += CARD_CMMN_VAL;
                    break;
                default:
                    scrap += 1;
            }
        }
        return scrap;
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
