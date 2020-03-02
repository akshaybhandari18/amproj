package com.example.amproj;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ComputeTime {

	int dy,mnth,yr,hr,mnt;
	int totalTime=0;
	public ComputeTime(String[] d, String[] t)
	{
		mnt=Integer.parseInt(t[1]);
		hr=Integer.parseInt(t[0]);
		dy=Integer.parseInt(d[0]);
		mnth=Integer.parseInt(d[1]);
		yr=Integer.parseInt(d[2]);
	}

	public int getTime()
	{

		String[] cdat=new String[]{"", "", "", "", "", ""};
		int cdy,cmnth,cyr,chr,cmnt;	// current time
	//	int ddy,dmnth,dyr,dhr,dmnt;

		Calendar cdt=Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String scdt = formatter.format(cdt.getTime());	// get current time...

		int j=0;
		for(int i=0;i<scdt.length();i++)
		{
        	if(scdt.charAt(i)!=('/') && scdt.charAt(i)!=(' ') && scdt.charAt(i)!=(':'))
        		cdat[j]+=scdt.charAt(i);
        	if(scdt.charAt(i)==('/') || scdt.charAt(i)==(' ') || scdt.charAt(i)==(':'))
        		j++;
		}
		cdy=Integer.parseInt(cdat[0]);
		cmnth=Integer.parseInt(cdat[1]);
		cyr=Integer.parseInt(cdat[2]);
		chr=Integer.parseInt(cdat[3]);
		cmnt=Integer.parseInt(cdat[4]);

        Calendar c=Calendar.getInstance();
        c.set(cyr,cmnth,cdy,chr,cmnt);	//set future time to event fire
        Date dd=c.getTime();	//set future time to date object

        c.set(yr,mnth,dy,hr,mnt);
        Date dd1=c.getTime();	// set current time to date object...
        totalTime=(int) ((dd1.getTime() - dd.getTime())/(1000 * 60 ));	// difference between two date in minute

		return totalTime;	//returns no of minutes to delay
	}

}
