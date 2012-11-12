#version 150
#define MAX_LIGHTS 8
// GLSL version 1.50
// Fragment shader for diffuse shading in combination with a texture map

// Uniform variables passed in from host program
uniform sampler2D myTexture;
uniform vec3 pointLightsCol[MAX_LIGHTS];

// Variables passed in from the vertex shader
in float ndotl;
in float specularLight[MAX_LIGHTS];
in vec2 frag_texcoord;

// Output variable, will be written to framebuffer automatically
out vec4 frag_shaded;

void main()
{		
	// The built-in GLSL function "texture" performs the texture lookup
	frag_shaded = ndotl * texture(myTexture, frag_texcoord);
	for (int i = 0; i < MAX_LIGHTS; i++) {
		frag_shaded += specularLight[i] * vec4(pointLightsCol[i], 1);
	}
}
