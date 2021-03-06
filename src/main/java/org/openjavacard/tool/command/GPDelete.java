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
import org.openjavacard.gp.GPIssuerDomain;
import org.openjavacard.gp.GPRegistry;
import org.openjavacard.iso.AID;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import javax.smartcardio.CardException;
import java.io.PrintStream;
import java.util.List;

@Parameters(
        commandNames = "gp-delete",
        commandDescription = "GlobalPlatform: delete applets or packages from the card"
)
public class GPDelete extends GPCommand {

    @Parameter(
            names = "--related",
            description = "Delete dependent modules"
    )
    private boolean related = false;

    @Parameter(
            names = "--present",
            description = "Fail when objects don't exist"
    )
    private boolean present = false;

    @Parameter(
            description = "AIDs of objects to delete",
            required = true
    )
    private List<AID> objectAIDs = null;

    public GPDelete(GPContext context) {
        super(context);
    }

    @Override
    protected void performOperation(GPContext context, GPCard card) throws CardException {
        PrintStream os = System.out;
        // check presence
        GPRegistry reg = card.getRegistry();
        for (AID aid : objectAIDs) {
            GPRegistry.Entry entry = reg.findAppletOrPackage(aid);
            if(entry != null) {
                os.println("Object " + aid + ": " + entry);
            } else {
                if(present) {
                    throw new CardException("Object " + aid + " is not present on the card");
                } else {
                    os.println("Object " + aid + " is not present");
                    continue;
                }
            }
        }
        // perform deletions
        GPIssuerDomain isd = card.getIssuerDomain();
        for (AID aid : objectAIDs) {
            os.println("Deleting object " + aid + (related?" and related":""));
            isd.deleteObject(aid, related);
        }
    }

}
