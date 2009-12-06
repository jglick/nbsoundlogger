/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2009 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 * Contributor(s):
 *
 * Portions Copyrighted 2009 Sun Microsystems, Inc.
 */

package org.netbeans.modules.soundlogger;

import java.io.File;
import java.util.regex.Pattern;
import org.apache.tools.ant.module.spi.AntEvent;
import static org.apache.tools.ant.module.spi.AntEvent.*;
import org.apache.tools.ant.module.spi.AntLogger;
import org.apache.tools.ant.module.spi.AntSession;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service=AntLogger.class, position=99)
public class AntSoundLogger extends AntLogger {

    public @Override boolean interestedInSession(AntSession session) {
        return true;
    }

    public @Override boolean interestedInAllScripts(AntSession session) {
        return true;
    }

    public @Override boolean interestedInScript(File script, AntSession session) {
        return true;
    }

    public @Override int[] interestedInLogLevels(AntSession session) {
        return new int[] {LOG_WARN, LOG_ERR};
    }

    public @Override String[] interestedInTargets(AntSession session) {
        return ALL_TARGETS;
    }

    public @Override String[] interestedInTasks(AntSession session) {
        return ALL_TASKS;
    }

    public @Override void buildFinished(AntEvent event) {
        event.getSession().putCustomData(this, true);
        if (event.getException() != null) {
            Sound.BUILD_FAILED.play();
        } else {
            Sound.BUILD_SUCCEEDED.play();
        }
    }

    public @Override void targetStarted(AntEvent event) {
        String target = event.getTargetName();
        if (target == null || target.startsWith("-")) {
            return;
        }
        Sound.TARGET_STARTED.play();
    }

    private static final Pattern ERROR = Pattern.compile(":[0-9]+: ");
    public @Override void messageLogged(AntEvent event) {
        if (event.isConsumed()) {
            return;
        }
        if (Boolean.TRUE.equals(event.getSession().getCustomData(this))) {
            return;
        }
        String msg = event.getMessage();
        if (msg.startsWith("Trying to override old definition of ")) {
            // ignored by StandardLogger
        } else if (msg.contains("warning: ")) {
            Sound.WARNING.play();
        } else if (ERROR.matcher(msg).find()) {
            Sound.ERROR.play();
        } else {
            // some other line of output as part of a multiline warning, perhaps?
        }
    }

}
