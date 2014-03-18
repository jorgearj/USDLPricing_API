package usdl.constants.enums;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * The FOAF Enumeration of properties and classes necessary constants.
 * @author  Daniel Barrigas
 * @author Jorge Araujo
 * @version 1.0, March 10
 */
public enum CLOUDEnum {

	// interface Types
	API("API", "C"), COMMAND_LINE("CommandLine", "C"), CONSOLE("Console", "C"), 
	GUI("GUI", "C"), WEB("Web", "C"), OTHER_INTERFACE("Interface", "C"),

	CONSISTENCY("Consistency", "C"), DURABILITY("Durability", "C"), 
	PERFORMANCE("Performance", "C"), RELIABILITY("Reliability", "C"), SCALABILITY("Scalability", "C"),
	// security Types
	SECURITY("Security", "C"), SSL("SSL", "C"), ENCRYPTION("Encryption", "C"), 
	LOCATION("Location", "C"), MONITORING("Monitoring", "C"), PLATFORM("Platform", "C"), REPLICATION("Replication", "C"), 
	COMPUTINGINTANCE("ComputingInstance", "C"), CPUTYPE("CPUType", "C"), 
	GRAPHICALCARD("GraphicalCard", "C"), LOADBALANCING("LoadBalancing", "C"),
	MEMORYALLOCATION("MemoryAllocation", "C"), STORAGETYPE("StorageType", "C"), 
	FAILOVER("FailOver", "C"),

	// Support types
	DEVELOPERCENTER("DeveloperCenter", "C"), FORUM("Forum", "C"), 
	LIVECHAT("LiveChat", "C"), MANUAL("Manual", "C"), SUPPORT_24_7("Support_24_7", "C"),
	SUPPORTTEAM("SupportTeam", "C"), VIDEOS("Videos", "C"), OTHER_SUPPORT("SupportProperties", "C"),

	// License types
	OPENSOURCE("OpenSource", "C"), PROPRIETARY("Proprietary", "C"),

	// Architecture types
	BIT32("Arch32bit", "C"), BIT64("Arch64bit", "C"),

	// OS Types
	EMBEDDED("Embedded", "C"), MOBILE("Mobile", "C"), WINDOWS("Windows", "C"), 
	UNIX("Unix", "C"), REALTIME("RealTime", "C"),

	// ----->DATA PROPERTIES
	BACKUP_RECOVERY("Backup_Recovery", "C"), REDUNDANCY("Redundancy", "C"),

	// ----->NETWORK PROPERTIES
	PUBLICIP("PublicIP", "C"), ELASTICIP("ElasticIP", "C"), IPV4("IPv4", "C"), 
	IPV6("IPv6", "C"), PROTOCOL("NetworkProtocol", "C"),

	// ----->PLATFORM PROPERTIES
	LANGUAGE("Language", "C"),

	// ----->Messages
	MESSAGEPROTOCOL("MessageProtocol", "C"), MESSAGETYPE("MessageType", "C"),

	// Other Features
	FEATURE("Feature", "C"),
	
	
	//******************************************************************************** QUANTITATIVE ************************************************************
	
	
	AVAILABILITY("Availability", "C"),
	CACHESIZE("CacheSize", "C"),
	CPUSPEED("CPUSpeed", "C"),
	DATAINEXTERNAL("DataINExternal", "C"),
	DATAININTERNAL("DataINInternal", "C"),
	DATAOUTEXTERNAL("DataOUTExternal", "C"),
	DATAOUTINTERNAL("DataOUTInternal", "C"),
	DATAPROCESSED("DataProcessed", "C"),
	DISKSIZE("DiskSize", "C"),
	FILESIZE("FileSize", "C"),
	MEMORYSIZE("MemorySize", "C"),
	NETWORKDELAY("NetworkDelay", "C"),
	NETWORKINTERNALBANDWIDTH("NetworkInternalBandwidth", "C"),
	NETWORKLATENCY("NetworkLatency", "C"),
	NETWORKPUBLICBANDWIDTH("NetworkPublicBandwidth", "C"),
	STORAGECAPACITY("StorageCapacity", "C"),
	TRAFFIC("Traffic", "C"),
	TRANSFERRATE("TransferRate", "C"),
	APICALLS("APICalls", "C"),
	APPLICATIONS("Applications", "C"),
	COPYREQUESTS("COPYRequest", "C"),
	CPUCORES("CPUCores", "C"),
	CPUFLOP("CPUFlops", "C"),
	DELETEREQUESTS("DELETERequest", "C"),
	GETREQUESTS("GETRequest", "C"),
	HTTPREQUEST("HTTPRequest", "C"),
	HTTPSREQUEST("HTTPSRequest", "C"),
	IOOPERATIONS("IOOperations", "C"),
	LISTREQUESTS("LISTRequest", "C"),
	POSTREQUESTS("POSTRequest", "C"),
	PUTREQUESTS("PUTRequest", "C"),
	QUERIES("Queries", "C"),
	READSREQUESTS("Reads", "C"),
	RECORDS("Records", "C"),
	TRANSACTIONS("Transactions", "C"),
	USERS("Users", "C"),
	WEBSITES("Websites", "C"),
	WRITESREQUESTS("Writes", "C"),
	NUMBEROFIPS("NumberOfIPs", "C"),
	MESSAGENUMBER("MessageNumber", "C"),
	DEDICATEDIP("DedicatedIP", "C");
	 
	private String concept;
	private String type; // C = Class || P = Property
 
	/**
	 * CloudTaxonomy Enumerator constructor. 
	 * @param   c   String of the concept.
	 * @param   t   type of the concept. Either "C" for classes or "P" for properties.
	 */
	private CLOUDEnum(String c, String t) {
		concept = c;
		type = t;
	}
 
	/**
	 * Return the string of the enumerator element. 
	 * @return   A String of the property with its prefix.
	 */
	public String getConceptURI(){
		return Prefixes.CLOUD.getPrefix() + ":" + concept;
	}
	
	/**
	 * Return a ready to use Jena Resource 
	 * @param   model  Semantic model where the concept is located.
	 * @return   A Jena Resource.
	 */
	public Resource getConceptResource(Model model) {
		if(type.equalsIgnoreCase("C")){
			return model.createResource(Prefixes.CLOUD.getPrefix() + ":" + concept);
		}else{
			return null;
		}
	}

}
