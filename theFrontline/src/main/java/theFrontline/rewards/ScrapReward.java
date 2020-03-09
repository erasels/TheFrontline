package theFrontline.rewards;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import theFrontline.TheFrontline;
import theFrontline.patches.general.RewardItemTypeEnumPatch;
import theFrontline.util.ScrapHelper;
import theFrontline.util.TextureLoader;

public class ScrapReward extends CustomReward {
    private static final Texture ICON = TextureLoader.getTexture(TheFrontline.makeUIPath("scrapReward.png"));
    public static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(TheFrontline.makeID("ScrapReward"));

    public int amountOfScraps;

    public ScrapReward(){
        this(1);
    }

    public ScrapReward(int amount) {
        super(ICON, amount + (amount > 1 ? uiStrings.TEXT[1]:uiStrings.TEXT[0]), RewardItemTypeEnumPatch.SCRAP);
        this.amountOfScraps = amount;
    }

    @Override
    public boolean claimReward() {
        ScrapHelper.addScrap(amountOfScraps);
        return true;
    }
}