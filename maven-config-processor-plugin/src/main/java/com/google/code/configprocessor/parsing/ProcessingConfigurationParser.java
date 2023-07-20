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
package com.google.code.configprocessor.parsing;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import com.google.code.configprocessor.ParsingException;
import com.google.code.configprocessor.processing.AddAction;
import com.google.code.configprocessor.processing.CommentAction;
import com.google.code.configprocessor.processing.ModifyAction;
import com.google.code.configprocessor.processing.NestedAction;
import com.google.code.configprocessor.processing.RemoveAction;
import com.google.code.configprocessor.processing.UncommentAction;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;

public class ProcessingConfigurationParser {

	public NestedAction parse(InputStream is, Charset charset) throws ParsingException {
		if (is == null) {
			throw new NullPointerException("InputStream is null");
		}
		return parse(new InputStreamReader(is, charset));
	}

	public NestedAction parse(Reader is) throws ParsingException {
		XStream xstream = getXStream();
		try {
			return (NestedAction) xstream.fromXML(is);
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	protected XStream getXStream() {
		XStream xstream = new XStream(new PureJavaReflectionProvider());

        // clear out existing permissions and start a whitelist
        xstream.addPermission(NoTypePermission.NONE);
        // allow some basics
        xstream.addPermission(NullPermission.NULL);
        xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
        // allow any type from the same package
        xstream.allowTypesByWildcard(new String[] { "com.google.code.configprocessor.processing**"
        });

		xstream.alias("processor", NestedAction.class);
		xstream.alias("add", AddAction.class);
		xstream.alias("modify", ModifyAction.class);
		xstream.alias("remove", RemoveAction.class);
		xstream.alias("comment", CommentAction.class);
		xstream.alias("uncomment", UncommentAction.class);
		xstream.addImplicitCollection(NestedAction.class, "actions");
		xstream.registerConverter(new AddActionConverter());

		return xstream;
	}
}
