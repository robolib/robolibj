

package io.github.robolib.module.sensor.mpu6050;

/**
 * 
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class VectorDouble {
    public double x;
    public double y;
    public double z;

    public VectorDouble() {
        x = 0;
        y = 0;
        z = 0;
    }
    
    public VectorDouble(double nx, double ny, double nz) {
        x = nx;
        y = ny;
        z = nz;
    }

   public double getMagnitude() {
        return Math.sqrt(x*x + y*y + z*z);
    }

    public void normalize() {
        double m = getMagnitude();
        x /= m;
        y /= m;
        z /= m;
    }
    
    public VectorDouble getNormalized() {
        VectorDouble r = new VectorDouble(x, y, z);
        r.normalize();
        return r;
    }
    
    public void rotate(Quaternion q) {
        Quaternion p = new Quaternion(0, x, y, z);

        // quaternion multiplication: q * p, stored back in p
        p = q.getProduct(p);

        // quaternion multiplication: p * conj(q), stored back in p
        p = p.getProduct(q.getConjugate());

        // p quaternion is now [0, x', y', z']
        x = p.x;
        y = p.y;
        z = p.z;
    }

    public VectorDouble getRotated(Quaternion q) {
        VectorDouble r = new VectorDouble(x, y, z);
        r.rotate(q);
        return r;
    }
}