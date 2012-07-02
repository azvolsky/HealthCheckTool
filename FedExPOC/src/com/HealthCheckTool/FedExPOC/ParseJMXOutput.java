package com.HealthCheckTool.FedExPOC;

//These are basic classes to be used for parser
import java.util.ArrayList;
import java.util.regex.*;
import java.io.*;
import java.util.Scanner;

public class ParseJMXOutput /*extends Action*/
{
    static String regExp = "Operation Result:(.*)";
    static int aGroup = 1;

    public Matcher getMatcher(String path) throws FileNotFoundException {
        StringBuilder HtmlText = new StringBuilder();
        Scanner in = new Scanner(new File(path));
        try {
            while (in.hasNextLine()){
                HtmlText.append(in.nextLine()).append('\n');
            }
        }
        finally{
            in.close();
        }

        //http://docs.oracle.com/javase/1.4.2/docs/api/java/util/regex/Pattern.html
        //A compiled representation of a regular expression.

        Pattern p = Pattern.compile(regExp);

        //http://docs.oracle.com/javase/1.4.2/docs/api/java/util/regex/Matcher.html
        //An engine that performs match operations on a character sequence by interpreting a Pattern.
        return p.matcher(HtmlText);
    }

     public void main(String[] args) throws IOException
    {

        //read from file into a String variable
        StringBuilder HtmlText = new StringBuilder();
        Scanner in = new Scanner(new File("d:\\XLAM\\DOCS\\CPE\\Health Check Tool\\example_to_parse.html"));
        try {
            while (in.hasNextLine()){
                HtmlText.append(in.nextLine()).append('\n');
            }
        }
        finally{
            in.close();
        }

        //http://docs.oracle.com/javase/1.4.2/docs/api/java/util/regex/Pattern.html
        //A compiled representation of a regular expression.

        Pattern p = Pattern.compile(regExp);

        //http://docs.oracle.com/javase/1.4.2/docs/api/java/util/regex/Matcher.html
        //An engine that performs match operations on a character sequence by interpreting a Pattern.
        Matcher m = p.matcher(HtmlText);

        ArrayList<String> str = new ArrayList<String>();

        while(m.find())
        {
            System.out.println(m.group(1));
            str.add(HtmlText.substring(m.start(),m.end()));
        }

        // System.out.println(" " + p + m + aGroup);
       /* if(str.size()>0)
        System.out.println(m.group(1));
        else System.out.println("Sorry, nothing found.");*/
        /* return new Result(true, "Result string: " + resultString.toString());*/
    }
}