package fuzzy;

import java.net.URISyntaxException;
import java.util.Scanner;

public class program {
		public static void main(String[] args) {
			Scanner in = new Scanner(System.in);
			System.out.print("Tecrübe Süresi (Yýl): ");
			double tecrubeSuresi = in.nextDouble();
			System.out.print("Eðitim Süresi (Yýl): ");
			double egitimSuresi = in.nextDouble();
			System.out.print("Cinsiyet (0 veya 1 giriniz): ");
			double cinsiyet = in.nextDouble();
			try {
				maas maas = new maas();
				System.out.print("Maas: " + maas.getMaas(tecrubeSuresi, egitimSuresi, cinsiyet));
				maas.grafiklerCiz();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
}
