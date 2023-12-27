package pkt;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;

public class YSA {  
	private static final File egitimDosya = new File(YSA.class.getResource("DataEgitim.txt").getPath());
	private static final File testDosya = new File(YSA.class.getResource("DataTest.txt").getPath());
	
	private double[] minimumlar;
	private double[] maksimumlar;
	
	private int araKatmanNoronSayisi;
	private MomentumBackpropagation mbp;
	private DataSet egitimVeriSeti;
	private DataSet testVeriSeti; 

	public YSA(int araKatmanNoronSayisi, double momentum, double ogrenmeKatsayisi, double maxHata, int epoch) throws FileNotFoundException {
		
		minimumlar = new double[4];
		maksimumlar = new double[4];
		
		for(int i = 0; i<1; i++) {
			minimumlar[i] = Double.MAX_VALUE; 
			maksimumlar[i] = Double.MIN_VALUE;
		}
		MinimumveMaksimumlar(egitimDosya);
		MinimumveMaksimumlar(testDosya);
		egitimVeriSeti = veriSetiOku(egitimDosya);
		testVeriSeti = veriSetiOku(testDosya);
		
		mbp = new MomentumBackpropagation();
		mbp.setMomentum(momentum);
		mbp.setLearningRate(ogrenmeKatsayisi);
		mbp.setMaxError(maxHata);
		mbp.setMaxIterations(epoch);
	}
	public void egit() {
		MultiLayerPerceptron sinirselAg = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,3,araKatmanNoronSayisi,1);
		sinirselAg.setLearningRule(mbp);
		sinirselAg.learn(egitimVeriSeti);
		sinirselAg.save("ysa.nnet");
		System.out.println("Egitim tamamlandý. ");
	}
	public double test() {
		NeuralNetwork sinirselAg = NeuralNetwork.createFromFile("ysa.nnet");
		double toplamHata = 0;
		var satirlar = testVeriSeti.getRows();
		for(DataSetRow satir : satirlar)
		{
			sinirselAg.setInput(satir.getInput());
			sinirselAg.calculate();
			toplamHata += mse(satir.getDesiredOutput(),sinirselAg.getOutput());
		}
		return toplamHata/testVeriSeti.size();
	}
	public double egitimHata() {
		return mbp.getTotalNetworkError();
		
	}
	private double mse(double[] beklenen, double[] uretilen) {
		double birSatirHata = 0;
		for(int i = 0; i<beklenen.length; i++)
		{
			birSatirHata += Math.pow(beklenen[i] - uretilen[i],2);
		}
		return birSatirHata/beklenen.length;
	}
	private void MinimumveMaksimumlar(File dosya) throws FileNotFoundException {
		Scanner veriler = new Scanner(dosya);
		
		while (veriler.hasNextLine()) {

			String[] deger = veriler.nextLine().split("\\s+"); 
			for (int i = 0; i < 4; i++) {
				double d = Double.parseDouble(deger[i]);
				if (d < minimumlar[i])
					minimumlar[i] = d;
				if (d > maksimumlar[i])
					maksimumlar[i] = d;
			}
		}
		veriler.close();
	}
	private DataSet veriSetiOku(File dosya) throws FileNotFoundException {
		Scanner in = new Scanner(dosya);
		DataSet ds = new DataSet(3, 1);
		
		while (in.hasNextLine()) {

			String[] deger = in.nextLine().split("\\s+"); 
			double[] inputs = new double[3];
			double[] outputs = new double[1];
			for (int i = 0; i < 4; i++) {
				double d = Double.parseDouble(deger[i]);
				if (i == 3) {
					outputs[0] = minmaxDeger(d, minimumlar[i], maksimumlar[i]);

				} else {
					inputs[i] = minmaxDeger(d, minimumlar[i], maksimumlar[i]);
				}
			}
			DataSetRow satir = new DataSetRow(inputs, outputs);
			ds.add(satir);
		}
		in.close();
		return ds;
	}
	private double minmaxDeger(double d, double min, double max) {
		return(d-min)/(max-min);
	}

}