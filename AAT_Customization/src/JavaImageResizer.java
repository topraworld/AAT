import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class JavaImageResizer {
	
    public static void main(String[] args) throws IOException {
    	
    	String from = "C:/Users/Chathuranga/Desktop/from/2014/";
    	String to = "C:/Users/Chathuranga/Desktop/2014/";
    	
        File folder = new File(from);
        File[] listOfFiles = folder.listFiles();
        System.out.println("Total No of Files:"+listOfFiles.length);
        BufferedImage img = null;
        BufferedImage tempPNG = null;
        BufferedImage tempJPG = null;
        File newFilePNG = null;
        File newFileJPG = null;
        for (int i = 0; i < listOfFiles[i].length() -1; i++) {
              if (listOfFiles[i].isFile()) {
            	  
            	  try{
            		  System.out.println("File " + listOfFiles[i].getName());
                      img = ImageIO.read(new File(from+listOfFiles[i].getName()));
                      tempJPG = resizeImage(img, img.getWidth(), img.getHeight());
                      newFileJPG = new File(to+listOfFiles[i].getName());
                      ImageIO.write(tempJPG, "jpg", newFileJPG);
                      new File(from+listOfFiles[i].getName()).delete();
              		
              	}catch(Exception ex){
              		
              	}  
            	  
                
              }
        }
        System.out.println("DONE");
    }

    /**
     * This function resize the image file and returns the BufferedImage object that can be saved to file system.
     */
        public static BufferedImage resizeImage(final Image image, int width, int height) {
    int targetw = 0;
    int targeth = 180;

    if (width > height)targetw = 80;
    else targetw = 130;

    do {
        if (width > targetw) {
            width /= 2;
            if (width < targetw) width = targetw;
        }

        if (height > targeth) {
            height /= 2;
            if (height < targeth) height = targeth;
        }
    } while (width != targetw || height != targeth);

    final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    final Graphics2D graphics2D = bufferedImage.createGraphics();
    graphics2D.setComposite(AlphaComposite.Src);
    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
    graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    graphics2D.drawImage(image, 0, 0, width, height, null);
    graphics2D.dispose();

    return bufferedImage;
    }
 }