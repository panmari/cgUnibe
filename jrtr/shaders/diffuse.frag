#version 150
#define MAX_LIGHTS 8
// GLSL version 1.50
// Fragment shader for diffuse shading in combination with a texture map

// Uniform variables passed in from host program
uniform sampler2D myTexture;
uniform sampler2D glossMap;
uniform sampler2DShadow shadowMap;

uniform mat4 camera;

// positions of lights are in world space!
uniform vec3 pointLightsPos[MAX_LIGHTS];
uniform vec3 pointLightsCol[MAX_LIGHTS];
uniform float pointLightsRad[MAX_LIGHTS];
uniform float specularReflectionCoefficient;
uniform float diffuseReflectionCoefficient;
uniform float phongExponent;

// Variables passed in from the vertex shader
in vec3 normalCameraSpace;
in vec2 frag_texcoord;
in vec4 posCameraSpace;
in vec4 posLightSpace;

// Output variable, will be written to framebuffer automatically
out vec4 frag_shaded;

void main()
{	

	// The built-in GLSL function "texture" performs the texture lookup
	vec4 ambColor = diffuseReflectionCoefficient * texture(myTexture, frag_texcoord);
	vec4 specColor = vec4(0,0,0,0);
	vec4 diffColor = vec4(0,0,0,0);
	float rfc = specularReflectionCoefficient;
	//float rfc = texture(glossMap, frag_texcoord).x;
 	
	for (int i = 0; i < MAX_LIGHTS; i++) {
		vec3 L = (camera * vec4(pointLightsPos[i].xyz, 1) - posCameraSpace).xyz;

		float nxDir = max(0.0, dot(normalCameraSpace, normalize(L)));
		float attenuation = dot(L, L);
		float relativeRadiance = pointLightsRad[i]/attenuation;
		
		float diffuseLight = relativeRadiance * diffuseReflectionCoefficient * nxDir;
		diffColor += diffuseLight * texture(myTexture, frag_texcoord);
		
		vec3 R = reflect(- normalize(L), normalCameraSpace);
		vec3 e = - normalize(posCameraSpace.xyz);
		float specular = pow(max(dot(R,e), 0), phongExponent);
		float specularLight = relativeRadiance * rfc * specular; //TODO use glossmap value
		specColor += vec4(pointLightsCol[i], 0) * specularLight;
	}
	
	float shadowValue = textureProj(shadowMap, posLightSpace, 0);
	vec4 finalColor = specColor + ambColor + diffColor;
	finalColor = finalColor*shadowValue;
	
	if (shadowValue == 1) {
		finalColor = vec4(1,0,0,0);
	}

	frag_shaded = finalColor;
}
