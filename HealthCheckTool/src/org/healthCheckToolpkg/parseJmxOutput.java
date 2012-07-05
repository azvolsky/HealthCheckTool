package org.healthCheckToolpkg;

//These are basic classes to be used for parser
import java.util.ArrayList;
import java.util.regex.*;

public class parseJmxOutput /*extends Action*/
{
    private String regExp;
    static int aGroup = 1;
    private String httpResponse;
    //Constructor
    public parseJmxOutput(String responseItself){
        httpResponse = responseItself;
        regExp = "Operation Result:(.*)";  //initializing our RegExp
    }

    //setter for RegExp
    public void setRegExp(String regularka) {
        this.regExp = regularka;
    }

    public ArrayList<String> getMatcher() {

        //http://docs.oracle.com/javase/1.4.2/docs/api/java/util/regex/Pattern.html
        //A compiled representation of a regular expression.

        Pattern p = Pattern.compile(regExp);
        System.out.print("what is inside Pattern Object \n" + p + " ");     //debug pattern obj
        Matcher m = p.matcher(httpResponse);
        System.out.print("what is inside Matcher Object \n" + m + " ");    //debug matcher obj
        //http://docs.oracle.com/javase/1.4.2/docs/api/java/util/regex/Matcher.html
        //An engine that performs match operations on a character sequence by interpreting a Pattern.
        //System.out.print("what is iside " + m.group(1));    //debug m.group
        //return m.group(1); this was initially

        ArrayList<String> str = new ArrayList<String>();
        while(m.find())
        {
            System.out.println("\n" + m.group(0));
            str.add(httpResponse.substring(m.start(),m.end()));
        }
        return str;
    }

//     public void main(String[] args) throws IOException
//    {
//
//        //read from file into a String variable
//        StringBuilder HtmlText = new StringBuilder();
//        Scanner in = new Scanner(new File("d:\\XLAM\\DOCS\\CPE\\Health Check Tool\\example_to_parse.html")); //this will be removed as we're going to use http output directly
//        try {
//            while (in.hasNextLine()){
//                HtmlText.append(in.nextLine()).append('\n');
//            }
//        }
//        finally{
//            in.close();
//        }
//
//        //http://docs.oracle.com/javase/1.4.2/docs/api/java/util/regex/Pattern.html
//        //A compiled representation of a regular expression.
//
//        Pattern p = Pattern.compile(regExp);
//
//        //http://docs.oracle.com/javase/1.4.2/docs/api/java/util/regex/Matcher.html
//        //An engine that performs match operations on a character sequence by interpreting a Pattern.
//
//
//        ArrayList<String> str = new ArrayList<String>();
//
//        while(m.find())
//        {
//            System.out.println(m.group(1));
//            str.add(HtmlText.substring(m.start(),m.end()));
//        }
//
//        // System.out.println(" " + p + m + aGroup);
//       /* if(str.size()>0)
//        System.out.println(m.group(1));
//        else System.out.println("Sorry, nothing found.");*/
//        /* return new Result(true, "Result string: " + resultString.toString());*/
//    }
}