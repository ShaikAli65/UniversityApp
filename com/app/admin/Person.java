package app.admin;

import app.Date;

import java.io.Serializable;
import java.util.Objects;


class Person implements Serializable
{
	protected String name;
	protected Date dob;
	protected String emailId;
	protected long mobile;
	Person() {
		this.name = "";
		this.dob = new Date(0, 0, 0);
		this.emailId = "";
		this.mobile=0;
	}

	Person(String name, String dob, String emailId, long mobile) {
		this.name = name;
		this.dob = new Date(dob);
		this.emailId = emailId;
		this.mobile=mobile;
	}

	Person (String name)
	{
		this.name = name;
		this.dob = new Date("1/1/2000");
		this.emailId = "";
		this.mobile = 0;
	}

	public String display() {
		return     "1. Name       : " + name
				+"\n2. DOB        : " + dob
				+"\n3. Email      : "+ emailId
				+"\n4. Mobile     : "+mobile;
	}

	void setDOB(String d)
	{
		this.dob.setDate(d);
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

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setMobile(long mobile) {
		this.mobile = mobile;
	}

	public void setName(String name) {
		this.name = name;
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

