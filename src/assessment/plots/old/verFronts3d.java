import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileWriter; 
import java.io.File;
import java.util.Scanner;




public class verFronts3d {
	public static void main(String[] args) throws IOException{	

		if(args.length != 1){
			System.out.println("uso: medias <resultados>");
			System.exit(1);
		}
	
		FileReader fr = new FileReader(args[0]);
		BufferedReader br = new BufferedReader(fr);
		ArrayList<double[]> dados = new ArrayList<double[]>();
		ArrayList<double[]> matrizMedias = new ArrayList<double[]>();

		String linha = br.readLine();
		while (linha != null) {
			String[] objs = linha.split("\\s");
			if(objs.length==0)
				objs = linha.split("\t");
			if ( objs.length>0 && (objs.length != 1 || !(objs[0].equals(""))) && !objs[0].equals("#") ) {
				double[] d = new double[objs.length];
				for (int i = 0; i < objs.length; i++) {
					d[i] = Double.parseDouble(objs[i].replace(",","."));
				}
				dados.add(d);
			} else {
				if(dados.size() > 0){
					executar(dados);
				}
				dados.clear();
			}
			linha = br.readLine();
		}
		if(dados.size() > 0){
			executar(dados);
		}

	}
	static void executar(ArrayList<double[]> dados){		
		try (FileWriter entrada = new FileWriter("temp") ) {
			for(int i=0;i<dados.size();i++){ //percorre colunas
				for(int o=0;o<dados.get(i).length;o++){
					entrada.write(dados.get(i)[o]+" ");
				}
				entrada.write("\n\n\n");
			}
// 			for(int o=0;o<dados.size();o++){
// 				for(int i=0;i<dados.get(0).length;i++){ //percorre colunas
// 					entrada.write(i+" "+dados.get(o)[i]+"\n");
// 				}
// 				entrada.write("\n\n\n");
// 			}
			entrada.close();
		
			//String comando="gnuplot -e \"set key below ; set format y '%.2f'; splot 'temp' with points pt 7 ps 2 lt 3\" -persist";
			String comando="gnuplot -e \"splot 'temp' with points pt 7 ps 1 lt 3\" -persist";
			
		
 			Process p =  Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", comando});
 			p.waitFor();
 			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ( (line=br.readLine()) != null) {
					System.out.println(line);
			}	
				
			System.out.println("Digite algo para interromper: ");
			Scanner in = new Scanner(System.in);
			//System.out.println('"'+in.nextLine()+'"');
			
// 			File file = new File("temp");
// 			if(!file.delete())
// 				System.out.println("Temp file delete operation failed.");
			
			if(in.nextLine().length() != 0)
				System.exit(0);
			
		}catch (Exception e){	e.printStackTrace();}
	}
}
