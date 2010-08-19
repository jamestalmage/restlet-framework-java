/**
 * Copyright 2005-2010 Noelios Technologies.
 * 
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: LGPL 3.0 or LGPL 2.1 or CDDL 1.0 or EPL 1.0 (the
 * "Licenses"). You can select the license that you prefer but you may not use
 * this file except in compliance with one of these Licenses.
 * 
 * You can obtain a copy of the LGPL 3.0 license at
 * http://www.opensource.org/licenses/lgpl-3.0.html
 * 
 * You can obtain a copy of the LGPL 2.1 license at
 * http://www.opensource.org/licenses/lgpl-2.1.php
 * 
 * You can obtain a copy of the CDDL 1.0 license at
 * http://www.opensource.org/licenses/cddl1.php
 * 
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0.php
 * 
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 * 
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly at
 * http://www.noelios.com/products/restlet-engine
 * 
 * Restlet is a registered trademark of Noelios Technologies.
 */

package org.restlet.engine.nio;

import java.io.IOException;

import org.restlet.Response;

/**
 * Server-side outbound way.
 * 
 * @author Jerome Louvel
 */
public class ServerOutboundWay extends OutboundWay {

    /**
     * Constructor.
     * 
     * @param connection
     *            The parent connection.
     */
    public ServerOutboundWay(Connection<?> connection) {
        super(connection);
    }

    @Override
    protected void onCompleted(Response message) {
        super.onCompleted(message);
        getMessages().remove(message);

        if (!message.getStatus().isInformational()) {
            // Attempt to read additional inbound messages
            getConnection().getInboundWay().getMessages().remove(message);
        }

        if (!getConnection().isPersistent()) {
            getConnection().close(true);
        }
    }

    @Override
    protected void writeStartLine() throws IOException {
        getLineBuilder().append(getVersion(getMessage().getRequest()));
        getLineBuilder().append(' ');
        getLineBuilder().append(getMessage().getStatus().getCode());
        getLineBuilder().append(' ');

        if (getMessage().getStatus().getName() != null) {
            getLineBuilder().append(getMessage().getStatus().getName());
        } else {
            getLineBuilder().append(
                    "Status " + getMessage().getStatus().getCode());
        }

        getLineBuilder().append("\r\n");
    }

}
