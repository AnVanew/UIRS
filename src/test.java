import java.io.BufferedWriter;
import java.io.FileWriter;

public class test {

  public static void main(String[] args) throws Exception {
    coordinates coordinates;

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("result.txt"))) {
      for (double j = 0, m = 0, o = 0, w = 0; j <= 2 * Math.PI;
          j += 2 * Math.PI / 100, m += 2 * Math.PI / 200, o += 2 * Math.PI / 100, w +=
              2 * Math.PI / 200) {
        coordinates = new coordinates(130000, 0.0549, 0.005, o, w, m);

        double[] x = coordinates.coordinates(j);

        String str = String.format("%20f %20f %20f\n", x[0], x[1], x[2]);
        writer.write(str);

        coordinates.orbitElem(x);
      }
    }


  }
}
