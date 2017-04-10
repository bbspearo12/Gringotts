package com.groupware.gringotts.domain;

public class GenericBulkUpload {
//OEM	Model	Serial Number	Type	Contract	Name	Address Line 1	City	State	Zip	Primary Contact	Phone Number	Email	Start Date	End Date	Coverage Plan	Service Vendor	Vendor Contact Number
	private String[] entries;

	public String[] getEntries() {
		return entries;
	}

	public void setEntries(String[] entries) {
		this.entries = entries;
	}
}
