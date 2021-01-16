/*
 * Copyright (c) 2021 Kaiserpfalz EDV-Service, Roland T. Lichti.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.kaiserpfalzedv.rpg.core.snowflake.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BinHexUtilTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testBin() {
		System.out.println(BinHexUtil.bin(-1L));
		System.out.println(BinHexUtil.bin(-1L << 12));
		System.out.println(BinHexUtil.bin(-1L ^ (-1L << 12)));

		Assert.assertEquals("1111111111111111111111111111111111111111111111111111111111111111", BinHexUtil.bin(-1L));
		Assert.assertEquals("1111111111111111111111111111111111111111111111111111000000000000",
				BinHexUtil.bin(-1L << 12));
		Assert.assertEquals("0000000000000000000000000000000000000000000000000000111111111111",
				BinHexUtil.bin(-1L ^ (-1L << 12)));
	}

	@Test
	public void testHex() {
		System.out.println(BinHexUtil.hex(-1L));
		System.out.println(BinHexUtil.hex(-1L << 12));
		System.out.println(BinHexUtil.hex(-1L ^ (-1L << 12)));

		Assert.assertEquals("FFFFFFFFFFFFFFFF", BinHexUtil.hex(-1L));
		Assert.assertEquals("FFFFFFFFFFFFF000", BinHexUtil.hex(-1L << 12));
		Assert.assertEquals("0000000000000FFF", BinHexUtil.hex(-1L ^ (-1L << 12)));
	}

	@Test
	public void testDiode() {
		System.out.println("diode: " + BinHexUtil.bin(BinHexUtil.diode(1, 41)));
		System.out.println("diode: " + BinHexUtil.bin(BinHexUtil.diode(1+41, 5)));
		System.out.println("diode: " + BinHexUtil.bin(BinHexUtil.diode(1+41+5, 5)));
		System.out.println("diode: " + BinHexUtil.bin(BinHexUtil.diode(1+41+5+5, 12)));
		
		
		Assert.assertEquals("0111111111111111111111111111111111111111110000000000000000000000",
				BinHexUtil.bin(BinHexUtil.diode(1, 41)));
		
		Assert.assertEquals("0000000000000000000000000000000000000000001111100000000000000000",
				BinHexUtil.bin(BinHexUtil.diode(1+41, 5)));
		
		Assert.assertEquals("0000000000000000000000000000000000000000000000011111000000000000",
				BinHexUtil.bin(BinHexUtil.diode(1+41+5, 5)));
		
		Assert.assertEquals("0000000000000000000000000000000000000000000000000000111111111111",
				BinHexUtil.bin(BinHexUtil.diode(1+41+5+5, 12)));
		
		System.out.println("diode: " + BinHexUtil.bin(BinHexUtil.diode(0, 0)));
		System.out.println("diode: " + BinHexUtil.bin(BinHexUtil.diode(0, 64)));
		
		Assert.assertEquals("0000000000000000000000000000000000000000000000000000000000000000",
				BinHexUtil.bin(BinHexUtil.diode(0,0)));
		
		Assert.assertEquals("1111111111111111111111111111111111111111111111111111111111111111",
				BinHexUtil.bin(BinHexUtil.diode(0,64)));
		
	}
}
