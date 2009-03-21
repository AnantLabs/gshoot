import java.io.IOException;
import java.io.File;

public class RunOnce {
    public void test() throws IOException {
        File f = new File("fixtures/unknow.txt");
        System.out.println(f.lastModified());
    }

    public static void main(String[] args) throws IOException {
        RunOnce test = new RunOnce();
        test.test();
    }
}
