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

package org.openjavacard.gp.protocol;

import org.openjavacard.gp.keys.GPKey;
import org.openjavacard.gp.keys.GPKeyCipher;

/**
 * GlobalPlatform Key Information
 * <p/>
 * This is the member of a Key Information Template.
 * <p/>
 * Each of these describes a single key with given
 * version and ID. Size and type are also specified.
 * <p/>
 */
public class GPKeyInfoEntry {

    private int mKeyId;
    private int mKeyVersion;
    private int[] mKeyTypes;
    private int[] mKeySizes;

    public GPKeyInfoEntry() {
    }

    public int getKeyId() {
        return mKeyId;
    }

    public int getKeyVersion() {
        return mKeyVersion;
    }

    public int[] getKeyTypes() {
        return mKeyTypes;
    }

    public int[] getKeySizes() {
        return mKeySizes;
    }

    public boolean matchesKey(GPKey key) {
        if (mKeyTypes.length != 1) {
            throw new UnsupportedOperationException("Keys with multiple components are not supported");
        }

        // check the key id
        int keyId = key.getId();
        if (keyId != 0 && keyId != mKeyId) {
            return false;
        }

        // check the cipher
        byte keyType = (byte) (mKeyTypes[0] & 0xFF);
        GPKeyCipher kiCipher = GPKeyCipher.getCipherForKeyType(keyType);
        GPKeyCipher keyCipher = key.getCipher();
        // we allow the card to say DES when it means 3DES
        if (kiCipher == GPKeyCipher.DES && keyCipher == GPKeyCipher.DES3) {
        } else if (kiCipher != keyCipher) {
            return false;
        }

        // check the size of the secret
        if (mKeySizes[0] != key.getLength()) {
            return false;
        }

        return true;
    }

    public void read(byte[] buf) {
        read(buf, 0, buf.length);
    }

    public void read(byte[] buf, int off, int length) {
        if (length < 4) {
            throw new IllegalArgumentException("Invalid key info - too short");
        }
        mKeyId = buf[off++] & 0xFF;
        mKeyVersion = buf[off++] & 0xFF;
        int numKeys = (length - 2) / 2;
        if (numKeys > 1) {
            throw new UnsupportedOperationException("Keys with multiple components are not supported");
        }
        mKeyTypes = new int[numKeys];
        mKeySizes = new int[numKeys];
        for (int i = 0; i < numKeys; i++) {
            mKeyTypes[i] = buf[off++] & 0xFF;
            mKeySizes[i] = buf[off++] & 0xFF;
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Key id ");
        sb.append(mKeyId);
        sb.append(" version ");
        sb.append(mKeyVersion);
        int numKeys = mKeyTypes.length;
        if (numKeys > 0) {
            sb.append(" (");
        }
        for (int i = 0; i < numKeys; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append("type ");
            sb.append(GP.keyTypeString((byte) (mKeyTypes[i] & 0xFF)));
            sb.append(" size ");
            sb.append(mKeySizes[i]);
        }
        if (numKeys > 0) {
            sb.append(")");
        }
        return sb.toString();
    }

}
