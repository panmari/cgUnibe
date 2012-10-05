Normal shaders:

These shader files lead to slightly more interesting shading than the default shaders. They require your meshes to have surface normals. See the ObjReader.java file for how normals are included in the vertex data.



How to use these shaders:

- copy these shaders into the jrtr/shaders directory
- in jrtr/GLRenderContext.java, search for default.vert and default.frag, replace these with normal.vert and normal.frag