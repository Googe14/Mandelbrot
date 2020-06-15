#version 430

in vec2 passTex;

uniform sampler2D textureSampler;

out vec4 outCol;

void main(void) {

	//outCol = vec4(passTex, 0, 1);
	outCol = texture(textureSampler, passTex);

}
