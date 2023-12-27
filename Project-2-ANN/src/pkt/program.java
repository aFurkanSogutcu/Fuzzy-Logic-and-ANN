package pkt;


import java.io.FileNotFoundException;
import java.net.URISyntaxException;

public class program {
	public static void main(String[] args) throws FileNotFoundException, URISyntaxException {		
		
		YSA ysa = null;
		
		ysa = new YSA(5,0.1,0.14,0.08,5000);
					
		ysa.egit();
		System.out.println("egitimdeki hata: "+ysa.egitimHata());
		System.out.println("test hata: "+ysa.test());
	}
	
}