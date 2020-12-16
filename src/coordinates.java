import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import java.util.HashMap;

public class coordinates {

  //неизменные параметры орбиты
  private final double a, e, i, O, w, M0;

  public coordinates(double a, double e, double i, double o, double w, double m0) {
    this.a = a;
    this.e = e;
    this.i = i;
    O = o;
    this.w = w;
    M0 = m0;
  }

  // используем интерфейс Kepler для реализации полиморфима
  // (мы не знаем, какой метод будет реализовывать решение уравнения Кеплера)
  private Kepler resultKepler = new dihotomKepler();

  //Метод, возвращающий массив координат в определленное время
  double[] coordinates(double t) {

    //Вычисление входных параметров по соответствующим формулам

    double n = Math.sqrt(1 / Math.pow(a, 3));
//    double n = 1;
    double M = n * t + M0;
    double E = resultKepler.resultKepler(M, e);
    double v = 2 * atan(Math.sqrt((1 + e) / (1 - e)) * Math.tan(E / 2));
    double r = a * (1 - e * e) / (1 + e * Math.cos(v));
    double u = v + w;

    double[] x = new double[6];

    x[0] = r * (cos(u) * cos(O) - sin(u) * sin(O) * cos(i));
    x[1] = r * (cos(u) * sin(O) + sin(u) * cos(O) * cos(i));
    x[2] = r * sin(u) * sin(i);

    double p = a * (1 - e * e);
    double kor = sqrt(1 / p);
    double vr = kor * e * sin(v);
    double vn = kor * (1 + e * cos(v));

    x[3] = x[0] * vr / r + (-sin(u) * cos(O) - cos(u) * sin(O) * cos(i)) * vn; //dx
    x[4] = x[1] * vr / r + (-sin(u) * sin(O) + cos(u) * cos(O) * cos(i)) * vn; //dy
    x[5] = x[2] * vr / r + cos(u) * sin(i) * vn; //dz

    return x;
  }

  //Метод, возвращающий HashMap параметров орбиты по входному массиву координат
//  public Map<String, Double> orbitElemCalc(double[] x, double t) {
//
//    Map<String, Double> result = new LinkedHashMap<>();
//
//    double r = sqrt(x[0] * x[0] + x[1] * x[1] + x[2] * x[2]);
//    result.put("r", r);
//    double u = PI - asin(x[2] / (sin(i) * r));
//
//    result.put("u", u);
//    double v = u - w;
//    result.put("v", v);
//    double E = 2 * Math.atan(Math.tan(v / 2) / Math.sqrt((1 + e) / (1 - e)));
//
//    result.put("E", E);
//    double M = E - e * Math.sin(E);
//    result.put("M", M);
//
//    return result;
//  }

  public HashMap orbitElem(double[] x) {
    double X = x[0];
    double Y = x[1];
    double Z = x[2];
    double dX = x[3];
    double dY = x[4];
    double dZ = x[5];

    double V0 = sqrt(dX * dX + dY * dY + dZ * dZ);
    double r0 = sqrt(X * X + Y * Y + Z * Z);
    double Vc = sqrt(398603.0E9 / r0);

//    double a = r0 / (2 - pow((V0 / Vc), 2)); // из учебника
    double a = 1 / (2 / r0 - V0 * V0 / 1); //из википедии формула, из учебника не сходится
//    System.out.println(a);

//    double e = sqrt(pow((V0 * V0 / Vc / Vc - 1), 2) + r0 / a * pow(V0 / Vc, 2) * pow(
//        (X * dX + Y * dY + Z * dZ) / (r0 * V0), 2));

    double e0 = abs(pow(V0 / Vc, 2) - 1);
    double e = pow(e0, 2) + (1 - pow(e0, 2)) * pow((X * dX + Y * dY + Z * dZ) / (r0 * V0), 2);

    System.out.println(e);

    return null;
  }


}
