#version 430

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoords;

out vec2 passTex;

void main(void) {

	passTex = texCoords;
	gl_Position = vec4(position, 1);
}
