package theFrontline.util;

import theFrontline.characters.characterInfo.AbstractCharacterInfo;

public class CharacterHelper {
    public static AbstractCharacterInfo getCharacterFromID(String id) {
        AbstractCharacterInfo ci = null;
        try {
            Class<?> clazz = Class.forName(id);
            ci = (AbstractCharacterInfo) clazz.newInstance();
        } catch (Exception e) {
            System.out.println("Failed to load character from Frontline: " + e.getStackTrace());
        }

        return ci;
    }
}
