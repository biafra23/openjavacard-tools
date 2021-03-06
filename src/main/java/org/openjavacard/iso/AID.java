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

package org.openjavacard.iso;

import org.openjavacard.util.HexUtil;
import org.openjavacard.util.VerboseString;

import java.util.Arrays;

/**
 * Representation for ISO7816 AIDs and RIDs
 */
public class AID implements VerboseString {

    /** Offset of a RID in an AID (0) */
    public static final int RID_OFFSET =  0;
    /** Length of a RID in an AID (5) */
    public static final int RID_LENGTH =  5;

    /** Offset of a PIX in an AID (5) */
    public static final int PIX_OFFSET =  5;
    /** Maximum length of a PIX in an AID (11) */
    public static final int PIX_LENGTH = 11;

    /** Binary value of the AID */
    private final byte[] mBytes;

    /**
     * Construct an AID from a byte array
     * @param bytes indicating the AID
     */
    public AID(byte[] bytes) {
        if(bytes.length < RID_LENGTH) {
            throw new IllegalArgumentException("AID must be 5 bytes long");
        }
        mBytes = bytes.clone();
    }

    /**
     * Construct an AID from part of a byte array
     * @param bytes containing the AID
     * @param offset of the AID
     * @param length of the AID
     */
    public AID(byte[] bytes, int offset, int length) {
        if(length < RID_LENGTH) {
            throw new IllegalArgumentException("AID must be 5 bytes long");
        }
        mBytes = Arrays.copyOfRange(bytes, offset, offset + length);
    }

    /**
     * Construct an AID from a hex string
     * @param hex string indicating the AID
     */
    public AID(String hex) {
        mBytes = HexUtil.hexToBytes(hex);
    }

    /** @return length of the AID */
    public int getLength() {
        return mBytes.length;
    }

    /** @return binary representation of the AID */
    public byte[] getBytes() {
        return mBytes.clone();
    }

    /** @return the RID part of the AID as an object */
    public AID getRID() {
        return new AID(getRIDBytes());
    }

    public int getRIDLength() {
        return RID_LENGTH;
    }

    /** @return the RID part of the AID as a byte array */
    public byte[] getRIDBytes() {
        byte[] res = new byte[RID_LENGTH];
        System.arraycopy(mBytes, RID_OFFSET, res, 0, RID_LENGTH);
        return res;
    }

    /** @return the length of the PIX part of this AID */
    public int getPIXLength() {
        return mBytes.length - RID_LENGTH;
    }

    /** @return bytes comprising the PIX part of this AID */
    public byte[] getPIXBytes() {
        int len = getPIXLength();
        byte[] res = new byte[PIX_LENGTH];
        System.arraycopy(mBytes, PIX_OFFSET, res, 0, len);
        return res;
    }

    /** @return the RID part of the AID as a hex string */
    public String getRIDString() {
        return HexUtil.bytesToHex(getRIDBytes());
    }

    /** Stringify the AID */
    public String toString() {
        return HexUtil.bytesToHex(mBytes);
    }

    /** Equality by value */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AID aid = (AID) o;
        return Arrays.equals(mBytes, aid.mBytes);
    }

    /** Hash of value */
    @Override
    public int hashCode() {
        return Arrays.hashCode(mBytes);
    }

    /**
     * Construct an AID from a string-serialized byte array
     *
     * This format is used in CAP manifests.
     *
     * @param string indicating the AID
     * @return an appropriate instance
     */
    public static AID fromArrayString(String string) {
        String hex = string.replace("0x", "").replace(":", "");
        return new AID(hex);
    }

    /** Stringify the AID verbosely */
    @Override
    public String toVerboseString() {
        byte[] rid = getRIDBytes();
        byte[] pix = getPIXBytes();
        String res = "";
        res += HexUtil.bytesToHex(rid);
        if(pix.length > 0) {
            res += HexUtil.bytesToHex(pix);
        }
        return res;
    }

}
