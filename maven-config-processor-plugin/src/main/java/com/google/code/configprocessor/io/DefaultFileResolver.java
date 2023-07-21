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
package com.google.code.configprocessor.io;

import java.io.*;

public class DefaultFileResolver implements FileResolver {

	private String basedir;

	public File resolve(String name) throws IOException {
		File file = new File(name);
		if (!file.exists()) {
			file = new File(basedir, name);
			if (!file.exists()) {
				throw new FileNotFoundException("File [" + name +
						"] does not exist. " + "looked in: " + new File(name) +
						"\n" + file);
			}
		}
		return file;
	}
	
	public void setBasedir(String basedir) {
		this.basedir = basedir;
	}

}
