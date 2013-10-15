#version 140

// Fragment shader for pseudo shading using the normal 

in vec4 frag_normal;
in vec4 frag_color;
in vec4 pos_camera_space;

// Output variable, will be written to framebuffer automatically
out vec4 frag_shaded;

void main()
{		
	// Scale color according to z component of camera space normal

	float angle = acos(dot(-pos_camera_space, frag_normal)/(length(pos_camera_space)*length(frag_normal)));
	if (angle > 3.14/2 - 0.7 && angle < 3.14/2 - 0.5) {
		frag_shaded = vec4(0,1,1,0);
	} else {
		frag_shaded = vec4(.1,.1,.1,0);
	}
}
