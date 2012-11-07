#version 150
#define MAX_LIGHTS 8
// GLSL version 1.50 
// Vertex shader for diffuse shading in combination with a texture map

// Uniform variables, passed in from host program via suitable 
// variants of glUniform*
uniform mat4 projection;
uniform mat4 transform;
uniform mat4 camera;
uniform vec4 lightDirection;
uniform vec3 pointLightsPos[MAX_LIGHTS];
uniform vec3 pointLightsCol[MAX_LIGHTS];
uniform float pointLightsRad[MAX_LIGHTS];
uniform float diffuseReflectionCoefficient;
// Input vertex attributes; passed in from host program to shader
// via vertex buffer objects
in vec3 normal;
in vec4 position;
in vec2 texcoord;

// Output variables for fragment shader
out float ndotl;
out vec2 frag_texcoord;

void main()
{		
	// Compute dot product of normal and light direction
	// and pass color to fragment shader
	// Note: here we assume "lightDirection" is specified in camera coordinates,
	// so we transform the normal to camera coordinates, and we don't transform
	// the light direction, i.e., it stays in camera coordinates
	// ndotl = max(dot(modelview * vec4(normal,0), lightDirection),0);
	for (int i = 0; i < MAX_LIGHTS; i++) {
		vec3 L = pointLightsPos[i].xyz - (transform*position).xyz;
		float relativeRadiance =  pointLightsRad[i]/dot(L,L);
		float nxDir = max(0.0, dot((transform * vec4(normal, 0)).xyz,  normalize(L)));
		ndotl += relativeRadiance*diffuseReflectionCoefficient*nxDir;
	}
	// Pass texture coordiantes to fragment shader, OpenGL automatically
	// interpolates them to each pixel  (in a perspectively correct manner) 
	frag_texcoord = texcoord;

	// Transform position, including projection matrix
	// Note: gl_Position is a default output variable containing
	// the transformed vertex position
	gl_Position = projection * camera * transform * position;
}
