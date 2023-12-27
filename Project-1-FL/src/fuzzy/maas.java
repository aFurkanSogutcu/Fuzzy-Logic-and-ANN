package fuzzy;

import java.io.File;
import java.net.URISyntaxException;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;

public class maas {
	private final FIS fis;
	
	public maas() throws URISyntaxException {
		File file = new File(getClass().getResource("maas.fcl").toURI());
        fis = FIS.load(file.getPath(), true);
	}
	public double getMaas(double tecrubeSuresi, double egitimSuresi, double cinsiyet) throws URISyntaxException {
		if(tecrubeSuresi > 50 || egitimSuresi > 12) return -1;
        //File file = new File(getClass().getResource("maas.fcl").toURI());
        fis.setVariable("tecrubeSuresi", tecrubeSuresi);
        fis.setVariable("egitimSuresi", egitimSuresi);
        fis.setVariable("cinsiyet", cinsiyet);
        fis.evaluate();
        return fis.getVariable("maas").getValue() ;
    }
	public void grafiklerCiz() {
		JFuzzyChart.get().chart(fis);
		JFuzzyChart.get().chart(fis.getVariable("maas").getDefuzzifier(), "maas", true);
	}
} 
