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

package de.kaiserpfalzedv.rpg.core.snowflake;

import de.kaiserpfalzedv.rpg.core.snowflake.util.BinHexUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author downgoon {@literal http://downgoon.com}
 * @since 1.0.0 2021-01-11
 */
public class SnowflakeDemo2 {

	/*
	 * 快速生成1千个ID，基本在1毫秒内就能
	 * */
	public static void main(String[] args) {
		Snowflake snowflake = new Snowflake(2, 5);
		final int idAmout = 1000;
		List<Long> idPool = new ArrayList<Long>(idAmout);
		for (int i = 0; i < idAmout; i++) {
			long id = snowflake.nextId();
			idPool.add(id);
		}

		for (Long id : idPool) {
			System.out.println(String.format("%s => id: %d, hex: %s, bin: %s", snowflake.formatId(id), id,
					BinHexUtil.hex(id), BinHexUtil.bin(id)));
		}

	}

}
