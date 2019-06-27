package ok;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.openimaj.data.dataset.GroupedDataset;
import org.openimaj.data.dataset.ListDataset;
import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.experiment.dataset.split.GroupedRandomSplitter;
import org.openimaj.experiment.dataset.util.DatasetAdaptors;
import org.openimaj.feature.DoubleFV;
import org.openimaj.feature.DoubleFVComparison;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.Transforms;
import org.openimaj.image.model.EigenImages;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.FaceDetector;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
import org.openimaj.image.processing.resize.ResizeProcessor;



public class TestRun {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame("Results");
		JPanel panel = new JPanel();
		JLabel label1 = new JLabel("1");
		JLabel label2 = new JLabel("2");
		JLabel label3 = new JLabel("3");
		JLabel label4 = new JLabel("4");
		JFileChooser fc1 = new JFileChooser();
		JFileChooser fc2 = new JFileChooser();
		JFileChooser fc3 = new JFileChooser();
		JFileChooser fc4 = new JFileChooser();
		JFileChooser fc5 = new JFileChooser();
		
		JFileChooser fc6 = new JFileChooser();
		JFileChooser fc7 = new JFileChooser();
		
		
		VFSGroupDataset <FImage> dataset = 
			    new VFSGroupDataset <FImage> ("zip:/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/att_faces.zip",ImageUtilities.FIMAGE_READER);
		//FImage imageee = ImageUtilities.readF(new File("F:\\大学\\大数据\\Final_Project\\att_faces\\s14\\9.pgm"));

		//divide dataset
		int nTraining = 5;
		int nTesting = 5;
		GroupedRandomSplitter <String,FImage> splits = 
		    new GroupedRandomSplitter <String,FImage> (dataset,nTraining,0,nTesting);
		GroupedDataset <String,ListDataset <FImage>,FImage> training = splits.getTrainingDataset();
		GroupedDataset <String,ListDataset <FImage>,FImage> testing = splits.getTestDataset();
		
		System.out.println("-------------------------------------");
		
		//load the EigenImages model
		int nEigenvectors = 100;
		EigenImages eigen = new EigenImages(nEigenvectors);
		FileInputStream fos = new FileInputStream("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/3.txt");
		DataInputStream dos = new DataInputStream(fos);
		eigen.readBinary(dos);
		dos.close();
		fos.close();


		//model building done
		//build a base basis set
		Map <String,DoubleFV []> features = new HashMap <String,DoubleFV []> ();
		for (final String person:training.getGroups()){
		    final DoubleFV [] fvs = new DoubleFV [nTraining];
		    int sum = 0;
		    for(int i = 0; i <nTraining; i ++){
		        final FImage face = training.get(person).get(i);
			    fvs [i] = eigen.extractFeature(face);
		    }
		    features.put(person,fvs);
		}
		
        System.out.println(features.keySet().size());

		//add a Mike's face's basis
		FImage image2 = ImageUtilities.readF(new File("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/Joke/1.jpg"));
        List <FImage> buildset2 = getface(image2);
	    DoubleFV [] fvs = new DoubleFV [nTraining];
    	fvs [0] = eigen.extractFeature(buildset2.get(0));
    	image2 = ImageUtilities.readF(new File("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/Joke/2.jpg"));
        buildset2 = getface(image2);
    	fvs [1] = eigen.extractFeature(buildset2.get(0));
    	image2 = ImageUtilities.readF(new File("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/Joke/3.jpg"));
        buildset2 = getface(image2);
    	fvs [2] = eigen.extractFeature(buildset2.get(0));
    	image2 = ImageUtilities.readF(new File("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/Joke/4.jpg"));
        buildset2 = getface(image2);
    	fvs [3] = eigen.extractFeature(buildset2.get(0));
    	image2 = ImageUtilities.readF(new File("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/Joke/5.jpg"));
        buildset2 = getface(image2);
    	fvs [4] = eigen.extractFeature(buildset2.get(0));
	    features.put("New Face",fvs);
	    
	    //add a Obama's face's basis
	    //Obama's face has been cut in to 92:112,dont need to find face
		//image2 = ImageUtilities.readF(new File("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/Obama/0.png"));
	    image2 = ImageUtilities.readF(new File("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/testfaces/s18/3.pgm"));
	    fvs = new DoubleFV [nTraining];
    	fvs [0] = eigen.extractFeature(image2);
    	image2 = ImageUtilities.readF(new File("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/testfaces/s18/5.pgm"));
    	fvs [1] = eigen.extractFeature(image2);
    	image2 = ImageUtilities.readF(new File("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/testfaces/s18/7.pgm"));
    	fvs [2] = eigen.extractFeature(image2);
    	image2 = ImageUtilities.readF(new File("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/testfaces/s18/9.pgm"));
    	fvs [3] = eigen.extractFeature(image2);
    	image2 = ImageUtilities.readF(new File("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/testfaces/s18/10.pgm"));
    	fvs [4] = eigen.extractFeature(image2);
	    features.put("Train Face",fvs);

	    
    	//test Obama's face
		FImage image4 = ImageUtilities.readF(new File("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/Obama/5.png"));				
		label1 = new JLabel(new ImageIcon("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/Obama/5.png"));
		
		
    	//DisplayUtilities.display(image4);
		DoubleFV testFeature2 = eigen.extractFeature(image4);
        String bestPerson = null;
        double minDistance = Double.MAX_VALUE;
        System.out.println(features.keySet().size());
        for (final String person1 : features.keySet()) {
            for (final DoubleFV fv : features.get(person1)) {
            	if(fv==null) {
            		continue;
            	}
                
                double distance = fv.compare(testFeature2, DoubleFVComparison.EUCLIDEAN);
                
                if (distance < minDistance) {
                    minDistance = distance;
                    bestPerson = person1;
                }
            }
        }
        System.out.println("\tguess: " + bestPerson+ "\t: " + (int)((20/(20+minDistance))*100)+ "% ");
        label3.setText("\tguess: " + bestPerson+ "\t: " + (int)((20/(20+minDistance))*100)+ "% ");
        
        //test Mike's face
		FImage image3 = ImageUtilities.readF(new File("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/JokeTest/3.jpg"));	
		label2 = new JLabel(new ImageIcon("/Users/Phoenix/OpenIMAJ-Phoenix/src/main/java/ok/JokeTest/3.jpg"));
		
		
    	//DisplayUtilities.display(image3);
		testFeature2 = eigen.extractFeature(getface(image3).get(0));
		bestPerson = null;
		minDistance = Double.MAX_VALUE;
		System.out.println(features.keySet().size());
		for (final String person1 : features.keySet()) {
		    for (final DoubleFV fv : features.get(person1)) {
				if(fv==null) {
					continue;
				}
				double distance = fv.compare(testFeature2, DoubleFVComparison.EUCLIDEAN);
				if (distance < minDistance) {
				    minDistance = distance;
				    bestPerson = person1;
				}
		    }
		}
		System.out.println("\tguess: " + bestPerson+ "\t: " + (int)((20/(20+minDistance))*100)+ "% ");				
	    label4.setText("\tguess: " + bestPerson+ "\t: " + (int)((20/(20+minDistance))*100)+ "% ");
	
	    
	    panel.add(label1);
	    panel.add(label3);
	    panel.add(label2);
	    panel.add(label4);
	    frame.add(panel);
	    frame.setSize(800, 600);
	    frame.setLocationRelativeTo(null);
		// this.setLocation(600, 600);
		// this.pack();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);		
		
	}
	
	public static List <FImage> getface( FImage imageee ) {
		FaceDetector <DetectedFace,FImage> displays = new HaarCascadeDetector(40);
    	List <DetectedFace> faces = displays.detectFaces(imageee);
    	List <FImage> result = new ArrayList<>();
    	for(DetectedFace face:faces){
    		int x = (int) face.getBounds().x;
    		int y = (int) face.getBounds().y;
    		int w = (int) face.getBounds().height;
    		int h = (int) face.getBounds().width; 		
    		FImage clone2 = new FImage(w,h);
    		for( int i = 0; i<w; i++ ) {
    			for( int j = 0 ; j<h; j++ ) {
    				clone2.pixels[j][i] = imageee.getPixel(x+i+1, y+j+1);
    				}
    		}
    		result.add(ResizeProcessor.resample(clone2, 92, 112));
    	}
		return result;
	}
}


