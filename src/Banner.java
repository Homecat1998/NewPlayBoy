import java.awt.*;

/**
 * Sphere - this is the basic element that makes up a scene.
 * Eric McCreath 2009
 */


public class Banner extends Item {
	P3D position;
	P3D topvector;
	P3D sidevector;
	P3D normal;

	Texture texture;
	Color color;

	public Banner(P3D pos, P3D topv, P3D sidev,P3D n, Color color) {
		super();
		this.position=pos;
		this.topvector=topv;
		this.sidevector=sidev;
		this.normal=n;
		this.color = color;
		this.texture=null;
	}
	public Banner(P3D pos, P3D topv, P3D sidev,P3D n, Texture t) {
		super();
		this.position=pos;
		this.topvector=topv;
		this.sidevector=sidev;
		this.normal=n;
		this.color = null;
		this.texture=t;
	}
	
	
	// intersect - returns either intersect object or null if the ray misses
	public Intersect intersect(Ray ray) {
		double top=-(ray.position.sub(position).dot(normal));
		double bottom=ray.direction.dot(normal);

		if (bottom==0.0 ) return null;

		double u=top/bottom;

		P3D hit=ray.position.add(ray.direction.scale(u));
		P3D W=hit.sub(position);

		if ((W.x <= topvector.x && W.x >= 0) && (W.y < sidevector.y && W.y > 0)){
			if (texture!=null){
				double tu=W.x / topvector.x;
				double tv=W.y / sidevector.y;
				return new Intersect(u,hit,normal,this,texture.lookup(tu,tv));
//				return new Intersect(u,hit,normal,this,color);
			}
			return new Intersect(u,hit,normal,this,color);
//			return null;
		}
		else {
			return null;
		}
	}
	
	public String toString() {
		return "Banner at : " + position;
	}
	
}
