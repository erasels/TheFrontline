package theFrontline.characters.characterInfo.frontline.RF;

import theFrontline.util.UC;

public class G43 extends RFInfo {
    public static final String ID = G43.class.getSimpleName();
    public static final int MAX_HP = 5;
    private static final int CRD_DRAW = 2;

    public G43() {
        super(ID, MAX_HP);
        magicNumber = CRD_DRAW;
    }

    @Override
    public void onDeploy() {
        if(UC.pc().getPrevChar().isGFL(Type.HG)) {
            UC.doDraw(getMN());
        }
    }

    @Override
    public void setFlavorStats() {
        offence = 2;
        defence = 2;
        utility = 2;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.BASIC;
    }

    @Override
    public String getDescription() {
        return characterStrings.TEXT[0] + getMN() + characterStrings.TEXT[1];
    }
}
