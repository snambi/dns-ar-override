package org.github.dns;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Security;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.Cache;
import org.xbill.DNS.Credibility;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

public class DnsTest {

	static final String GOOG = "google.com";
	static final String LWN = "lwn.net"; // 45.33.94.129
	static final String LLVM = "llvm.org"; // 192.17.58.186

	public static final void main( String[] args ) {
	
		System.out.println("Beging DNS test");
		
		/** 
		 * Set the following system properties.
		 * http://docs.oracle.com/javase/1.5.0/docs/guide/net/properties.html
		 */
		System.setProperty("sun.net.spi.nameservice.provider.1", "dns,dnsjava");
		
		// set the security settings
		Security.setProperty("networkaddress.cache.ttl", "0"); 
		
		try {
			
			InetAddress lwnAddress = InetAddress.getByName( LWN );
			
			System.out.println("ip address: " + lwnAddress.getHostAddress() );
			System.out.println("INET " + lwnAddress.toString() + "\n" );
			

			Name lwnname = new Name(LWN + ".");
			
			// represention of LLVM ip address as a byte array
			byte[] yAddr = new byte[]{  (byte)  192, (byte) 17, (byte) 58, (byte) 186};
			InetAddress kernelAddress = InetAddress.getByAddress( yAddr);
			
			
			
			// change the ARecord of LWN to KERNEL 
			Record arec = new ARecord( lwnname, Type.A, 999999999, kernelAddress);
						
			// print the default lookup cache for examining its contents			
			System.out.println("Cache: " + Lookup.getDefaultCache(Type.A) );

			// flush the contents of existing cache to remove all entries
			Lookup.getDefaultCache(Type.A).flushName(lwnname);


			System.out.println("Cache: " + Lookup.getDefaultCache(Type.A) );
			
			// Add the new A-Record into the cache
			Lookup.getDefaultCache(Type.A).addRecord(arec, Credibility.NORMAL, new DnsTest() );
			
			Cache c = Lookup.getDefaultCache(Type.A);
			// print the default lookup cache for examining its contents			
			System.out.println("Cache: " + Lookup.getDefaultCache(Type.A) );
			
			
			//TEST 1 : Use InetAddress to find the ip address
			InetAddress address1 = InetAddress.getByName( LWN );
			System.out.println("ip address: " + address1.getHostAddress() );
			System.out.println("INET " + address1.toString() );
	
			// TEST 2 : Use apache HttpClient to connect to google.com.
			//          this will actually connect to yahoo and show the results.
			String url = "http://" + LWN;
			
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(url);
			
			HttpResponse response = client.execute(request);

			System.out.println("Response Code : " 
		                + response.getStatusLine().getStatusCode());

		
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			
			System.out.println("Result: " + result );
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (TextParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}
	
}
