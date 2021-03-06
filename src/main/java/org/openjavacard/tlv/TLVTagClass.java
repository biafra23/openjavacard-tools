/*
 *  openjavacard-tools: OpenJavaCard development tools
 *  Copyright (C) 2018  Ingo Albrecht (prom@berlin.ccc.de)
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software Foundation,
 *  Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 *
 */

package org.openjavacard.tlv;

import java.util.HashMap;

public enum  TLVTagClass {
    UNIVERSAL(TLVTag.TAG_CLASS_UNIVERSAL),
    APPLICATION(TLVTag.TAG_CLASS_APPLICATION),
    CONTEXT(TLVTag.TAG_CLASS_CONTEXT),
    PRIVATE(TLVTag.TAG_CLASS_PRIVATE);

    private static final HashMap<Integer,TLVTagClass> CLASSES
            = buildClasses();

    final int value;

    TLVTagClass(int value) {
        this.value = value;
    }

    public static TLVTagClass forValue(int classValue) {
        return CLASSES.get(classValue);
    }

    private static HashMap<Integer,TLVTagClass> buildClasses() {
        HashMap<Integer, TLVTagClass> res = new HashMap<>();
        for(TLVTagClass tc: TLVTagClass.values()) {
            res.put(tc.value, tc);
        }
        return res;
    }

}
