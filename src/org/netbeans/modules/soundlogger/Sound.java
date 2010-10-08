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

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public enum Sound {

    TARGET_STARTED("21693__ice9ine__light_switch.wav"),
    WARNING("28927__junggle__btn117.wav"),
    ERROR("32987__HardPCM__Alarm003.wav"),
    BUILD_SUCCEEDED("72126__KIZILSUNGUR__SweetAlertSound2.wav"),
    BUILD_FAILED("11868__medialint__box_of_beer_bottles_being_kicked.wav");

    private final Clip clip;
    private Sound(String file) {
        try {
            URL u = Sound.class.getResource(file);
            assert u != null : file;
            AudioInputStream is = AudioSystem.getAudioInputStream(u);
            clip = AudioSystem.getClip();
            clip.open(is);
        } catch (Exception x) {
            throw new ExceptionInInitializerError(x);
        }
    }

    public void play() {
        // XXX consider tips in: jar:https://issues.apache.org/bugzilla/attachment.cgi?id=24902!/PlaySounds.java
        clip.start();
    }

}
