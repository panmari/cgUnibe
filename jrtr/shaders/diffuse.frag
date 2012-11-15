#version 150
#define MAX_LIGHTS 8
// GLSL version 1.50
// Fragment shader for diffuse shading in combination with a texture map

// Uniform variables passed in from host program
uniform sampler2D myTexture;
uniform sampler2D glossMap;
uniform vec3 pointLightsCol[MAX_LIGHTS];
uniform float pointLightsRad[MAX_LIGHTS];

uniform float diffuseReflectionCoefficient;
uniform float specularReflectionCoefficient;
uniform float phongCoefficient;

// Variables passed in from the vertex shader
in float ndotl;
in float relativeRadiance[MAX_LIGHTS];
in vec3 e;
in vec3 normalCameraSpace;
in vec3 L[MAX_LIGHTS];
in vec2 frag_texcoord;

// Output variable, will be written to framebuffer automatically
out vec4 frag_shaded;

void main()
{		
	// The built-in GLSL function "texture" performs the texture lookup
	vec4 ambColor = ndotl * texture(myTexture, frag_texcoord);
	vec4 specColor = vec4(0,0,0,0);
	vec4 diffColor = vec4(0,0,0,0);
	for (int i = 0; i < MAX_LIGHTS; i++) {
		float nxDir = max(0.0, dot(normalCameraSpace, normalize(L[i])));
		float relativeRadiance = pointLightsRad[i]/dot(L[i], L[i]);
		float diffuseLight = relativeRadiance * diffuseReflectionCoefficient * nxDir;

		vec3 R = 2 * dot(normalize(L[i]), normalCameraSpace) * normalCameraSpace - normalize(L[i]);
		diffColor += diffuseLight * texture(myTexture, frag_texcoord);
		float specular = pow(max(dot(R,e), 0), phongCoefficient);
		float specularLight = relativeRadiance * specularReflectionCoefficient * max(specular, 0); //TODO use glossmap value
		specColor += vec4(pointLightsCol[i], 0) * specularLight;
	}
	vec4 finalColor = specColor + ambColor + diffColor;
	frag_shaded = finalColor;
}
