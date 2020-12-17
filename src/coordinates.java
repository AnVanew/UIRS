import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.*;

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

    double p = a * (1 - pow(e, 2));
    double kor = sqrt(1 / p);
    double vr = kor * e * sin(v);
    double vn = kor * (1 + e * cos(v));

    x[3] = x[0] * vr / r + (-sin(u) * cos(O) - cos(u) * sin(O) * cos(i)) * vn; //dx
    x[4] = x[1] * vr / r + (-sin(u) * sin(O) + cos(u) * cos(O) * cos(i)) * vn; //dy
    x[5] = x[2] * vr / r + cos(u) * sin(i) * vn; //dz


    System.out.println("M01 = "+M0);
    return x;
  }

  public Map orbitElem(double[] x, double t) {

    Map <String, Double> orrbitElem = new HashMap<>();

    double X = x[0];
    double Y = x[1];
    double Z = x[2];
    double dX = x[3];
    double dY = x[4];
    double dZ = x[5];

    double V0 = sqrt(dX * dX + dY * dY + dZ * dZ);
    double r0 = sqrt(X * X + Y * Y + Z * Z);
    double Vc = sqrt(1 / r0); //мю = 1

    double a = r0 / (2 - pow((V0 / Vc), 2)); // из учебника

    double e = sqrt(pow((pow(V0 / Vc, 2) - 1), 2) + r0 / a * pow(V0 / Vc, 2) * pow(
        (X * dX + Y * dY + Z * dZ) / (r0 * V0), 2));

    double p = a*(1-e*e);

    double v0 = atan( (X*dX+Y*dY+Z*dZ) * sqrt(p) / (p-r0) );
    if (v0<0) v0 = PI + v0;

    double i = acos((X*dY-Y*dX)/sqrt(p*1));  //1 is equal mu

    double O = acos( -1*(Z*dX - X*dZ) / (sqrt(p*1)*sin(i)) ); //1 is equal mu

    double w = atan( Z / ( sin(i) * (X*cos(O)+Y*sin(O)) ) );
    if (t>=PI/2 && t<PI*3/2)w=PI+w;
    if (t>=3*PI/2)w = 2*PI+w;

    double M0 = t * sqrt(1/(a*a*a));
    System.out.println(M0);
    orrbitElem.put("a", a);
    orrbitElem.put("e", e);
    orrbitElem.put("i", i);
    orrbitElem.put("O", O);
    orrbitElem.put("w", w);

    return orrbitElem;
  }


}
