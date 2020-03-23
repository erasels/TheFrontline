package theFrontline.relics.abstracts;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import theFrontline.TheFrontline;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.util.TextureLoader;

public abstract class FrontlineRelic extends AbstractRelic {
    public FrontlineRelic(String setId, String imgName, RelicTier tier, LandingSound sfx) {
        super(setId, "", tier, sfx);

        imgUrl = imgName;

        if (img == null || outlineImg == null) {
            img = TextureLoader.getTexture(TheFrontline.makeRelicPath(imgName));
            largeImg = TextureLoader.getTexture(TheFrontline.makeRelicPath(imgName));
            outlineImg = TextureLoader.getTexture(TheFrontline.makeRelicOutlinePath(imgName));
        }
    }

    public void onCharacterSwitch(boolean inCombat) {}
    public int characterSwitchCost(int cost, AbstractCharacterInfo ci){ return cost;}
}
