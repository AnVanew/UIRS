import java.io.BufferedWriter;
import java.io.FileWriter;

public class test {

  public static void main(String[] args) throws Exception {
    coordinates coordinates;

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("result.txt"))) {
      for (double j = 0, i = 0, o = 0, w = 0; j <= 2 * Math.PI;
          j += 2 * Math.PI / 100, i += 2 * Math.PI / 200, o += 2 * Math.PI / 200, w += 2 * Math.PI / 200) {
        coordinates = new coordinates(1300000, 0.0004, i, o, w, 0);

        double[] x = coordinates.coordinates(j);

        writer.write(x[0] + " " + x[1] + " " + x[2] + "\n");

        coordinates.orbitElem(x);
      }
    }


  }
}
