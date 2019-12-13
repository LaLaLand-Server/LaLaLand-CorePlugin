package de.lalaland.core.utils.common;

import java.util.UUID;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 25.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class UtilText {

  public static String unicodeEscaped(final char ch) {
    if (ch < 0x10) {
      return "\\u000" + Integer.toHexString(ch);
    } else if (ch < 0x100) {
      return "\\u00" + Integer.toHexString(ch);
    } else if (ch < 0x1000) {
      return "\\u0" + Integer.toHexString(ch);
    }
    return "\\u" + Integer.toHexString(ch);
  }

  public static UUID uuidFromShortString(final String idWithoutDashes) {
    return UUID.fromString(
        idWithoutDashes.replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"));
  }

}
