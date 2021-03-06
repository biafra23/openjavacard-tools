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

package org.openjavacard.tool.command;

import org.openjavacard.gp.GPCard;
import org.openjavacard.gp.GPContext;
import org.openjavacard.gp.protocol.GPCardData;
import org.openjavacard.gp.protocol.GPKeyInfo;
import org.openjavacard.gp.protocol.GPLifeCycle;
import org.openjavacard.gp.scp.SCPProtocol;
import org.openjavacard.util.ATRUtil;
import org.openjavacard.util.HexUtil;
import com.beust.jcommander.Parameters;

import javax.smartcardio.Card;
import javax.smartcardio.CardTerminal;
import java.io.PrintStream;

@Parameters(
        commandNames = "gp-info",
        commandDescription = "GlobalPlatform: show information about card"
)
public class GPInfo extends GPCommand {

    public GPInfo(GPContext context) {
        super(context);
    }

    @Override
    protected void performOperation(GPContext context, GPCard card) {
        PrintStream os = System.out;

        os.println();

        CardTerminal terminal = card.getTerminal();
        os.println("Host terminal information:");
        os.println("  Name \"" + terminal.getName() + "\"");
        os.println("  Class " + terminal.getClass().getName());
        os.println();

        Card scard = card.getCard();
        os.println("Host card information:");
        os.println("  Protocol " + scard.getProtocol());
        os.println("  ATR " + ATRUtil.toString(scard.getATR()));
        os.println("  Class " + scard.getClass().getName());
        os.println();

        os.println("Host GP information:");
        String identifier = card.getLifetimeIdentifier();
        if(identifier != null) {
            os.println("  LID " + identifier);
        }
        os.println("  ISD " + card.getISD());
        byte[] iin = card.getCardIIN();
        if(iin != null) {
            os.println("  IIN " + HexUtil.bytesToHex(iin));
        }
        byte[] cin = card.getCardCIN();
        if(cin != null) {
            os.println("  CIN " + HexUtil.bytesToHex(cin));
        }
        os.println();

        GPLifeCycle lifeCycle = card.getCardLifeCycle();
        if (lifeCycle == null) {
            os.println("Card did not provide life cycle data");
        } else {
            os.println(lifeCycle.toString());
        }
        os.println();

        GPCardData cardData = card.getCardData();
        if (cardData == null) {
            os.println("Card did not provide card data");
        } else {
            os.println(cardData.toString());
        }
        os.println();

        SCPProtocol scpProtocol = card.getProtocol();
        if (scpProtocol == null) {
            os.println("Could not determine SCP protocol");
        } else {
            os.println(scpProtocol.toVerboseString());
        }
        os.println();

        GPKeyInfo keyInfo = card.getCardKeyInfo();
        if (keyInfo == null) {
            os.println("Card did not provide key info template");
        } else {
            os.println(keyInfo.toString());
        }
        os.println();

        GPList.printRegistry(os, card, true, true, true);
    }

}
