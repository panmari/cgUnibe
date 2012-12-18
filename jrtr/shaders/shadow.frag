#version 150
#define MAX_LIGHTS 8
// GLSL version 1.50
// Fragment shader for diffuse shading in combination with a texture map

// Uniform variables passed in from host program
uniform sampler2D myTexture;
uniform sampler2D glossMap;
uniform vec3 lightPos;

uniform mat4 camera;

in vec4 posCameraSpace;

// Output variable, will be written to framebuffer automatically
out vec4 frag_shaded;

void main()
{	
	float dist = distance(posCameraSpace.xyz, vec3(0,0,0));
	frag_shaded = vec4(dist,0,0,0);
}
