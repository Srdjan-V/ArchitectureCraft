package com.tridevmc.architecture.common.shape.behaviour;

import com.tridevmc.architecture.legacy.common.block.entity.LegacyShapeBlockEntity;
import com.tridevmc.architecture.legacy.math.LegacyTrans3;
import com.tridevmc.architecture.legacy.math.LegacyVector3;
import com.tridevmc.architecture.common.utils.MiscUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;

import static net.minecraft.core.Direction.*;

public class ShapeBehaviourPlainWindow extends ShapeBehaviourWindow {
    public ShapeBehaviourPlainWindow() {
        this.frameSides = new Direction[]{DOWN, EAST, UP, WEST};
        this.frameAlways = new boolean[]{false, false, false, false};
        this.frameTypes = new ShapeBehaviourWindow.FrameType[]{ShapeBehaviourWindow.FrameType.PLAIN, ShapeBehaviourWindow.FrameType.PLAIN, ShapeBehaviourWindow.FrameType.NONE, FrameType.NONE, ShapeBehaviourWindow.FrameType.PLAIN, ShapeBehaviourWindow.FrameType.PLAIN};
        this.frameOrientations = new Direction[]{EAST, EAST, null, null, UP, UP};
        this.frameTrans = new LegacyTrans3[]{
                LegacyTrans3.ident,
                LegacyTrans3.ident.rotZ(90),
                LegacyTrans3.ident.rotZ(180),
                LegacyTrans3.ident.rotZ(270),
        };
    }

    @Override
    public boolean orientOnPlacement(Player player, LegacyShapeBlockEntity te, LegacyShapeBlockEntity nte, Direction face,
                                     LegacyVector3 hit) {
        if (nte != null && !player.isCrouching()) {
            if (nte.getArchitectureShape().behaviour instanceof ShapeBehaviourPlainWindow) {
                te.setSide(nte.getSide());
                te.setTurn(nte.getTurn());
                return true;
            }
            if (nte.getArchitectureShape().behaviour instanceof ShapeBehaviourCornerWindow) {
                Direction nlf = nte.localFace(face);
                ShapeBehaviourWindow.FrameType nfk = ((ShapeBehaviourWindow) nte.getArchitectureShape().behaviour).frameTypeForLocalSide(nlf);
                if (nfk == FrameType.PLAIN) {
                    Direction lf = face.getOpposite();
                    te.setSide(nte.getSide());
                    switch (nlf) {
                        case SOUTH -> {
                            te.setTurn(MiscUtils.turnToFace(WEST, lf));
                            return true;
                        }
                        case WEST -> {
                            te.setTurn(MiscUtils.turnToFace(EAST, lf));
                            return true;
                        }
                    }
                }
            }
        }
        return super.orientOnPlacement(player, te, nte, face, hit);
    }
}
