package Outlook;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CMD {
	
    public void main(String s) throws Exception 
    {
        ProcessBuilder builder = new ProcessBuilder( 
        	"cmd.exe " + "/c	 " + s);
            //"cmd.exe", "/c", " "C:\\Program Files\\Microsoft SQL Server\" && dir");
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) { break; }
            System.out.println(line);
        }
    }
}
