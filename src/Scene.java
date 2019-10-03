import java.awt.Color;
import java.util.ArrayList;

/**
 * Scene - the list of items that make up a scene.
 * 
 * Eric McCreath 2009, 2019
 */

public class Scene extends ArrayList<Item> {

	Color background = Color.blue;

	P3D lightpos =  new P3D(25.0,40.0,-40.0);
	double i_a = 1.0d;
	double i_d = 1.0d;
	double i_s = 1.0d;

	int count =0;

	public Color raytrace(Ray r) {
		
		Double mindis = null;
		Intersect intersect = null;

		
		for (Item s : this) {
			Intersect i = s.intersect(r);
			if (i != null) {
				if (intersect == null || (i.distance < mindis && i.distance > 0.01)) {
					mindis = i.distance;
					intersect = i;
				}
			}
		}
		if (intersect != null) {

			if (intersect.item.mirror && count < 10) {

				count = count + 1;

				P3D newPos = intersect.hitPosition;
				P3D newDirection = intersect.hitNormal.reflect(r.direction.scale(-1.0d));

				System.out.println("new ray" + " with depth " + count);
//				return Color.yellow;
				return raytrace(new Ray(newPos, newDirection));
//				i = i.item.intersect(new Ray(newPos, newDirection));
			} else {

				count = 0;
				Color objectColor = intersect.color;
				P3D hitnormal = intersect.hitNormal;
				P3D pointToLight = lightpos.sub(intersect.hitPosition).normalize();
				P3D k_a = new P3D(objectColor.getRed() / 255.0d, objectColor.getGreen() / 255.0d, objectColor.getBlue() / 255.0d);
				P3D amb = k_a.scale(0.3d);

				P3D k_d = k_a;
				P3D diff = k_d.scale(pointToLight.dot(hitnormal)).scale(0.6d);

				P3D k_s = new P3D(1.0d, 1.0d, 1.0d);
				P3D pointToView = r.position.sub(intersect.hitPosition).normalize();
				P3D reflect = hitnormal.reflect(pointToLight);
				double temp = Math.pow(Math.max(0.0d,pointToView.dot(reflect)), 16.0d);
				P3D spec = k_s.scale(temp).scale(0.8d);

				P3D rtn = amb.add(diff).add(spec);


				System.out.println(amb.add(diff).add(spec).toString());

				float red;
				float green;
				float blue;


				if (rtn.x > 1){
					red = 1.0f;
				} else if (rtn.x < 0){
					red = 0.0f;
				} else {
					red = (float) rtn.x;
				}

				if (rtn.y > 1){
					green = 1.0f;
				} else if (rtn.y < 0){
					green = 0.0f;
				} else {
					green = (float) rtn.y;
				}

				if (rtn.z > 1){
					blue = 1.0f;
				} else if (rtn.z < 0){
					blue = 0.0f;
				} else {
					blue = (float) rtn.z;
				}





				return new Color(red,green,blue);
			}


		} else {
			count = 0;
			return background;
		}
	}

	private double clamp(double r) {
		return (r < 0.0 ? 0.0 : (r > 1.0 ? 1.0 : r));
	}

	

}
