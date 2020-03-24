package theFrontline.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import theFrontline.TheFrontline;
import theFrontline.characters.characterInfo.frontline.FrontlineInfo;
import theFrontline.util.CharacterHelper;

import java.util.HashMap;
import java.util.Map;

public class CharListCommand extends ConsoleCommand {
    public CharListCommand() {
        maxExtraTokens = 0;
        minExtraTokens = 0;
    }

    @Override
    protected void execute(String[] tokens, int depth) {
        TheFrontline.logger.info("Dumping character IDs.");
        for(Map.Entry<FrontlineInfo.Type, HashMap<String, CharacterHelper.FlInstanceInfo>> charMap : CharacterHelper.frontlineMap.entrySet()) {
            for(Map.Entry<String, CharacterHelper.FlInstanceInfo> s : charMap.getValue().entrySet()) {
                TheFrontline.logger.info(charMap.getKey() + ": " + s.getKey() + " | " + s.getValue().rarity.toString());
            }
        }
        DevConsole.log("Dumping IDs in ModTheSpire window.");
    }
} 