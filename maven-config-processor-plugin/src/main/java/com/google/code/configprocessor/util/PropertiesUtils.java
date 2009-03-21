/*
 * Copyright (C) 2009 Leandro de Oliveira Aparecido <lehphyro@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.code.configprocessor.util;

import static com.google.code.configprocessor.util.IOUtils.*;

import java.io.*;
import java.util.*;

import com.google.code.configprocessor.*;
import com.google.code.configprocessor.log.*;

public abstract class PropertiesUtils {

	/**
	 * Read additional properties file if specified.
	 * 
	 * @param input
	 * @param logAdapter
	 * 
	 * @return Properties read or empty properties if not specified.
	 * @throws ConfigProcessException If processing cannot be performed.
	 */
	public static final Properties loadIfPossible(File input, LogAdapter logAdapter) throws ConfigProcessException {
		Properties additional = new Properties();
		if (input == null) {
			return additional;
		}

		if (!input.exists()) {
			throw new ConfigProcessException("Additional properties file [" + input + "] does not exist");
		}

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(input);
			additional.load(fis);
			return additional;
		} catch (Exception e) {
			throw new ConfigProcessException("Error loading additional properties", e);
		} finally {
			close(fis, logAdapter);
		}
	}
}
