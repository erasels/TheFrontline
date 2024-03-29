package theFrontline.cards.abstracts;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.CommonKeywordIconsField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import theFrontline.TheFrontline;
import theFrontline.characters.FrontlineCharacter;
import theFrontline.patches.cards.CardENUMs;
import theFrontline.util.CardInfo;
import theFrontline.util.TextureLoader;
import theFrontline.util.UC;

import static theFrontline.TheFrontline.makeID;

public abstract class FrontlineCard extends CustomCard {
    protected CardStrings cardStrings;
    protected String img;

    protected boolean upgradesDescription;

    protected int baseCost;

    protected boolean upgradeCost;
    protected boolean upgradeDamage;
    protected boolean upgradeBlock;
    protected boolean upgradeMagic;

    protected int costUpgrade;
    protected int damageUpgrade;
    protected int blockUpgrade;
    protected int magicUpgrade;

    protected boolean baseExhaust;
    protected boolean upgExhaust;
    protected boolean baseInnate;
    protected boolean upgInnate;

    protected boolean upgradeBurst;
    protected boolean upgradeRetain;
    protected boolean upgradeEthereal;
    protected boolean upgradeMultiDmg;

    public int baseMagicNumber2;
    public int magicNumber2;
    public boolean isMagicNumber2Modified;

    public int baseShowNumber;
    public int showNumber;
    public boolean isShowNumberModified;

    public boolean useGrace;
    public boolean switchCardOnPlay;


    public FrontlineCard(CardInfo cardInfo, boolean upgradesDescription) {
        this(FrontlineCharacter.Enums.COLOR_FRONTLINE, cardInfo.cardName, cardInfo.cardCost, cardInfo.cardType, cardInfo.cardTarget, cardInfo.cardRarity, upgradesDescription);
    }

    public FrontlineCard(CardColor color, String cardName, int cost, CardType cardType, CardTarget target, CardRarity rarity, boolean upgradesDescription) {
        super(makeID(cardName), "", (String) null, cost, "", cardType, color, rarity, target);
        CommonKeywordIconsField.useIcons.set(this, true);

        cardStrings = CardCrawlGame.languagePack.getCardStrings(cardID);

        img = TextureLoader.getAndLoadCardTextureString(cardName, cardType);
        this.textureImg = img;
        loadCardImage(textureImg);

        //this.rarity = autoRarity();
        this.rarity = rarity;

        this.rawDescription = cardStrings.DESCRIPTION;
        this.originalName = cardStrings.NAME;
        this.name = originalName;

        this.baseCost = cost;

        this.upgradesDescription = upgradesDescription;

        this.upgradeCost = false;
        this.upgradeDamage = false;
        this.upgradeBlock = false;
        this.upgradeMagic = false;

        this.costUpgrade = cost;
        this.damageUpgrade = 0;
        this.blockUpgrade = 0;
        this.magicUpgrade = 0;

        upgradeBurst = false;
        upgradeRetain = false;
        upgradeEthereal = false;

        useGrace = false;
        switchCardOnPlay = false;

        if(cardName.toLowerCase().contains("strike")) {
            tags.add(CardTags.STRIKE);
        }

        InitializeCard();
    }

    //Methods meant for constructor use
    public void setDamage(int damage) {
        this.setDamage(damage, 0);
    }

    public void setBlock(int block) {
        this.setBlock(block, 0);
    }

    public void setMagic(int magic) {
        this.setMagic(magic, 0);
    }

    public void setCostUpgrade(int costUpgrade) {
        this.costUpgrade = costUpgrade;
        this.upgradeCost = true;
    }

    public void setExhaust(boolean exhaust) {
        this.setExhaust(exhaust, exhaust);
    }

    public void setDamage(int damage, int damageUpgrade) {
        this.baseDamage = this.damage = damage;
        if (damageUpgrade != 0) {
            this.upgradeDamage = true;
            this.damageUpgrade = damageUpgrade;
        }
    }

    public void setBlock(int block, int blockUpgrade) {
        this.baseBlock = this.block = block;
        if (blockUpgrade != 0) {
            this.upgradeBlock = true;
            this.blockUpgrade = blockUpgrade;
        }
    }

    public void setMagic(int magic, int magicUpgrade) {
        this.baseMagicNumber = this.magicNumber = magic;
        if (magicUpgrade != 0) {
            this.upgradeMagic = true;
            this.magicUpgrade = magicUpgrade;
        }
    }

    public void setExhaust(boolean baseExhaust, boolean upgExhaust) {
        this.baseExhaust = baseExhaust;
        this.upgExhaust = upgExhaust;
        this.exhaust = baseExhaust;
    }

    public void setInnate(boolean baseInnate, boolean upgInnate) {
        this.baseInnate = baseInnate;
        this.isInnate = baseInnate;
        this.upgInnate = upgInnate;
    }

    public void setBurst(boolean upgradeToBurst) {
        if(upgradeToBurst) {
            upgradeBurst = true;
        } else {
            tags.add(CardENUMs.BURST);
        }
    }

    public void setRetain(boolean upgradeToRetain) {
        if(upgradeToRetain) {
            upgradeRetain = true;
        } else {
            selfRetain = true;
        }
    }

    public void setEthereal(boolean upgradeToEthereal) {
        if(upgradeToEthereal) {
            upgradeEthereal = true;
        } else {
            isEthereal = true;
        }
    }

    public void setMultiDamage(boolean upgradeMulti) {
        if(upgradeMulti) {
            upgradeMultiDmg = true;
        } else {
            this.isMultiDamage = true;
        }
    }

    public void setSN(int sn) {
        this.showNumber = baseShowNumber = sn;
    }

    public void setMN2(int mn2) {
        this.magicNumber2 = baseMagicNumber2 = mn2;
    }

    private CardRarity autoRarity() {
        String packageName = this.getClass().getPackage().getName();

        String directParent;
        if (packageName.contains(".")) {
            directParent = packageName.substring(1 + packageName.lastIndexOf("."));
        } else {
            directParent = packageName;
        }
        switch (directParent) {
            case "common":
                return CardRarity.COMMON;
            case "uncommon":
                return CardRarity.UNCOMMON;
            case "rare":
                return CardRarity.RARE;
            case "basic":
                return CardRarity.BASIC;
            default:
                if(Settings.isDebug) {
                    TheFrontline.logger.info("Automatic Card rarity resulted in SPECIAL, input: " + directParent);
                }
                return CardRarity.SPECIAL;
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();

        if (card instanceof FrontlineCard) {
            card.rawDescription = this.rawDescription;
            ((FrontlineCard) card).upgradesDescription = this.upgradesDescription;

            ((FrontlineCard) card).baseCost = this.baseCost;

            ((FrontlineCard) card).upgradeCost = this.upgradeCost;
            ((FrontlineCard) card).upgradeDamage = this.upgradeDamage;
            ((FrontlineCard) card).upgradeBlock = this.upgradeBlock;
            ((FrontlineCard) card).upgradeMagic = this.upgradeMagic;

            ((FrontlineCard) card).costUpgrade = this.costUpgrade;
            ((FrontlineCard) card).damageUpgrade = this.damageUpgrade;
            ((FrontlineCard) card).blockUpgrade = this.blockUpgrade;
            ((FrontlineCard) card).magicUpgrade = this.magicUpgrade;

            ((FrontlineCard) card).baseExhaust = this.baseExhaust;
            ((FrontlineCard) card).upgExhaust = this.upgExhaust;
            ((FrontlineCard) card).baseInnate = this.baseInnate;
            ((FrontlineCard) card).upgInnate = this.upgInnate;

            ((FrontlineCard) card).upgradeMultiDmg = this.upgradeMultiDmg;
            ((FrontlineCard) card).upgradeRetain = this.upgradeRetain;
            ((FrontlineCard) card).upgradeEthereal = this.upgradeEthereal;
            ((FrontlineCard) card).upgradeBurst = this.upgradeBurst;

            ((FrontlineCard) card).baseMagicNumber2 = this.baseMagicNumber2;
            ((FrontlineCard) card).magicNumber2 = this.magicNumber2;
            ((FrontlineCard) card).baseShowNumber = this.baseShowNumber;
            ((FrontlineCard) card).showNumber = this.showNumber;

            ((FrontlineCard) card).useGrace = this.useGrace;
            ((FrontlineCard) card).switchCardOnPlay = this.switchCardOnPlay;
        }

        return card;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeName();

            if (this.upgradesDescription)
                this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;

            if (upgradeCost) {
                int diff = this.baseCost - this.cost; //positive if cost is reduced

                this.upgradeBaseCost(costUpgrade);
                this.cost -= diff;
                this.costForTurn -= diff;
                if (cost < 0)
                    cost = 0;

                if (costForTurn < 0)
                    costForTurn = 0;
            }

            if (upgradeDamage)
                this.upgradeDamage(damageUpgrade);

            if (upgradeBlock)
                this.upgradeBlock(blockUpgrade);

            if (upgradeMagic)
                this.upgradeMagicNumber(magicUpgrade);

            if (baseExhaust ^ upgExhaust) //different
                this.exhaust = upgExhaust;

            if (baseInnate ^ upgInnate) //different
                this.isInnate = upgInnate;

            if(upgradeBurst) {
                tags.add(CardENUMs.BURST);
            }

            if(upgradeRetain) {
                selfRetain = true;
            }

            if(upgradeEthereal) {
                isEthereal = true;
            }

            if(upgradeMultiDmg) {
                this.isMultiDamage = true;
            }

            this.initializeDescription();

            if(switchCardOnPlay && cardsToPreview != null && !cardsToPreview.upgraded) {
                cardsToPreview.upgrade();
            }
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if(CardCrawlGame.isInARun()) {
            if ((this.hasTag(CardENUMs.BURST) && UC.anonymousCheckBurst())) {
                glowColor = GOLD_BORDER_GLOW_COLOR;
            } else {
                glowColor = BLUE_BORDER_GLOW_COLOR;
            }
        }
    }

    @Override
    public void renderCardPreview(SpriteBatch sb) {
        boolean tmp = false;
        if(UC.p() != null) {
            tmp = UC.p().isDraggingCard;
            UC.p().isDraggingCard = false;
        }
        super.renderCardPreview(sb);
        if(UC.p() != null) {
            UC.p().isDraggingCard = tmp;
        }
    }

    public void InitializeCard() {
        FontHelper.cardDescFont_N.getData().setScale(1.0f);
        this.initializeTitle();
        this.initializeDescription();
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void resetAttributes() {
        super.resetAttributes();
        this.magicNumber2 = baseMagicNumber2;
        this.isMagicNumber2Modified = false;
        this.showNumber = baseShowNumber;
        this.isShowNumberModified = false;
    }

    @Override
    public void applyPowers() {
        this.applyPowersToMN2();
        this.applyPowersToSN();
        super.applyPowers();
    }

    private void applyPowersToMN2() {
        this.isMagicNumber2Modified = magicNumber2 != baseMagicNumber2;
    }
    private void applyPowersToSN() {
        this.isShowNumberModified = showNumber != baseShowNumber;
    }
}