package theFrontline.util;

import theFrontline.TheFrontline;
import theFrontline.characters.FrontlineCharacter;

public class ScrapHelper {
    public static int getScrap() {
        FrontlineCharacter p = UC.pc();
        if(p != null) {
            return p.scrap;
        }
        return -1;
    }

    public static void addScrap(int i) {
        FrontlineCharacter p = UC.pc();
        if(p != null) {
            TheFrontline.scrapDisplay.flash();
            p.scrap += i;
        }
    }

    public static void loseScrap(int i) {
        addScrap(-i);
    }
}
