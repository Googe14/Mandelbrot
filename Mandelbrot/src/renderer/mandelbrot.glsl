#version 430

layout(local_size_x = 32,local_size_y = 1) in;
layout(rgba32f, binding = 0) uniform image2D img_out;

uniform double zoom;
uniform double xd;
uniform double yd;

uniform int samples;

uniform dvec2 res;


vec3[] pal = vec3[](
		vec3(0, 0, 0),
		vec3(1, 0, 0),
		vec3(1, 1, 0),
		vec3(0, 0, 1)
//		vec3(0, 0, 1),
//		vec3(1, 0, 1),
//		vec3(1, 1, 1),
//		vec3(0, 1, 0),
//		vec3(0, 1, 1)
//		vec3(0, 0, 0)
);
int smoothFact = 100;



ivec2 pixCoords = ivec2(gl_GlobalInvocationID.x*gl_GlobalInvocationID.z, gl_GlobalInvocationID.y);
void main(void) {

	double z2 = 2*zoom;

	double x = (double(pixCoords.x)/res.x)*z2 + xd - zoom;
	double y = (double(pixCoords.y)/res.y)*z2 + yd - zoom;

	int itr = 0;

	double r = x;
	double i = y;

	while(r*r + i*i < 1 && itr < samples) {

		double tempr = r*r - i*i;
		double tempi = r*i*2;

		r = tempr + x;
		i = tempi + y;

		itr++;
	}



	int ii = itr / smoothFact;
	int j = ii % pal.length;

	vec3 min = pal[j];
	if(j == pal.length-1) {
		j = -1;
	}
	j++;
	vec3 max = pal[j];

	vec3 diff = max-min;

	vec3 col = min + ((itr%smoothFact)/float(smoothFact)) * diff;

	vec4 pixel = vec4(col.xyz, 1);

	imageStore(img_out, pixCoords, pixel);

}
