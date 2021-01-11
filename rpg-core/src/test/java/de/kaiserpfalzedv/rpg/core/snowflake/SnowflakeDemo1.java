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

/**
 * @author downgoon {@literal http://downgoon.com}
 * @since 1.0.0 2021-01-11
 */
public class SnowflakeDemo1 {

	/*
	 * 由于``System.out.println``IO操作比较耗时，导致前后两次生成ID的时间间隔偶尔会超过1毫秒。
	 * 超过1毫秒的，序号字段会是``#0``；没超过1毫秒的，序号字段会累加。
	 */

	public static void main(String[] args) {
		Snowflake snowflake = new Snowflake(2, 5);
		for (int i = 0; i < 20; i++) {
			long id = snowflake.nextId();
			System.out.println(String.format("%s => id: %d, hex: %s, bin: %s", snowflake.formatId(id), id,
					BinHexUtil.hex(id), BinHexUtil.bin(id)));
		}
	}

}
