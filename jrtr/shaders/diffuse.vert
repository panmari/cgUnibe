#version 150
#pragma optionNV unroll all
#define MAX_LIGHTS 8
// GLSL version 1.50 
// Vertex shader for diffuse shading in combination with a texture map

// Uniform variables, passed in from host program via suitable 
// variants of glUniform*
uniform mat4 projection;
uniform mat4 modelview;
uniform mat4 camera;
uniform vec4 lightDirection;
// positions of lights are in world space!
uniform vec3 pointLightsPos[MAX_LIGHTS];
uniform float diffuseReflectionCoefficient;

// Input vertex attributes; passed in from host program to shader
// via vertex buffer objects
in vec3 normal;
in vec4 position;
in vec2 texcoord;

// Output variables for fragment shader
out vec3 L[MAX_LIGHTS];
out vec3 normalCameraSpace;
out vec3 e;
out float ndotl;
out vec2 frag_texcoord;

void main()
{		
	// Compute dot product of normal and light direction
	// and pass color to fragment shader
	// Note: here we assume "lightDirection" is specified in camera coordinates,
	// so we transform the normal to camera coordinates, and we don't transform
	// the light direction, i.e., it stays in camera coordinates
	normalCameraSpace = normalize((modelview * vec4(normal,0)).xyz);

	//we're in camera space, cop is always (0,0,0) => just take the negative	
	e = - normalize((modelview * position).xyz);

	for (int i = 0; i < MAX_LIGHTS; i++) {
		L[i] = (camera * vec4(pointLightsPos[i].xyz, 1) - modelview * position).xyz;
	}
	
	//ambient light:
	// according to script: diffuse = ambient coeff
	ndotl = diffuseReflectionCoefficient*max(dot(modelview * vec4(normal,0), lightDirection),0);
	
	// Pass texture coordiantes to fragment shader, OpenGL automatically
	// interpolates them to each pixel  (in a perspectively correct manner) 
	frag_texcoord = texcoord;

	// Transform position, including projection matrix
	// Note: gl_Position is a default output variable containing
	// the transformed vertex position
	gl_Position = projection * modelview * position;
}
