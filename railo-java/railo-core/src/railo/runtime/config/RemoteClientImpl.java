package railo.runtime.config;


import railo.runtime.crypt.CFMXCompat;
import railo.runtime.exp.PageException;
import railo.runtime.functions.other.Encrypt;
import railo.runtime.net.proxy.ProxyData;
import railo.runtime.net.rpc.client.WSClient;
import railo.runtime.op.Caster;
import railo.runtime.spooler.remote.RemoteClientTask;
import railo.runtime.type.Struct;
import railo.runtime.type.StructImpl;
import railo.runtime.type.util.KeyConstants;
import railo.runtime.type.util.ListUtil;

public class RemoteClientImpl implements RemoteClient {

	private String url;
	private String serverUsername;
	private String serverPassword;
	private ProxyData proxyData;
	private String type;
	private String adminPassword;
	private String securityKey;
	private String label;
	private String usage;
	private String id;

	public RemoteClientImpl(String label,String type, String url, String serverUsername, String serverPassword,String adminPassword, ProxyData proxyData, String securityKey,String usage) {
		this.label = label;
		this.url = url;
		this.serverUsername = serverUsername;
		this.serverPassword = serverPassword;
		this.proxyData = proxyData;
		this.type = type;
		this.adminPassword = adminPassword;
		this.securityKey = securityKey;
		this.usage = usage;
	}

	/**
	 * @return the url
	 */
	@Override
	public String getUrl() {
		return url;
	}

	/**
	 * @return the serverUsername
	 */
	@Override
	public String getServerUsername() {
		return serverUsername;
	}

	/**
	 * @return the serverPassword
	 */
	@Override
	public String getServerPassword() {
		return serverPassword;
	}

	/**
	 * @return the proxyData
	 */
	@Override
	public ProxyData getProxyData() {
		return proxyData;
	}

	/**
	 * @return the type
	 */
	@Override
	public String getType() {
		return type;
	}

	/**
	 * @return the adminPassword
	 */
	@Override
	public String getAdminPassword() {
		return adminPassword;
	}

	/**
	 * @return the securityKey
	 */
	@Override
	public String getSecurityKey() {
		return securityKey;
	}

	@Override
	public String getAdminPasswordEncrypted() {
		try {
			return Encrypt.invoke( getAdminPassword(), getSecurityKey(), CFMXCompat.ALGORITHM_NAME, "uu", null, 0 );
		} 
		catch (PageException e) {
			return null;
		}
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getUsage() {
		return usage;
	}

	@Override
	public boolean hasUsage(String usage) {
		return ListUtil.listFindNoCaseIgnoreEmpty(this.usage,usage,',')!=-1 ;
	}

	@Override
	public String getId(Config config) {

		if(id!=null) return id;
		
		Struct attrColl = new StructImpl();
		attrColl.setEL(KeyConstants._action, "getToken");
		
		Struct args = new StructImpl();
		args.setEL(KeyConstants._type, getType());
		args.setEL(RemoteClientTask.PASSWORD, getAdminPasswordEncrypted());
		args.setEL(RemoteClientTask.CALLER_ID, "undefined");
		args.setEL(RemoteClientTask.ATTRIBUTE_COLLECTION, attrColl);
		
		
		
		try {
			WSClient rpc = 
				WSClient.getInstance(null,getUrl(),getServerUsername(),getServerPassword(),getProxyData());
			
			Object result = rpc.callWithNamedValues(config, KeyConstants._invoke, args);
			return id=ConfigImpl.getId(securityKey, Caster.toString(result,null),false, null);
			
		} 
		catch (Throwable t) {t.printStackTrace();
			return null;
		}
	}
	

}
