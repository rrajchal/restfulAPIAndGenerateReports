package com.energy.restfulExample;

/**
 * This is just bean class for variables.
 * No annotation or ApplicationContext interfaces were used. 
 * @author Rajesh
 */
public class EnergyRequest {
	int activeMonth;
	String organization;
	String vsc;
	String name;
	String description;
	Double laborHours;
	String repositoryURL;
	boolean status;
	String tags;
	String license;
	Double totalLaborHours;
	int[] mostActiveMonths;
	int releaseCount;
	
	// all getters and setters
	
	public int[] getMostActiveMonths() {
		return mostActiveMonths;
	}
	public void setMostActiveMonths(int[] mostActiveMonths) {
		this.mostActiveMonths = mostActiveMonths;
	}
	public Double getTotalLaborHours() {
		return totalLaborHours;
	}
	public void setTotalLaborHours(Double totalLaborHours) {
		this.totalLaborHours = totalLaborHours;
	}
	
	public int getReleaseCount() {
		return releaseCount;
	}
	public void setReleaseCount(int releaseCount) {
		this.releaseCount = releaseCount;
	}

	public int getActiveMonth() {
		return activeMonth;
	}
	public void setActiveMonth(int activeMonth) {
		this.activeMonth = activeMonth;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getLaborHours() {
		return laborHours;
	}
	public void setLaborHours(Double laborHours) {
		this.laborHours = laborHours;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getRepositoryURL() {
		return repositoryURL;
	}
	public void setRepositoryURL(String repositoryURL) {
		this.repositoryURL = repositoryURL;
	}
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getVsc() {
		return vsc;
	}
	public void setVsc(String vsc) {
		this.vsc = vsc;
	}
}
