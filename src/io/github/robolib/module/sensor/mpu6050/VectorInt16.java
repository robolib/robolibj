

package io.github.robolib.module.sensor.mpu6050;

/**
 * 
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class VectorInt16 {
    public short x;
    public short y;
    public short z;

    public VectorInt16() {
        x = 0;
        y = 0;
        z = 0;
    }
    
    public VectorInt16(short nx, short ny, short nz) {
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
    
    public VectorInt16 getNormalized() {
        VectorInt16 r = new VectorInt16(x, y, z);
        r.normalize();
        return r;
    }
    
    public void rotate(Quaternion q) {
        // http://www.cprogramming.com/tutorial/3d/quaternions.html
        // http://www.euclideanspace.com/maths/algebra/realNormedAlgebra/quaternions/transforms/index.htm
        // http://content.gpwiki.org/index.php/OpenGL:Tutorials:Using_Quaternions_to_represent_rotation
        // ^ or: http://webcache.googleusercontent.com/search?q=cache:xgJAp3bDNhQJ:content.gpwiki.org/index.php/OpenGL:Tutorials:Using_Quaternions_to_represent_rotation&hl=en&gl=us&strip=1
    
        // P_out = q * P_in * conj(q)
        // - P_out is the output vector
        // - q is the orientation quaternion
        // - P_in is the input vector (a*aReal)
        // - conj(q) is the conjugate of the orientation quaternion (q=[w,x,y,z], q*=[w,-x,-y,-z])
        Quaternion p = new Quaternion(0, x, y, z);

        // quaternion multiplication: q * p, stored back in p
        p = q.getProduct(p);

        // quaternion multiplication: p * conj(q), stored back in p
        p = p.getProduct(q.getConjugate());

        // p quaternion is now [0, x', y', z']
        x = (short) p.x;
        y = (short) p.y;
        z = (short) p.z;
    }

    public VectorInt16 getRotated(Quaternion q) {
        VectorInt16 r = new VectorInt16(x, y, z);
        r.rotate(q);
        return r;
    }
}