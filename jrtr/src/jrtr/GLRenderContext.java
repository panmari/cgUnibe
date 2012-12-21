package jrtr;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.TraceGL2;
import javax.media.opengl.TraceGL3;
import javax.vecmath.*;

/**
 * This class implements a {@link RenderContext} (a renderer) using OpenGL version 3 (or later).
 */
public class GLRenderContext implements RenderContext {

	private SceneManagerInterface sceneManager;
	private GL3 gl;
	private GLShader activeShader, defaultShader;
	private GLTexture defaultTexture;
	private GLTexture shadowMap;
	private PointLight light;
	private boolean shadowDraw;
	private IntBuffer shadowMapBuffer;
	/**
	 * This constructor is called by {@link GLRenderPanel}.
	 * 
	 * @param drawable 	the OpenGL rendering context. All OpenGL calls are
	 * 					directed to this object.
	 */
	public GLRenderContext(GLAutoDrawable drawable)
	{
		gl = drawable.getGL().getGL3();
		gl.glEnable(GL3.GL_DEPTH_TEST);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        // Load and use default shader
        defaultShader = new GLShader(gl);
        defaultTexture = new GLTexture(gl);
        shadowMap = new GLTexture(gl);
        try {
        	defaultShader.load("../jrtr/shaders/diffuse.vert","../jrtr/shaders/diffuse.frag");
        	defaultTexture.load("../jrtr/textures/wood.jpg");
        } catch(Exception e) {
	    	System.out.print("Problem with shader:\n");
	    	System.out.print(e.getMessage());
	    }
        defaultShader.use();	  
        activeShader = defaultShader;	
	}
	
	private void fillTuple3fIntoArray(int i, Tuple3f l, float[] array) {
		array[3*i] = l.x;
		array[3*i + 1] = l.y;
		array[3*i + 2] = l.z;
	}
	/**
	 * Set the scene manager. The scene manager contains the 3D
	 * scene that will be rendered. The scene includes geometry
	 * as well as the camera and viewing frustum.
	 */
	public void setSceneManager(SceneManagerInterface sceneManager)
	{
		this.sceneManager = sceneManager;
	}
	
	/**
	 * This method is called by the GLRenderPanel to redraw the 3D scene.
	 * The method traverses the scene using the scene manager and passes
	 * each object to the rendering method.
	 */
	public void display(GLAutoDrawable drawable)
	{
		gl = drawable.getGL().getGL3();
		
		Iterator<PointLight> lightIter = sceneManager.lightIterator();
		
		SceneManagerIterator iterator = sceneManager.iterator();

		if (lightIter.hasNext()) {
			this.light = lightIter.next();
			shadowDraw = true;
		
            this.gl.glActiveTexture(GL3.GL_TEXTURE0 + 2);
            this.shadowMapBuffer = IntBuffer.allocate(1);
            this.gl.glGenTextures(1, this.shadowMapBuffer);
            this.gl.glBindTexture(GL3.GL_TEXTURE_2D,
                    this.shadowMapBuffer.get(0));
            this.gl.glTexImage2D(GL3.GL_TEXTURE_2D, 0, GL3.GL_DEPTH_COMPONENT,
                    500, 500, 0, GL3.GL_DEPTH_COMPONENT, GL3.GL_UNSIGNED_BYTE,
                    null);
            
            this.gl.glTexParameteri(GL3.GL_TEXTURE_2D,
                    GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_NEAREST);
            this.gl.glTexParameteri(GL3.GL_TEXTURE_2D,
                    GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_NEAREST);
            this.gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_WRAP_S,
                    GL3.GL_CLAMP_TO_EDGE);
            this.gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_WRAP_T,
                    GL3.GL_CLAMP_TO_EDGE);
            gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_COMPARE_MODE, GL3.GL_COMPARE_REF_TO_TEXTURE);
            //Shadow comparison should be true (ie not in shadow) if r<=texture^M
            gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_COMPARE_FUNC, GL3.GL_LEQUAL);
            gl.glTexParameteri(GL3.GL_TEXTURE_2D, TraceGL2.GL_DEPTH_TEXTURE_MODE, TraceGL2.GL_INTENSITY);

            beginFrame();

            this.gl.glViewport(0, 0, 500, 500);
            while (iterator.hasNext()) {
                RenderItem r = iterator.next();
                if (r != null && r.getShape() != null)
                    draw(r);
            }

            endFrame();

            this.gl.glBindTexture(GL.GL_TEXTURE_2D, this.shadowMapBuffer.get(0));
            this.gl.glCopyTexImage2D(GL.GL_TEXTURE_2D, 0, GL3.GL_DEPTH_COMPONENT, 0, 0, 500, 500, 0);

  
            
            int id = this.gl.glGetUniformLocation(
                    this.activeShader.programId(), "shadowMap");
            this.gl.glUniform1i(id, 2);
		}
		
		shadowDraw = false;
		beginFrame();
		iterator = sceneManager.iterator();	
		while(iterator.hasNext())
		{
			RenderItem r = iterator.next();
			if(r != null && r.getShape()!=null) draw(r);
		}		
		
		endFrame();
	}

	/**
	 * This method is called at the beginning of each frame, i.e., before
	 * scene drawing starts.
	 */
	private void beginFrame()
	{
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT);
        gl.glClear(GL3.GL_DEPTH_BUFFER_BIT);
	}

	/**
	 * This method is called at the end of each frame, i.e., after
	 * scene drawing is complete.
	 */
	private void endFrame()
	{
        gl.glFlush();		
	}
	
	/**
	 * Convert a Matrix4f to a float array in column major ordering,
	 * as used by OpenGL.
	 */
	private float[] matrix4fToFloat16(Matrix4f m)
	{
		float[] f = new float[16];
		for(int i=0; i<4; i++)
			for(int j=0; j<4; j++)
				f[j*4+i] = m.getElement(i,j);
		return f;
	}
	
	/**
	 * The main rendering method.
	 * 
	 * @param renderItem	the object that needs to be drawn
	 */
	private void draw(RenderItem renderItem)
	{
		VertexData vertexData = renderItem.getShape().getVertexData();
		LinkedList<VertexData.VertexElement> vertexElements = vertexData.getElements();
		int indices[] = vertexData.getIndices();

		// Don't draw if there are no indices
		if(indices == null) return;
		
		// Set the material
	
		// Compute the modelview matrix by multiplying the camera matrix and the 
		// transformation matrix of the object
		Matrix4f modelview;
		Camera lightCam;
		if (light != null)
			lightCam = new Camera(light.getPosition(), new Point3f(0,10,0), sceneManager.getCamera().getUpVector());
		else lightCam = new Camera();
		
		if (shadowDraw) {
			modelview = new Matrix4f(lightCam.getCameraMatrix());
		}
		else {
			modelview = new Matrix4f(sceneManager.getCamera().getCameraMatrix());
			setMaterial(renderItem.getShape().getMaterial());
		}
		modelview.mul(renderItem.getT());
		
		// Set modelview and projection matrices in shader
		gl.glUniformMatrix4fv(gl.glGetUniformLocation(activeShader.programId(), "modelview"), 1, false, matrix4fToFloat16(modelview), 0);
		gl.glUniformMatrix4fv(gl.glGetUniformLocation(activeShader.programId(), "projection"), 1, false, matrix4fToFloat16(sceneManager.getFrustum().getProjectionMatrix()), 0);
	    
		Matrix4f shadowMapT = new Matrix4f(1/2f,  0,  0,  1/2f,
											0,  1/2f, 0, 1/2f,
											0,    0,  1/2f, 1/2f,
											0,    0,  0 ,   1);
		shadowMapT.mul(sceneManager.getFrustum().getProjectionMatrix());
		shadowMapT.mul(lightCam.getCameraMatrix());
		shadowMapT.mul(renderItem.getT());
		gl.glUniformMatrix4fv(gl.glGetUniformLocation(activeShader.programId(), "shadowMapT"), 1, false, matrix4fToFloat16(shadowMapT), 0);

		// Steps to pass vertex data to OpenGL:
		// 1. For all vertex attributes (position, normal, etc.)
			// Copy vertex data into float buffers that can be passed to OpenGL
			// Make/bind vertex buffer objects
			// Tell OpenGL which "in" variable in the shader corresponds to the current attribute
		// 2. Render vertex buffer objects
		// 3. Clean up
		// Note: Of course it would be more efficient to store the vertex buffer objects (VBOs) in a
		// vertex array object (VAO), and simply bind ("load") the VAO each time to render. But this
		// requires a bit more logic in the rendering engine, so we render every time "from scratch".
		        
        // 1. For all vertex attributes, make vertex buffer objects
        IntBuffer vboBuffer = IntBuffer.allocate(vertexElements.size());
        gl.glGenBuffers(vertexElements.size(), vboBuffer);
		ListIterator<VertexData.VertexElement> itr = vertexElements.listIterator(0);
		while(itr.hasNext())
		{
			// Copy vertex data into float buffer
			VertexData.VertexElement e = itr.next();
			int dim = e.getNumberOfComponents();

	        FloatBuffer varr = FloatBuffer.allocate(indices.length * dim);
	        for (int i = 0; i < indices.length; i++) {
	            for (int j = 0; j < dim; j++) {
	                varr.put(e.getData()[dim * indices[i] + j]);
	            }
	        }
	        	        
	        // Make vertex buffer object 
	        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vboBuffer.get());
	        // Upload vertex data
	        gl.glBufferData(GL3.GL_ARRAY_BUFFER, varr.array().length * 4, FloatBuffer.wrap(varr.array()), GL3.GL_DYNAMIC_DRAW);	        
	        
	        // Tell OpenGL which "in" variable in the vertex shader corresponds to the current vertex buffer object
	        // We use our own convention to name the variables, i.e., "position", "normal", "color", "texcoord", or others if necessary
	        int attribIndex = -1;
	        if(e.getSemantic() == VertexData.Semantic.POSITION) {
	        	attribIndex = gl.glGetAttribLocation(activeShader.programId(), "position");
	        }	        	       
	        else if(e.getSemantic() == VertexData.Semantic.NORMAL) {
	        	attribIndex = gl.glGetAttribLocation(activeShader.programId(), "normal");
	        }	        
	        else if(e.getSemantic() == VertexData.Semantic.COLOR) {
	        	attribIndex  = gl.glGetAttribLocation(activeShader.programId(), "color");
	        }
	        else if(e.getSemantic() == VertexData.Semantic.TEXCOORD) {
	        	attribIndex = gl.glGetAttribLocation(activeShader.programId(), "texcoord");
	        }
        	gl.glVertexAttribPointer(attribIndex, dim, GL3.GL_FLOAT, false, 0, 0);
            gl.glEnableVertexAttribArray(attribIndex);        
		}
		
		// 3. Render the vertex buffer objects
		gl.glDrawArrays(GL3.GL_TRIANGLES, 0, indices.length);
		gl.glDeleteBuffers(vboBuffer.array().length, vboBuffer.array(), 0);		
		// 4. Clean up
        cleanMaterial(renderItem.getShape().getMaterial());
	}

	/**
	 * Pass the material properties to OpenGL, including textures and shaders.
	 * 
	 * To be implemented in the "Textures and Shading" project.
	 */
	private void setMaterial(Material m)
	{
		GLShader shader = (GLShader) m.getShader();
		if (shader != null) 
			activeShader = shader;
		else activeShader = defaultShader;
		
		activeShader.use();
		setLights();
		
		int id = gl.glGetUniformLocation(activeShader.programId(), "diffuseReflectionCoefficient");
		gl.glUniform1f(id, m.getDiffuseReflectionCoefficient());
		id = gl.glGetUniformLocation(activeShader.programId(), "specularReflectionCoefficient");
		gl.glUniform1f(id, m.getSpecularReflectionCoefficient());
		id = gl.glGetUniformLocation(activeShader.programId(), "phongExponent");
		gl.glUniform1f(id, m.getPhongExponent());
		GLTexture tex = (GLTexture) m.getTexture();
		
		if (tex == null) {
			tex = defaultTexture;
		}
		gl.glActiveTexture(GL3.GL_TEXTURE0);	// Work with texture unit 0
		gl.glEnable(GL3.GL_TEXTURE_2D);
		gl.glBindTexture(GL3.GL_TEXTURE_2D, tex.getId());
		gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_LINEAR);
		gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_LINEAR);
		id = gl.glGetUniformLocation(activeShader.programId(), "myTexture");
		gl.glUniform1i(id, 0);	// The variable in the shader needs to be set to the desired texture unit, i.e., 
		
		GLTexture glossMap = (GLTexture) m.getGlossMap();
		if (glossMap != null) {
			gl.glActiveTexture(GL3.GL_TEXTURE0 + 2);
			gl.glEnable(GL3.GL_TEXTURE_2D);
			gl.glBindTexture(GL3.GL_TEXTURE_2D, glossMap.getId());
			gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_LINEAR);
			gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_LINEAR);
			id = gl.glGetUniformLocation(activeShader.programId(), "glossMap");
			gl.glUniform1i(id, 2);	// The variable in the shader needs to be set to the desired texture unit, i.e., 0
		}
	}
	
	/**
	 * Pass the light properties to OpenGL. This assumes the list of lights in 
	 * the scene manager is accessible via a method Iterator<Light> lightIterator().
	 * 
	 * To be implemented in the "Textures and Shading" project.
	 */
	void setLights()
	{	
		final int MAX_LIGHTS = 8;
		float[] pointLightsPos = new float[3*MAX_LIGHTS];
		float[] pointLightsCol = new float[3*MAX_LIGHTS];
		float[] pointLightsRad = new float[MAX_LIGHTS];
		Iterator<PointLight> lightIter = sceneManager.lightIterator();
		for (int i = 0; i < MAX_LIGHTS && lightIter.hasNext(); i ++) {
			PointLight l = lightIter.next();
			fillTuple3fIntoArray(i, l.position, pointLightsPos);
			fillTuple3fIntoArray(i, l.color, pointLightsCol);
			pointLightsRad[i] = l.radiance;
		}
		
		//In case of camera update, this has to be informed
		Matrix4f camera = sceneManager.getCamera().getCameraMatrix();
		gl.glUniformMatrix4fv(gl.glGetUniformLocation(activeShader.programId(), "camera"), 1, false, matrix4fToFloat16(camera), 0);
		
		int id = gl.glGetUniformLocation(activeShader.programId(), "pointLightsPos");
		gl.glUniform3fv(id, MAX_LIGHTS, pointLightsPos, 0);
		id = gl.glGetUniformLocation(activeShader.programId(), "pointLightsCol");
		gl.glUniform3fv(id, MAX_LIGHTS, pointLightsCol, 0);
		id = gl.glGetUniformLocation(activeShader.programId(), "pointLightsRad");
		gl.glUniform1fv(id, MAX_LIGHTS, pointLightsRad, 0);
		
		//set directional light:
		id = gl.glGetUniformLocation(activeShader.programId(), "lightDirection");
		gl.glUniform4f(id, 0, 0, 1, 0);		// Set light direction

	}

	/**
	 * Disable a material.
	 * 
	 * To be implemented in the "Textures and Shading" project.
	 */
	private void cleanMaterial(Material m)
	{
		activeShader = defaultShader;
		activeShader.use();
		//TODO: somehow remove texture and so on...
	}

	public Shader makeShader()
	{
		return new GLShader(gl);
	}
	
	public Texture makeTexture()
	{
		return new GLTexture(gl);
	}
}
	