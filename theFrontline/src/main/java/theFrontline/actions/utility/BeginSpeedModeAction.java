package theFrontline.actions.utility;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import theFrontline.mechanics.speed.AbstractSpeedTime;
import theFrontline.util.UC;
import theFrontline.vfx.general.BeginSpeedModeEffect;

import static theFrontline.TheFrontline.makeID;

public class BeginSpeedModeAction extends AbstractGameAction {
    AbstractSpeedTime instance;
    private static final float START_DURATION = 1.5f;
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("SpeedModePrepText"));

    public BeginSpeedModeAction(AbstractSpeedTime instance) {
        this.instance = instance;
    }

    public void update() {
        UC.doVfx(new BorderLongFlashEffect(Color.WHITE.cpy()));
        UC.doVfx(new BeginSpeedModeEffect(Color.FIREBRICK, uiStrings.TEXT[0], START_DURATION, instance));
        isDone = true;
    }
}
