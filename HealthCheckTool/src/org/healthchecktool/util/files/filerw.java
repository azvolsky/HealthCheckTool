package org.healthchecktool.util.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

public class filerw {
	private File file;
	
	public filerw(String path) {
		file = new File(path);
	}
	public String readFile() throws FileNotFoundException,IOException {
		StringBuffer content= new StringBuffer();
		String temp;
		FileReader fr = new FileReader(this.file);
		BufferedReader bf = new BufferedReader(fr);
		while((temp = bf.readLine())!=null) {
			content.append(temp);
		}
		return new String(content);
	}
	public void writeFile(String content) throws IOException {
		
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
	}
	public String[] fileList() throws IOException {
		File file = new File(this.file.getCanonicalPath());
		String filesList[] = new String[file.listFiles().length];
		for(int i=0;i<file.listFiles().length;i++){
			String str[] = file.listFiles()[i].toString().replace('\\', '/').split("/");
			filesList[i] = str[str.length-1];
		}
		return filesList;
	}
	public Reader getReader() throws FileNotFoundException {
		return new FileReader(file);
	}
	public static String[] fileList(String path) {
		File file = new File(path);
		String filesList[] = new String[file.listFiles().length];
		for(int i=0;i<file.listFiles().length;i++){
			String str[] = file.listFiles()[i].toString().replace('\\', '/').split("/");
			filesList[i] = str[str.length-1];
		}
		return filesList;
	}
}
