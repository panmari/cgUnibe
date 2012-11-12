#version 150
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
uniform vec3 pointLightsCol[MAX_LIGHTS];
uniform float pointLightsRad[MAX_LIGHTS];
uniform float diffuseReflectionCoefficient;
uniform float specularReflectionCoefficient;
uniform float phongCoefficient;
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
	// ndotl 
	
	for (int i = 0; i < MAX_LIGHTS; i++) {
		vec3 L = (camera * vec4(pointLightsPos[i].xyz, 1) - modelview * position).xyz;
		float nxDir = max(0.0, dot((modelview * vec4(normal,0)).xyz, normalize(L)));
		float relativeRadiance =  pointLightsRad[i]/dot(L, L);
		ndotl += relativeRadiance*diffuseReflectionCoefficient*nxDir;
	}
	
	//ambient light:
	ndotl += max(dot(modelview * vec4(normal,0), lightDirection),0);
	
	// Pass texture coordiantes to fragment shader, OpenGL automatically
	// interpolates them to each pixel  (in a perspectively correct manner) 
	frag_texcoord = texcoord;

	// Transform position, including projection matrix
	// Note: gl_Position is a default output variable containing
	// the transformed vertex position
	gl_Position = projection * modelview * position;
}
