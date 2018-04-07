import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class LoggingDemo {
     public static void main(String[] args) {
         Logger logger = Logger.getLogger("LoggingDemo");
         try {
        	 	FileHandler fileHandler = new FileHandler("./test.txt");
        	 	fileHandler.setFormatter(new SimpleFormatter());
             logger.addHandler(fileHandler);
             logger.info("测试信息");
         } catch (SecurityException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
 }