import java.util.HashMap;

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


    System.out.println("w1 = "+w);
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

  public HashMap orbitElem(double[] x, double t) {
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
    //System.out.println("a:" + a);

    double e = sqrt(pow((pow(V0 / Vc, 2) - 1), 2) + r0 / a * pow(V0 / Vc, 2) * pow(
        (X * dX + Y * dY + Z * dZ) / (r0 * V0), 2));
    //System.out.println("e:" + e);

    double p = a*(1-e*e);

    double v0 = atan( (X*dX+Y*dY+Z*dZ) * sqrt(p) / (p-r0) );
    if ( t>=PI) v0 = PI + v0;
    if (v0<0) v0 = PI + v0;
    //System.out.println(v0);

    double i = acos((X*dY-Y*dX)/sqrt(p*1));  //1 is equal mu

    //double O = asin( (Y*dZ - Z*dY) / (sqrt(p*1)*sin(i)) ); //1 is equal mu
    double O = acos( -1*(Z*dX - X*dZ) / (sqrt(p*1)*sin(i)) ); //1 is equal mu
    if(t>PI)O=2*PI-O;
    //System.out.println("O2 = "+O +"   t= "+t);

    double w = atan( Z / ( sin(i) * (X*cos(O)+Y*sin(O)) ) );
    if (t>=PI/2 && t<PI*3/2)w=PI+w;
    if (t>=3*PI/2)w = 2*PI+w;
    System.out.println("w2= "+w +" t= "+t);




    System.out.println();
    return null;
  }


}
