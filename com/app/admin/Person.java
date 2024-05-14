package app.admin;
import java.util.Objects;
import java.util.StringTokenizer;

import app.*;
class Person implements University
{
	protected String name;
	protected DOB dob;
	protected String emailId;
	protected long mobile;
	Person() {
		this.name = "";
		this.dob = new DOB(0, 0, 0);
		this.emailId = "";
		this.mobile=0;
	}

	Person(String name, String dob, String emailId, long mobile) {
		this.name = name;
		this.dob = new DOB(dob);
		this.emailId = emailId;
		this.mobile=mobile;
	}

	Person (String name)
	{
		new Person(name, "1-1-2000", "", 0);
	}

	public String display() {
		return "1. Name : "+name+"\n2. DOB : "+dob+"\n3. Email ID : "+ emailId +"\n4. Mobile : "+mobile;
	}

	@Override
	public void printHeader(String out) {

	}

	void setDOB(String d)
	{
		this.dob= new DOB(d);
	}

	static class DOB {
		final private int day;
		final private int month;
		final private int year;
		DOB(int day, int month, int year)
		{
			this.day=day; 
			this.month=month;
			this.year=year;
		}
		DOB(String date)
		{
			String[]temp=new String[3];
			int further=0;
			StringTokenizer we=new StringTokenizer(date, "/ ,-");
			if(we.countTokens()==3)
			{
				while(we.hasMoreTokens())
				{
					temp[further]=we.nextToken();
					further++;
				}
				this.day=Integer.parseInt(temp[0]);
				this.month=Integer.parseInt(temp[1]);
				this.year=Integer.parseInt(temp[2]);
			}
			else if(date.length()==8){
				this.day=Integer.parseInt(date.substring(0, 2));
				this.month=Integer.parseInt(date.substring(2, 4));
				this.year=Integer.parseInt(date.substring(4, 8));
            }
			else{this.day= 1;this.month= 1;this.year=2000;}
		}
		public String toString(){ return day+"-"+month+"-"+year; }
	}

	public String getName()
	{
		return this.name;
	}
	public String getDOB()
	{
		return this.dob.toString();
	}
	public String getEmailId()
	{
		return this.emailId;
	}
	public long getMobile()
	{
		return this.mobile;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Person person = (Person) o;
		return mobile == person.mobile && Objects.equals(name, person.name) && Objects.equals(dob, person.dob);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, dob, mobile);
	}
}

