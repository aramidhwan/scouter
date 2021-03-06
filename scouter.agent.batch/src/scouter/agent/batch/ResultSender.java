/*
 *  Copyright 2016 the original author or authors. 
 *  @https://github.com/scouter-project/scouter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. 
 */

package scouter.agent.batch;

import java.io.File;
import java.io.FileWriter;

import scouter.agent.batch.trace.TraceContext;

public class ResultSender extends Thread {
	public void run(){
		try {
			Configure config = Configure.getInstance();
			config.scouter_stop = true;
			
			TraceContext traceContext = TraceContext.getInstance();
			traceContext.endTime = System.currentTimeMillis();
			traceContext.caculateLast();
			
			String result = traceContext.toString();
			if(config.scouter_standalone){
				saveStandAloneResult(traceContext, result);
			}
			Logger.println(result);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void saveStandAloneResult(TraceContext traceContext, String result){
		File resultFile = new File(traceContext.getLogFilename() + ".sbr");
		if(resultFile.exists()){
			return;
		}
		FileWriter writer = null;
		try {
			writer = new FileWriter(resultFile);
			writer.write(result);
		}catch(Exception ex){
			Logger.println(ex.toString());
		}finally{
			if(writer != null){
				try{ writer.close(); }catch(Exception ex){}
			}
		}
	}
}
