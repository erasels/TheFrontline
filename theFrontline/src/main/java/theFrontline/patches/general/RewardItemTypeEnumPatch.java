package theFrontline.patches.general;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.rewards.RewardItem;

public class RewardItemTypeEnumPatch {
    @SpireEnum
    public static RewardItem.RewardType SCRAP;
    @SpireEnum
    public static RewardItem.RewardType FRONTLINER;
}