package Outlook;

public class OneDrive {
	

 
	public static String Enviroment(String s)
	{
		String FilePath_temp = System.getenv(s);
		String FilePath = FilePath_temp.replace('\\', '/');
		return FilePath;
	}
}
