package de.uni_passau.fim.infosun.prophet;

import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

public class TestLanguageIndependence {
    /*
	 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights
	 * reserved.
	 *
	 * Redistribution and use in source and binary forms, with or without
	 * modification, are permitted provided that the following conditions are
	 * met:
	 *
	 * - Redistributions of source code must retain the above copyright notice,
	 * this list of conditions and the following disclaimer.
	 *
	 * - Redistributions in binary form must reproduce the above copyright
	 * notice, this list of conditions and the following disclaimer in the
	 * documentation and/or other materials provided with the distribution.
	 *
	 * - Neither the name of Oracle or the names of its contributors may be used
	 * to endorse or promote products derived from this software without
	 * specific prior written permission.
	 *
	 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
	 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
	 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
	 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
	 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
	 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
	 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
	 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
	 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
	 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
	 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
	 */

    static void displayValue(Locale currentLocale, String key) {

        ResourceBundle labels = ResourceBundle.getBundle("test.LabelsBundle", currentLocale);
        String value = labels.getString(key);
        System.out.println("Locale = " + currentLocale.toString() + ", " + "key = " + key + ", " + "value = " + value);
    } // displayValue

    static void iterateKeys(Locale currentLocale) {

        ResourceBundle labels = ResourceBundle.getBundle("test.LabelsBundle", currentLocale);

        Enumeration bundleKeys = labels.getKeys();

        while (bundleKeys.hasMoreElements()) {
            String key = (String) bundleKeys.nextElement();
            String value = labels.getString(key);
            System.out.println("key = " + key + ", " + "value = " + value);
        }
    } // iterateKeys

    static public void main(String[] args) {

        Locale[] supportedLocales = {Locale.FRENCH, Locale.GERMAN, Locale.ENGLISH};

        for (Locale supportedLocale : supportedLocales) {
            displayValue(supportedLocale, "s2");
        }

        System.out.println();

        iterateKeys(supportedLocales[0]);
    } // main
} // class

