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

package org.openjavacard.gp.scp;

import javax.smartcardio.CardException;

/**
 * SCP security policies
 *
 * These describe what part of card exchanges has to be
 * authenticated and/or encrypted.
 *
 * All existing protocols require CMAC to be enabled.
 *
 * All existing protocols have a linear tower of
 * options in order of increasing security as follows:
 *  - CMAC (minimum supported level)
 *  - CENC (implies CMAC)
 *  - RMAC (implies CMAC, CENC)
 *  - RENC (implies CMAC, CENC, RMAC)
 */
public enum SCPSecurityPolicy {
    /** Minimum supported level */
    CMAC(false, false, false),
    /** Implies CMAC */
    CENC(true, false, false),
    /** Implies CMAC, CENC */
    RMAC(true, true, false),
    /** Implies CMAC, CENC, RMAC */
    RENC(true, true, true);

    /** True if CMAC is required */
    public final boolean requireCMAC;
    /** True if CENC is required */
    public final boolean requireCENC;
    /** True if RMAC is required */
    public final boolean requireRMAC;
    /** True if RENC is required */
    public final boolean requireRENC;

    /** Internal constructor */
    SCPSecurityPolicy(boolean reqCENC, boolean reqRMAC, boolean reqRENC) {
        requireCMAC = true;
        requireCENC = reqCENC;
        requireRMAC = reqRMAC;
        requireRENC = reqRENC;
    }

    /**
     * Verify that the given protocol complies with this policy
     * @param protocol to check
     * @throws CardException if the protocol does not comply
     */
    public void checkProtocol(SCPProtocol protocol) throws CardException {
        if(!protocol.isSecuritySupported(this)) {
            throw new CardException("Security protocol " + protocol + " is insufficient for this security policy");
        }
    }

}
