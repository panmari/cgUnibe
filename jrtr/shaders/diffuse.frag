#version 150
#define MAX_LIGHTS 8
// GLSL version 1.50
// Fragment shader for diffuse shading in combination with a texture map

// Uniform variables passed in from host program
uniform sampler2D myTexture;
uniform sampler2D glossMap;
uniform vec3 pointLightsCol[MAX_LIGHTS];
uniform float specularReflectionCoefficient;
uniform float phongExponent;

// Variables passed in from the vertex shader
in float ndotl;
in float relativeRadiance[MAX_LIGHTS];
in vec3 e;
in vec3 R[MAX_LIGHTS];
in float diffuseLight[MAX_LIGHTS];
in vec2 frag_texcoord;

// Output variable, will be written to framebuffer automatically
out vec4 frag_shaded;

void main()
{		
	// The built-in GLSL function "texture" performs the texture lookup
	vec4 ambColor = ndotl * texture(myTexture, frag_texcoord);
	vec4 specColor = vec4(0,0,0,0);
	vec4 diffColor = vec4(0,0,0,0);
	float rfc = specularReflectionCoefficient;
	//float rfc = texture(glossMap, frag_texcoord).x;
 	
	for (int i = 0; i < MAX_LIGHTS; i++) {
		diffColor += diffuseLight[i] * texture(myTexture, frag_texcoord);
		float specular = pow(max(dot(R[i],e), 0), phongExponent);
		float specularLight = relativeRadiance[i] * rfc * specular; //TODO use glossmap value
		specColor += vec4(pointLightsCol[i], 0) * specularLight;
	}
	vec4 finalColor = specColor + ambColor + diffColor;
	frag_shaded = finalColor;
}
