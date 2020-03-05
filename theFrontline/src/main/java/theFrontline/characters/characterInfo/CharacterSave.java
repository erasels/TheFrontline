package theFrontline.characters.characterInfo;

import com.megacrit.cardcrawl.cards.CardSave;

import java.util.ArrayList;

public class CharacterSave {
    public String id;
    public String stats;
    public int currentHP;
    public int maxHP;
    public ArrayList<CardSave> masterDeck;

    public CharacterSave(String id, String stats, int currentHP, int maxHP, ArrayList<CardSave> masterDeck) {
        this.id = id;
        this.currentHP = currentHP;
        this.maxHP = maxHP;
        this.stats = stats;
        this.masterDeck = masterDeck;
    }
}
