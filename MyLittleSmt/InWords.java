package MyLittleSmt;

public class InWords {
	@SuppressWarnings("null")
	public String inPolish(Double amount, String currency)
	{
		StringBuilder inWords = new  StringBuilder();
		int intAmount =  amount.intValue();
		Double resAmount = FrameTemplate.round((amount - Double.valueOf(intAmount))*100,2);
		int restAmountS = resAmount.intValue();
		String strAmount = String.valueOf(intAmount);
 
		if (strAmount.length()<=3)
		{
			inWords = three(intAmount);
		} else if (strAmount.length()>3&&strAmount.length()<=6)
		{
			int hundr = ((Math.abs(intAmount) % 1000));
			int thous =(intAmount - hundr)/1000;
			inWords.append( three(thous));
			inWords.append( thousands(thous));
			inWords.append(" " + three(hundr));
		}else if (strAmount.length()>6&&strAmount.length()<=9)
		{
			 
			int thousS = ((Math.abs(intAmount) % 1000000));
			int hundr = ((Math.abs(intAmount) % 1000));
			int thous =(thousS - hundr)/1000;
			int milio =(intAmount - thousS)/1000000;
			inWords.append(three(milio)); 
			inWords.append(milions(milio)); 
			inWords.append(" " + three(thous));
			inWords.append( thousands(thous));
			inWords.append(" " + three(hundr));
		}
		
		
		inWords.append(" " + currency + " " + String.format("%02d",restAmountS)  + "/100");
		return inWords.toString();
	}	
	
	private String milions(int amount)
	{
		int twoLast = Math.abs(amount) % 100;
		String result = new String();
		if (twoLast==1)
		{
			result = " milion";
		}
		else if (twoLast==0)
		{
			result = " milionów";
		}else if (twoLast>=5&&twoLast<=19)
		{
			result = " milionów";
		}else if (twoLast>=2&&twoLast<=4)
		{
			result = " miliony";
		}else if (twoLast>=20)
		{
			int last = Math.abs(amount) % 10;
			if (last==0||last==1)
			{
				result = " milionów";
			}else if  (last>4)
			{
				result = " milionów";
			}else if  (last==2||last==3||last==4)
			{
				result = " miliony";
			}
		}
		
		
		
		return result;
	}
	
	
	private String thousands(int amount)
	{
		 int twoLast = Math.abs(amount) % 100;
		String result = new String();
		if (twoLast==1)
		{
			result = " tysi¹c";
		}
		else if (twoLast==0)
		{
			result = " tysiêcy";
		}else if (twoLast>=5&&twoLast<=19)
		{
			result = " tysiêcy";
		}else if (twoLast>=2&&twoLast<=4)
		{
			result = " tysi¹ce";
		}else if (twoLast>=20)
		{
			int last = Math.abs(amount) % 10;
			if (last==0||last==1)
			{
				result = " tysiêcy";
			}else if  (last>4)
			{
				result = " tysiêcy";
			}else if  (last==2||last==3||last==4)
			{
				result = " tysi¹ce";
			}
		}
		
		
		
		return result;
	}
	
	private StringBuilder three(int intAmount)
	{
		StringBuilder inWords = new StringBuilder();
		String[] descr = {	"zero",
							"jeden",
							"dwa",
							"trzy",
							"cztery",
							"piêæ",
							"szeœæ",
							"siedem",
							"osiem",
							"dziewiêæ",
							"dziesiêæ",
							"jedenaœcie",
							"dwanaœcie",
							"trzynaœcie",
							"czternaœcie",
							"piêtnaœcie",
							"szesnaœcie",
							"siedemnaœcie",
							"osiemnaœcie",
							"dziewiêtnaœcie"};
String[] DecDescr = {	"zero",
						"dziesiêæ",
						"dwadzieœcia",
						"trzydzieœci",
						"czterdzieœci",
						"piêædziesi¹t",
						"szeœædziesi¹t",
						"siedemdziesi¹t",
						"osiemdziesi¹t",
						"dziewiêædziesi¹t"};

String[] HunDescr = {	"zero", 
						"sto", 
						"dwieœcie",
						"trzysta",
						"czterysta",
						"piêæset",
						"szeœæset",
						"siedemset",
						"osiemset",
						"dziewiêæset"};

int last = Math.abs(intAmount) % 10;
	if (intAmount<20 &&intAmount>=0)
	{
		inWords.append(descr[intAmount]);
	
	}else if (intAmount>=20 && intAmount<100)
	{
		if (Math.abs(intAmount) % 10 == 0)
		{
			inWords.append(DecDescr[Math.abs(intAmount)/10]);
		}
		else 
		{
			inWords.append(DecDescr[(intAmount-last)/10]);
			inWords.append(" " + descr[last]);
		}
	}else
	{
		 if (Math.abs(intAmount) % 100 == 0)
		 {
			inWords.append(HunDescr[intAmount/100]);
		 }else 
		 { 
			 int twoLast = Math.abs(intAmount) % 100;
			 if (twoLast<20)
			 {
				 inWords.append(HunDescr[(intAmount-twoLast)/100]);
				 inWords.append(" " + descr[twoLast]); 
			 }else  if (twoLast>20)
			 {
				 inWords.append(HunDescr[(intAmount-twoLast)/100]);
				 inWords.append(" " + DecDescr[(twoLast-last)/10]); 
				 inWords.append(" " + descr[last]);
			 }
		 }
	}

 

	return inWords;
		
	}
}
