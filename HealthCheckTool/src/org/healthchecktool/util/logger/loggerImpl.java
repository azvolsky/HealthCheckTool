package org.healthchecktool.util.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class loggerImpl implements logger {
	private 	File 			logFile;
	private 	BufferedWriter 	writer;
	
	public loggerImpl(){
		logFile = new File("test.log");
		openFile();
	}
	public loggerImpl(String file) {
		logFile = new File(file);
		openFile();
	}
	private void openFile() {
		FileWriter fw;
		try{
			fw = new FileWriter(logFile,true);
			writer = new BufferedWriter(fw); 
		}
		catch(IOException ioe){
			ioe.printStackTrace();
			closeFile();
		}
		
	}
	private String createMessage(Class exceptionClass,Exception exception){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		StringBuffer message = new StringBuffer();
		message.append("[");
		message.append(dateFormat.format(date));
		message.append("]");
		message.append(" - ");
		message.append("(");
		message.append(exceptionClass.toString());
		message.append(")");
		message.append(" - ");
		message.append("{");
		message.append(exception.toString());
		message.append("}");
		message.append("\n");
		for(int i=0;i<exception.getStackTrace().length;i++){
			message.append("\t");
			message.append(exception.getStackTrace()[i]);
			message.append("\n");
		}
		return new String(message);
	}
	public void log(Class exceptionClass,Exception exception){
		
		try{
			writer.write(createMessage(exceptionClass,exception));
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	public void closeFile() {
		if(writer != null) {
			try{
				writer.close();
			}
			catch(IOException ioe){
				ioe.printStackTrace();
			}
		}
	}
}