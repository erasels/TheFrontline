package theFrontline.cards.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theFrontline.actions.utility.SwitchCharacterCombatAction;
import theFrontline.cards.abstracts.FrontlineCard;
import theFrontline.util.CardInfo;
import theFrontline.util.UC;

import static theFrontline.TheFrontline.makeID;

public class Defend extends FrontlineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Defend",
            1,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.BASIC
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int BLOCK = 5;
    private static final int UPG_BLOCK = 3;

    public Defend() {
        super(cardInfo, false);

        setBlock(BLOCK, UPG_BLOCK);
        tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        UC.doDef(block);
        if (CardCrawlGame.playerName.toLowerCase().equals("rordev")) {
            UC.att(new SwitchCharacterCombatAction(UC.pc().characters.stream().filter(ci -> !ci.id.equals(UC.pc().getCurrChar().id)).findFirst().get(), 1));
        }
    }
}