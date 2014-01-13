package com.chen.hello;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/*If jersey can not automatically convert to JSON object, then we may need to add maven dependency 
<dependency>
<groupId>com.sun.jersey</groupId>
<artifactId>jersey-json</artifactId>
<version>1.17</version>
</dependency>
*/

@XmlRootElement
public class ResponseMetaData {
	String populationTotal;
	String populationDesity;
	String areaTotal;
	
	
	public ResponseMetaData(){
	}
	
	public ResponseMetaData(String populationTotal,String populationDesity,String areaTotal){
		this.populationDesity=populationDesity;
		this.areaTotal=areaTotal;
		this.populationTotal=populationTotal;
	}
	
	
	@XmlElement(name="populationTotal")
	String getPopulationTotal(){
		return populationTotal;
	}

	@XmlElement(name="populationDesity")
	String getPopulationDesity(){
		return populationDesity;
	}
	
	
	@XmlElement(name="areaTotal")
	String getAreaTotal(){
		return areaTotal;
	}
	
	void setPopulationTotal(String populationTotal){
		this.populationTotal=populationTotal;
	}
	
	void setPopulationDesity(String populationDesity){
		this.populationDesity=populationDesity;
	}
	
	void setAreaTotal(String areaTotal){
		this.areaTotal=areaTotal;
	}
	
}
