/**
 * Created by samuelerb on 2019-05-11.
 * Matr_nr: s0556350
 * Package: PACKAGE_NAME
 */
public class Test {
    public static void main(String[] args) {
        String test = "Hallo \n Test";
        test = test.replace("\n", "").replace("\r", "");
        System.out.println(test);
    }
}
