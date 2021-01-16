/*
 * Copyright 2021 downgoon, http://downgoon.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.kaiserpfalzedv.rpg.core.snowflake.util;

/**
 * @author downgoon {@literal http://downgoon.com}
 * @since 1.0.0 2021-01-11
 */
public class BinHexUtil {

    public static String bin(long d) {
        return leftZeroPadding64(Long.toBinaryString(d));
    }

    public static String hex(long d) {
        return leftZeroPadding16(Long.toHexString(d).toUpperCase());
    }

    /**
     * a diode is a long value whose left and right margin are ZERO, while
     * middle bits are ONE in binary string layout. it looks like a diode in
     * shape.
     *
     * @param offset
     *            left margin position
     * @param length
     *            offset+length is right margin position
     * @return a long value
     */
    public static long diode(int offset, int length) {
        if (offset < 0 || length < 0 || (offset + length) > 64) {
            throw new IllegalArgumentException("bits ranges: [0, 64)");
        }
        if (length == 0) {
            return 0L;
        }
        if (length == 64) {
            return -1L;
        }
        int lb = 64 - offset;
        int rb = 64 - (offset + length);
        return (-1L << lb) ^ (-1L << rb);
    }

    private static final String ZERO_PADDING_64 = "0000000000000000000000000000000000000000000000000000000000000000";

    private static String leftZeroPadding64(String text) {
        return ZERO_PADDING_64.substring(text.length()) + text;
    }

    private static final String ZERO_PADDING_16 = "0000000000000000";

    private static String leftZeroPadding16(String text) {
        return ZERO_PADDING_16.substring(text.length()) + text;
    }
}
