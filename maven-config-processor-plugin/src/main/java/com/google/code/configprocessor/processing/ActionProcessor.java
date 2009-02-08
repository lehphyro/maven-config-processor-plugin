package com.google.code.configprocessor.processing;

import java.io.*;

import com.google.code.configprocessor.*;

public interface ActionProcessor {

	void process(InputStreamReader input, OutputStreamWriter output, Action action) throws ParsingException, IOException;
	
}
