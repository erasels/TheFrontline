package theFrontline.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import org.apache.commons.lang3.math.NumberUtils;
import theFrontline.characters.FrontlineCharacter;
import theFrontline.util.CharacterHelper;
import theFrontline.util.UC;

import java.util.ArrayList;

public class SetCharCommand extends ConsoleCommand {
    public SetCharCommand() {
        this.requiresPlayer = true;
        maxExtraTokens = 2;
        minExtraTokens = 2;
    }

    @Override
    protected void execute(String[] tokens, int depth) {
        int i;
        try {
            i = Integer.parseInt(tokens[1]);
            if(i >= FrontlineCharacter.MAX_CHARACTERS || i < 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            DevConsole.log("Invalid character slot");
            return;
        }

        FrontlineCharacter p = UC.pc();
        if(p != null) {
            if(i > p.characters.size() - 1) {
                i = NumberUtils.min(p.characters.size(), FrontlineCharacter.MAX_CHARACTERS - 1);
            }

            if(i == p.characters.size() -1) {
                p.characters.remove(p.characters.get(i));
            }
            CharacterHelper.addCharacter(CharacterHelper.retrieveCharacter(tokens[2]));
        }
    }

    @Override
    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();

        int slots = FrontlineCharacter.MAX_CHARACTERS;
        for (int i = 0; i < slots; i++) {
            result.add(String.valueOf(i));
        }

        if(result.contains(tokens[depth]) && tokens.length > depth + 1) {
            result.clear();
            result.addAll(CharacterHelper.characterIDs);

            if(result.contains(tokens[depth + 1])) {
                complete = true;
            }
        }

        return result;
    }
}