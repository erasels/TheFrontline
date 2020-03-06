package theFrontline.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;
import javassist.CtBehavior;
import theFrontline.TheFrontline;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.characters.characterInfo.frontline.AR.F2000;
import theFrontline.ui.buttons.LabledButton;
import theFrontline.util.CharacterHelper;
import theFrontline.util.UC;

import java.util.ArrayList;

public class CharacterAddScreen extends AbstractScreen implements ScrollBarListener {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(TheFrontline.makeID("CharAddScreen"));
    public static final String[] TEXT = uiStrings.TEXT;
    private static final float SPACE = 250.0F * Settings.scale;
    private static final float START_X = Settings.WIDTH * 0.25f;
    private static final float START_Y = Settings.HEIGHT * 0.75f;
    private AbstractCharacterInfo character;
    private float scrollY = START_Y;
    private float targetY = this.scrollY;
    private float scrollLowerBound = Settings.HEIGHT - 200.0F * Settings.scale;
    private float scrollUpperBound = scrollLowerBound + Settings.DEFAULT_SCROLL_LIMIT;//2600.0F * Settings.scale;
    private int row = 0;
    private int col = 0;
    private boolean grabbedScreen = false;
    private float grabStartY = 0.0F;
    private ScrollBar scrollBar;
    private boolean show = false;

    private LabledButton btnAccept;
    private LabledButton btnScrap;

    public CharacterAddScreen(float duration) {
        super(duration);
        scrollBar = new ScrollBar(this);
        btnAccept = new LabledButton(Settings.WIDTH * 0.1f, Settings.HEIGHT * 0.25f, TEXT[0], false,
                () -> {
                    UC.pc().characters.add(this.character);
                    close();
                }, Color.FOREST);

        btnScrap = new LabledButton(Settings.WIDTH * 0.1f, Settings.HEIGHT * 0.15f, TEXT[1], false,
                () -> close(), Color.ROYAL);
    }

    public void open(AbstractCharacterInfo ci) {
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.overlayMenu.showBlackScreen(BLACKSCREEN_INTENSITY);
        AbstractDungeon.overlayMenu.proceedButton.hide();
        show = true;

        character = ci;
        targetY = scrollLowerBound;
        scrollY = Settings.HEIGHT - 400.0f * Settings.scale;

        btnAccept.show();
        btnScrap.show();

        calculateScrollBounds();
    }

    public static AbstractRoom.RoomPhase roomPhase;

    @SpirePatch(clz = RewardItem.class, method = "claimReward")
    public static class tets {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<Boolean> patch(RewardItem __instance) {
            if (AbstractDungeon.isScreenUp) {
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.overlayMenu.cancelButton.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
            }
            roomPhase = AbstractDungeon.getCurrRoom().phase;
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;

            AbstractDungeon.closeCurrentScreen();

            TheFrontline.screen = new CharacterAddScreen(1f);
            ((CharacterAddScreen) TheFrontline.screen).open(new F2000());

            return SpireReturn.Return(true);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
                return new int[]{LineFinder.findAllInOrder(ctBehavior, finalMatcher)[1]};
            }
        }
    }

    public void close() {
        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.GRID;
        AbstractDungeon.closeCurrentScreen();

        btnAccept.hide();
        btnScrap.hide();
        isDone = true;
        show = false;
        AbstractDungeon.getCurrRoom().phase = roomPhase;
        roomPhase = null;
    }

    @Override
    public void update() {
        if (!shouldShow()) {
            return;
        }

        btnAccept.update();
        btnScrap.update();

        boolean isScrollingScrollBar = scrollBar.update();
        if (!isScrollingScrollBar) {
            updateScrolling();
        }

        updateList(getCards());
    }

    private void updateScrolling() {
        int y = InputHelper.mY;
        if (!grabbedScreen) {
            if (InputHelper.scrolledDown) {
                targetY += Settings.SCROLL_SPEED;
            } else if (InputHelper.scrolledUp) {
                targetY -= Settings.SCROLL_SPEED;
            }
            if (InputHelper.justClickedLeft) {
                grabbedScreen = true;
                grabStartY = (y - targetY);
            }
        } else if (InputHelper.isMouseDown) {
            targetY = (y - grabStartY);
        } else {
            grabbedScreen = false;
        }
        scrollY = MathHelper.scrollSnapLerpSpeed(scrollY, targetY);
        resetScrolling();
        updateBarPosition();
    }

    private void calculateScrollBounds() {
        int size = getCards().size();

        int scrollTmp = 0;
        if (size > 5) {
            scrollTmp = size / 5 - 2;
            if (size % 10 != 0) {
                ++scrollTmp;
            }
            scrollUpperBound = scrollLowerBound + Settings.DEFAULT_SCROLL_LIMIT + scrollTmp * 80.0f * Settings.scale;
        } else {
            scrollUpperBound = scrollLowerBound + Settings.DEFAULT_SCROLL_LIMIT;
        }
    }

    private void resetScrolling() {
        if (targetY < scrollLowerBound) {
            targetY = MathHelper.scrollSnapLerpSpeed(targetY, scrollLowerBound);
        } else if (targetY > scrollUpperBound) {
            targetY = MathHelper.scrollSnapLerpSpeed(targetY, scrollUpperBound);
        }
    }

    private void updateList(ArrayList<AbstractCard> list) {
        for (AbstractCard r : list) {
            r.hb.move(r.current_x, r.current_y);
            r.update();
        }
    }

    public static final float X_OFFSET = Settings.WIDTH * 0.05f;

    public void render(SpriteBatch sb) {
        if (!shouldShow()) {
            return;
        }


        sb.setColor(Color.WHITE);
        float tmpImgW = character.img.getWidth();
        float tmpImgH = character.img.getHeight();
        sb.draw(character.img, X_OFFSET - ((tmpImgW * Settings.scale) * 0.5f), Settings.HEIGHT * 0.6f, tmpImgW * Settings.scale, tmpImgH * Settings.scale, 0, 0, (int) tmpImgW, (int) tmpImgH, false, false);

        FontHelper.renderFontLeft(sb, FontHelper.cardDescFont_L, CharacterHelper.getFlavorStatsString(character, false), ((tmpImgW * Settings.scale) / 2f) - (X_OFFSET - (40f * Settings.scale)), (Settings.HEIGHT * 0.6f) + ((tmpImgH * Settings.scale) / 4f), Color.WHITE.cpy());

        String stats = CharacterHelper.getStatsString(character, false);
        String effect = CharacterHelper.getEffectString(character);
        FontHelper.renderFontLeft(sb, FontHelper.cardDescFont_L, stats, X_OFFSET/2f, (Settings.HEIGHT * 0.6f) - ((tmpImgH * Settings.scale) / 4f), Color.WHITE.cpy());
        //TODO: Put this into a tip that renders on hovering the character portrait
        FontHelper.renderFontLeft(sb, FontHelper.cardDescFont_L, effect, START_X - SPACE, Settings.HEIGHT * 0.85f, Color.WHITE.cpy());

        row = -1;
        col = 0;
        renderList(sb, getCards());

        scrollBar.render(sb);

        btnAccept.render(sb);
        btnScrap.render(sb);
    }

    private void renderList(SpriteBatch sb, ArrayList<AbstractCard> list) {
        row += 1;
        col = 0;
        for (AbstractCard r : list) {
            if (col == 5) {
                col = 0;
                row += 1;
            }
            r.current_x = (START_X + SPACE * col);
            r.current_y = (START_Y - (scrollY - START_Y) - (SPACE * 2) * row);
            r.render(sb);
            col += 1;
        }
    }

    public boolean shouldShow() {
        return show && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.SETTINGS;
    }

    @Override
    public void scrolledUsingBar(float newPercent) {
        float newPosition = MathHelper.valueFromPercentBetween(scrollLowerBound, scrollUpperBound, newPercent);
        scrollY = newPosition;
        targetY = newPosition;
        updateBarPosition();
    }

    private void updateBarPosition() {
        float percent = MathHelper.percentFromValueBetween(scrollLowerBound, scrollUpperBound, scrollY);
        scrollBar.parentScrolledToPercent(percent);
    }

    private ArrayList<AbstractCard> getCards() {
        return character.getStarterDeck();
    }
}
