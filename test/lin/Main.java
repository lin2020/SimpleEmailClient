import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        File f = new File("C:/Users/jiadong/Desktop/test1.png");
        File f2 = new File("C:/Users/jiadong/Desktop/test2.png");
        FileInputStream in = new FileInputStream(f);
        FileOutputStream out = new FileOutputStream(f2);
        byte[] buffer = new byte[1024];
        int byteRead = 0;
        while ((byteRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, byteRead);
            for (byte b : buffer) {
                System.out.print(b);
            }
        }
    }
}
