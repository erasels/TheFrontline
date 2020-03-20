package theFrontline.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;
import org.apache.commons.lang3.math.NumberUtils;
import theFrontline.TheFrontline;
import theFrontline.characters.characterInfo.AbstractCharacterInfo;
import theFrontline.ui.buttons.CharacterImageButton;
import theFrontline.ui.buttons.LabledButton;
import theFrontline.util.CharacterHelper;
import theFrontline.util.ScrapHelper;
import theFrontline.util.UC;

import java.util.ArrayList;

public class CharacterScrapScreen extends AbstractScreen implements ScrollBarListener {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(TheFrontline.makeID("CharScrapScreen"));
    public static final String[] TEXT = uiStrings.TEXT;
    private static final float SPACE = 250.0F * Settings.scale;
    private static final float START_X = Settings.WIDTH * 0.25f;
    private static final float START_Y = Settings.HEIGHT * 0.75f;
    private static float SCRAP_MULT = 0.75f;
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
    private boolean reDarken = false;

    private ArrayList<LabledButton> buttons = new ArrayList<>();
    private CharacterImageButton btnChar;
    private AbstractCharacterInfo charToAdd;

    public CharacterScrapScreen() {
        super(0);
        scrollBar = new ScrollBar(this);

        buttons.add(new LabledButton(Settings.WIDTH * 0.1f, Settings.HEIGHT * 0.15f, TEXT[0], false,
                () -> {
                    ScrapHelper.addScrap(getScrapVal());
                    close();
                    if(UC.pc().getCurrChar() == character) {
                        UC.pc().switchToNextCharacter();
                    }
                    UC.pc().killChar(character);
                    if(charToAdd != null) {
                        CharacterHelper.addCharacter(charToAdd);
                    }
                }, Color.FIREBRICK));

        //Prev
        buttons.add(new LabledButton(Settings.WIDTH * 0.1f, Settings.HEIGHT * 0.25f, TEXT[4], false,
                () -> {
                    int tmp = UC.pc().characters.indexOf(character) - 1;
                    if(tmp < 0)
                        tmp = UC.pc().characters.size() - 1;
                    character = UC.pc().characters.get(tmp);
                    btnChar = new CharacterImageButton(X_OFFSET / 2f, Settings.HEIGHT * 0.6f, character, TEXT[5], character.getDescription(), true);
                }, Color.SKY));

        //Next
        buttons.add(new LabledButton(Settings.WIDTH * 0.1f, Settings.HEIGHT * 0.325f, TEXT[3], false,
                () -> {
                    int tmp = UC.pc().characters.indexOf(character) + 1;
                    if(tmp > UC.pc().characters.size() - 1)
                        tmp = 0;
                    character = UC.pc().characters.get(tmp);
                    btnChar = new CharacterImageButton(X_OFFSET / 2f, Settings.HEIGHT * 0.6f, character, TEXT[5], character.getDescription(), true);
                }, Color.SKY));
    }

    public void open() {
        open(null);
    }

    public void open(AbstractCharacterInfo ci) {
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.overlayMenu.showBlackScreen(BLACKSCREEN_INTENSITY);
        AbstractDungeon.overlayMenu.proceedButton.hide();
        show = true;

        character = UC.pc().getCurrChar();
        targetY = scrollLowerBound;
        scrollY = Settings.HEIGHT - 400.0f * Settings.scale;

        btnChar = new CharacterImageButton(X_OFFSET / 2f, Settings.HEIGHT * 0.6f, character, TEXT[5], character.getDescription(), true);
        buttons.forEach(LabledButton::show);

        if(ci != null) {
            charToAdd = ci;
        }

        calculateScrollBounds();
    }

    public static AbstractRoom.RoomPhase roomPhase;

    public void close() {
        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.GRID;
        AbstractDungeon.closeCurrentScreen();

        buttons.forEach(LabledButton::hide);
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

        buttons.forEach(LabledButton::update);
        btnChar.update();

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

    public static final float X_OFFSET = Settings.WIDTH * 0.05f;

    public void render(SpriteBatch sb) {
        if (!shouldShow()) {
            return;
        }

        Color tCol;
        switch(character.getRarity()) {
            case RARE:
                tCol = Color.GOLDENROD;
                break;
            case UNCOMMON:
                tCol = Color.OLIVE;
                break;
            case COMMON:
                tCol = Color.SKY;
                break;
            default:
                tCol = Color.WHITE;
        }
        sb.setColor(tCol);
        Texture tIcon = CharacterHelper.getTypeIcon(character);
        float tmpImgW = character.img.getWidth() * Settings.scale;
        float tmpImgH = character.img.getHeight() * Settings.scale;
        sb.draw(tIcon, X_OFFSET / 2f, (Settings.HEIGHT * 0.6f) + (tmpImgH + (5*Settings.scale)), tIcon.getWidth() * Settings.scale, tIcon.getHeight() * Settings.scale, 0, 0, tIcon.getWidth(), tIcon.getHeight(), false, false);
        sb.setColor(Color.WHITE);
        btnChar.render(sb);
        float tmpAddTextOffset = NumberUtils.min(tmpImgW + (30f * Settings.scale), 200f * Settings.scale);
        FontHelper.renderFontLeft(sb, FontHelper.cardDescFont_L, CharacterHelper.getFlavorStatsString(character, false), (X_OFFSET / 2f) + tmpAddTextOffset, (Settings.HEIGHT * 0.6f) + ((tmpImgH) / 4f), Color.WHITE.cpy());

        String stats = CharacterHelper.getStatsString(character, false);
        FontHelper.renderFontLeft(sb, FontHelper.cardDescFont_L, stats, X_OFFSET / 2f, (Settings.HEIGHT * 0.6f) - ((tmpImgH) / 2f), Color.WHITE.cpy());

        //Scrap button
        FontHelper.renderFontLeft(sb, FontHelper.cardDescFont_L, TEXT[1] + getScrapVal() * 0.75 + TEXT[2], buttons.get(0).hb.cX - (buttons.get(0).hb.width/2), buttons.get(0).hb.cY - (buttons.get(0).hb.height), Color.WHITE.cpy());

        row = -1;
        col = 0;
        renderList(sb, getCards());


        scrollBar.render(sb);

        buttons.forEach(btn -> btn.render(sb));
    }

    private void updateList(ArrayList<AbstractCard> list) {
        for (AbstractCard r : list) {
            r.update();
            r.updateHoverLogic();
        }
    }

    private void renderList(SpriteBatch sb, ArrayList<AbstractCard> list) {
        row += 1;
        col = 0;
        for (AbstractCard r : list) {
            if (col == 5) {
                col = 0;
                row += 1;
            }
            r.target_x = r.current_x = (START_X + SPACE * col);
            r.target_y = r.current_y = (START_Y - (scrollY - START_Y) - (AbstractCard.IMG_HEIGHT_S * 1.15f) * row);
            r.render(sb);
            if (r.hb.hovered) {
                TipHelper.renderTipForCard(r, sb, r.keywords);
                if (r.cardsToPreview != null) {
                    r.renderCardPreview(sb);
                }
            }
            col += 1;
        }
    }

    public boolean shouldShow() {
        if(!(show)) {
            reDarken = true;
            return false;
        }
        if(reDarken) {
            AbstractDungeon.overlayMenu.showBlackScreen(BLACKSCREEN_INTENSITY);
            reDarken = false;
        }
        return true;
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
        if (character.masterDeck == null) {
            return new ArrayList<>();
        }
        return character.masterDeck.group;
    }

    private int getScrapVal() {
        return MathUtils.round(ScrapHelper.getScrapValue(character) * SCRAP_MULT);
    }
}
