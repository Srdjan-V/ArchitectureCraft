/*
 * MIT License
 *
 * Copyright (c) 2017 Benjamin K
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tridevmc.architecture.common.helpers;

import com.tridevmc.architecture.legacy.common.shape.LegacyEnumShape;
import com.tridevmc.architecture.legacy.math.LegacyTrans3;
import net.minecraft.core.Direction;

import java.util.HashMap;
import java.util.Map;

public class Profile {

    protected static Map<Object, Object> opposites = new HashMap<>();

    public static Object getProfileGlobal(LegacyEnumShape shape, int side, int turn, Direction globalFace) {
        Direction localFace = LegacyTrans3.sideTurnRotations[side][turn].it(globalFace);
        return shape.behaviour.profileForLocalFace(shape, localFace);
    }

    public static boolean matches(Object profile1, Object profile2) {
        Object opposite1 = opposites.get(profile1);
        if (opposite1 != null)
            return opposite1 == profile2;
        else
            return profile1 == profile2;
    }

    public static void declareOpposite(Object profile1, Object profile2) {
        opposites.put(profile1, profile2);
        opposites.put(profile2, profile1);
    }

    public enum Generic {
        End, LeftEnd, RightEnd, OffsetBottom, OffsetTop;

        public static final Generic[] eeStraight = {null, null, null, null, End, End};
        public static final Generic[] lrStraight = {null, null, null, null, RightEnd, LeftEnd};
        public static final Generic[] eeCorner = {null, null, null, End, End, null};
        public static final Generic[] lrCorner = {null, null, null, LeftEnd, RightEnd, null};
        public static final Generic[] rlCorner = {null, null, RightEnd, null, null, LeftEnd};
        public static final Generic[] tOffset = {null, OffsetTop, null, null, null, null};
        public static final Generic[] bOffset = {OffsetBottom, null, null, null, null, null};
        public static final Generic[] tbOffset = {OffsetBottom, OffsetTop, null, null, null, null};

        static {
            declareOpposite(LeftEnd, RightEnd);
            declareOpposite(OffsetBottom, OffsetTop);
        }

    }

}
